package daelim.project.eatstagram.websocket;

import daelim.project.eatstagram.service.biz.ContentBizService;
import daelim.project.eatstagram.service.biz.DirectMessageRoomBizService;
import daelim.project.eatstagram.service.content.ContentDTO;
import daelim.project.eatstagram.service.contentFile.ContentFileDTO;
import daelim.project.eatstagram.service.contentReply.ContentReplyDTO;
import daelim.project.eatstagram.service.contentReply.ContentReplyService;
import daelim.project.eatstagram.service.directMessage.DirectMessageDTO;
import daelim.project.eatstagram.service.directMessage.DirectMessageService;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberDTO;
import daelim.project.eatstagram.service.directMessageRoomMember.DirectMessageRoomMemberService;
import daelim.project.eatstagram.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class WebsocketHandler extends TextWebSocketHandler {

    private final MemberService memberService;

    private final ContentBizService contentBizService;
    private final ContentReplyService contentReplyService;

    private final DirectMessageService directMessageService;
    private final DirectMessageRoomBizService directMessageRoomBizService;
    private final DirectMessageRoomMemberService directMessageRoomMemberService;

    List<LinkedHashMap<String, Object>> sessionList = new ArrayList<>(); // 웹소켓 세션을 담아둘 리스트
    int fileUploadIdx = 0; // 파일을 전송한 방의 번호
    String filename = "";

    @SuppressWarnings("unchecked")
    @Override // 소켓 연결
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        String url = session.getUri().toString();
        log.info("[웹소켓 연결] URL : " + url + ", sessionId: " + session.getId());
        String roomType = url.split("/")[5]; // 연결된 방의 type
        String roomId = url.split("/")[6]; // 연결된 방의 ID
        log.info(roomType);
        if (roomType.equals("directMessage")) {
            int idx = roomId.indexOf("?");
            roomId = roomId.substring(0, idx);
        }
        log.info(roomId);

        boolean flag = false;
        int idx = 0;

        // 존재하는 방인지 검사
        if (sessionList.size() > 0) {
            for (int i=0; i<sessionList.size(); i++) {
                String rn = (String) sessionList.get(i).get("roomId");
                String rt = (String) sessionList.get(i).get("roomType");
                if (rn.equals(roomId) && rt.equals(roomType)) {
                    flag = true;
                    idx = i;
                    break;
                }
            }
        }

        if (flag) { // 존재하는 방이라면 세션만 추가한다.
            LinkedHashMap<String, Object> map = sessionList.get(idx);
            map.put(session.getId(), session);
        } else { // 최초 생성하는 방이라면 방 타입과, 방 아이디, 세션을 추가한다.
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("roomType", roomType);
            map.put("roomId", roomId);
            map.put(session.getId(), session);
            sessionList.add(map);
        }
    }

    @Override // 메시지 발송 - Text 데이터가 들어오면 실행
    @Transactional(rollbackFor = Exception.class)
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("----- request websocket text data -----");

        JSONObject obj = jsonToObjectParse(message.getPayload()); // JSON 데이터를 JSONObject 로 파싱한다.
        log.info("requestObj : " + obj);

        String requestRoomType = (String) obj.get("roomType");

        // 방을 만들기 위한 요청이라면
        if (requestRoomType.equals("directMessageRoomList")) {
            List<String> requestUserList = (List<String>) obj.get("userList");
            // 채팅방 생성
            JSONObject jsonObj =  createDirectMessageRoom(requestUserList);
            // 방을 생성하기 위한 데이터를 클라이언트로 전송
            sendDirectMessageRoomListMessage(requestUserList, jsonObj);

        // 댓글 및 채팅을 위한 요청이라면
        } else {
            String requestMsgType = (String) obj.get("type");
            String requestMsg = requestMsgType.equals("file") ? UUID.randomUUID() + "_" + obj.get("msg") : (String) obj.get("msg");
            String requestRoomId = obj.get("roomId") != null ? (String) obj.get("roomId"): null; // 방의 번호를 받는다.
            String requestUsername = (String) obj.get("username"); // 회원의 ID 를 받는다.

            // 댓글을 위한 요청이라면
            if (requestRoomType.equals("contentReply")) {
                // DB에 댓글저장
                contentReplyService.save(ContentReplyDTO.builder()
                        .reply(requestMsg)
                        .contentId(requestRoomId)
                        .username(requestUsername)
                        .build());

                obj.put("profileImgName", memberService.getMemberInfo(requestUsername).getProfileImgName());

                // 댓글을 전송
                sendMessage(requestRoomType, requestRoomId, requestMsgType, requestMsg, obj);

            // 채팅을 위한 요청이라면
            } else if (requestRoomType.equals("directMessage")) {

                // 공유
                Map<String, Object> requestShareData = new HashMap<>();
                if (requestMsgType.equals("share")) {
                    requestShareData = (Map<String, Object>) obj.get("shareData");
                    String thumbnail = (String) requestShareData.get("thumbnail");

                    if (StringUtils.isEmpty(requestRoomId)) {
                        List<String> userList = (List<String>) requestShareData.get("userList");

                        JSONObject createDirectMessageRoomJsonObj = createDirectMessageRoom(userList);
                        sendDirectMessageRoomListMessage(userList, createDirectMessageRoomJsonObj);

                        requestRoomId = (String) createDirectMessageRoomJsonObj.get("directMessageRoomId");
                    }

                    ContentDTO contentDTO = contentBizService.findByContentId(requestMsg);

                    obj.clear();
                    obj.put("contentId", requestMsg);
                    obj.put("type", "share");
                    obj.put("thumbnail", thumbnail);
                    obj.put("username", requestUsername);
                    obj.put("contentUsername", contentDTO.getUsername());
                    obj.put("nickname", contentDTO.getNickname());
                    obj.put("profileImgName", contentDTO.getProfileImgName());
                    obj.put("text", contentDTO.getText());
                    obj.put("location", contentDTO.getLocation());
                    List<Map<String, String>> contentFileDTOList = new ArrayList<>();
                    for (ContentFileDTO contentFileDTO : contentDTO.getContentFileDTOList()) {
                        Map<String, String> contentFileInfo = new HashMap<>();
                        contentFileInfo.put("contentFileId", contentFileDTO.getContentFileId());
                        contentFileInfo.put("name", contentFileDTO.getName());
                        contentFileInfo.put("type", contentFileDTO.getType());
                        contentFileDTOList.add(contentFileInfo);
                    }
                    obj.put("contentFileDTOList", contentFileDTOList);
                    requestMsg = obj.toJSONString();
                }

                // DB에 채팅 저장
                directMessageService.save(DirectMessageDTO.builder()
                        .directMessage(requestMsg)
                        .directMessageType(requestMsgType)
                        .directMessageRoomId(requestRoomId)
                        .username(requestUsername)
                        .build());

                // 채팅 전송
                sendMessage(requestRoomType, requestRoomId, requestMsgType, requestMsg, obj);

                // 채팅방 재 초대 및 채팅 알림을 날리기 위해 해당 채팅방에 채팅을 보낸 회원을 제외한 나머지 회원들 리스트를 가져온다.
                List<DirectMessageRoomMemberDTO> userList
                        = directMessageRoomMemberService.getRepository().findByDirectMessageRoomId(requestRoomId, requestUsername);

                List<DirectMessageRoomMemberDTO> confirmUserList = new ArrayList<>();
                confirmUserList.add(DirectMessageRoomMemberDTO.builder().username(requestUsername).build());
                confirmUserList.addAll(userList);

                // 메시지를 보낸 회원을 제외한 나머지 회원들 만큼 반복
                for (DirectMessageRoomMemberDTO user : userList) {
                    String connectionYn = directMessageRoomMemberService.getConnectionYn(requestRoomId, user.getUsername());
                    String alertYn = directMessageRoomMemberService.getAlertYn(requestRoomId, user.getUsername());
                    String inYn = directMessageRoomMemberService.getInYn(requestRoomId, user.getUsername());

                    // 해당 회원이 채팅방을 나가있으면
                    if (inYn.equals("N")) {
                        Map<String, Object> result = directMessageRoomBizService.add(confirmUserList);
                        JSONObject jsonObj =  new JSONObject(result);
                        sendDirectMessageRoomListMessage(user.getUsername(), jsonObj);
                        sendDirectMessageAlertMessage(requestRoomId, "header", user.getUsername(), "N");
                        sendDirectMessageAlertMessage(requestRoomId, "directMessageRoomList", user.getUsername(), "N");
                    } else {
                        if (connectionYn.equals("N")) { // 해당 회원이 채팅방에 접속중이 아니라면
                            // 해당 회원의 헤더 웹소켓과, 채팅방 목록 웹소켓에 알림 전송(안읽음 표시를 해주기 위해)
                            sendDirectMessageAlertMessage(requestRoomId, "header", user.getUsername(), alertYn);
                            sendDirectMessageAlertMessage(requestRoomId, "directMessageRoomList", user.getUsername(), alertYn);
                        }
                    }
                    if (alertYn.equals("N")) {
                        // 해당 채팅방에 알림이 날라갔음을 DB에 반영해 준다.
                        directMessageRoomMemberService.updateAlertYn(requestRoomId, user.getUsername(), "Y");
                    }
                }
            }
        }

        log.info("---------------------------------------");
    }

    @Override // 바이너리 메시지 발송 - BinaryMessage 의 데이터가 들어오면 실행
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        ByteBuffer byteBuffer = directMessageService.fileSave(filename, message);

        // 파일 쓰기가 끝나면 이미지를 발송한다.
        LinkedHashMap<String, Object> temp = sessionList.get(fileUploadIdx);
        for (String k : temp.keySet()) {
            if (k.equals("roomType") || k.equals("roomId")) continue;
            WebSocketSession wss = (WebSocketSession) temp.get(k);
            try {
                wss.sendMessage(new BinaryMessage(byteBuffer)); // 초기화된 버퍼를 발송하낟.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // 소켓 종료
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (sessionList.size() > 0) {
            log.info("[웹소켓 종료] URL : " + session.getUri().toString() + ", sessionId: " + session.getId());
            String roomType = session.getUri().toString().split("/")[5]; // 종료된 방의 type
            String roomId = session.getUri().toString().split("/")[6]; // 종료된 방의 ID
            if (roomType.equals("directMessageRoomList")) { // 해당 회원의 채팅방 목록 웹소켓이 끊어지면 해당 회원의 전체 채팅방의 연결 상태를 N으로 변경한다.
                directMessageRoomMemberService.updateAllConnectionYn(roomId, "N");
            } else if (roomType.equals("directMessage")) {
                int directMessageRoomIdIndex = session.getUri().toString().split("/")[6].indexOf("?");
                String directMessageRoomId = session.getUri().toString().split("/")[6].substring(0, directMessageRoomIdIndex);
                int usernameIndex = session.getUri().toString().split("/")[6].indexOf("=");
                String username = session.getUri().toString().split("/")[6].substring(usernameIndex + 1);
                directMessageRoomMemberService.updateAlertYn(directMessageRoomId, username, "N");
            }
            for (LinkedHashMap<String, Object> linkedHashMap : sessionList) {
                linkedHashMap.remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }

    private static JSONObject jsonToObjectParse(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private JSONObject createDirectMessageRoom(List<String> userList){
        List<DirectMessageRoomMemberDTO> directMessageRoomMemberDTOList = new ArrayList<>();
        for (String user : userList) {
            directMessageRoomMemberDTOList.add(DirectMessageRoomMemberDTO.builder().username(user).build());
        }
        Map<String, Object> result = directMessageRoomBizService.add(directMessageRoomMemberDTOList);
        return new JSONObject(result);
    }

    // 클라이언트에게 채팅 알림 메시지 전송
    private void sendDirectMessageAlertMessage(String requestRoomId, String conditionRoomType, String conditionUsername, String alertYn) {
        LinkedHashMap<String, Object> temp = new LinkedHashMap<>();
        if (sessionList.size() > 0) {
            for (LinkedHashMap<String, Object> map : sessionList) {
                String roomType = (String) map.get("roomType");
                String roomId = (String) map.get("roomId");
                if (roomType.equals(conditionRoomType) && roomId.equals(conditionUsername)) {
                    temp = map;
                    break;
                }
            }

            for (String k : temp.keySet()) {
                if (k.equals("roomType") || k.equals("roomId")) continue;
                WebSocketSession wss = (WebSocketSession) temp.get(k);
                if (wss != null) {
                    try {
                        log.info("wss : " + wss);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("websocketType", conditionRoomType);
                        jsonObject.put("websocketId" , conditionUsername);
                        jsonObject.put("directMessageRoomId", requestRoomId);
                        jsonObject.put("alertYn", alertYn);
                        jsonObject.put("type", "alert");
                        wss.sendMessage(new TextMessage(jsonObject.toJSONString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 클라이언트에게 메시지 전송
    private void sendMessage(String requestRoomType, String requestRoomId, String requestMsgType, String requestMsg, JSONObject obj) {
        LinkedHashMap<String, Object> temp = new LinkedHashMap<>();
        if (sessionList.size() > 0) {
            for (int i = 0; i < sessionList.size(); i++) {
                String roomType = (String) sessionList.get(i).get("roomType");
                String roomId = (String) sessionList.get(i).get("roomId"); // 세션리스트의 저장된 방 번호를 가져와
                if (roomType.equals(requestRoomType) && roomId.equals(requestRoomId)) { // 같은값이 방이 존재한다면
                    temp = sessionList.get(i); // 해당 방번호의 세션리스트의 존재하는 모든 object 값을 가져온다.
                    fileUploadIdx = i;
                    filename = requestMsgType.equals("file") ? requestMsg : null;
                    break;
                }
            }

            // 해당 방의 세션들만 찾아서 메시지를 발송해준다.
            for (String k : temp.keySet()) {
                if (k.equals("roomType") || k.equals("roomId")) continue; // key 가 roomType 이거나 roomId 면 건너뛴다.
                WebSocketSession wss = (WebSocketSession) temp.get(k);
                if (wss != null) {
                    try {
                        if (requestMsgType.equals("file")) obj.put("msg", filename);
                        obj.put("regDate", LocalDateTime.now().toString());
                        wss.sendMessage(new TextMessage(obj.toJSONString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 클라이언트에게 채팅방을 만들기 위한 데이터 전송 - 여러명에게
    private void sendDirectMessageRoomListMessage(List<String> requestUserList, JSONObject obj) {
        List<LinkedHashMap<String, Object>> tempList = new ArrayList<>();
        if (sessionList.size() > 0) {
            for (LinkedHashMap<String, Object> element : sessionList) {
                String roomType = (String) element.get("roomType");
                String roomId = (String) element.get("roomId");
                for (String k : requestUserList) {
                    if (roomType.equals("directMessageRoomList") && roomId.equals(k)) {
                        tempList.add(element);
                    }
                }
            }

            for (LinkedHashMap<String, Object> temp : tempList) {
                for (String k : temp.keySet()) {
                    String directMessageRoomId = obj.get("directMessageRoomId").toString();
                    if (k.equals("roomId")) {
                        String username = temp.get(k).toString();
                        String inYn = directMessageRoomMemberService.getInYn(directMessageRoomId, username);
                        obj.put("inYn", inYn);
                        if (inYn.equals("N")) {
                            directMessageRoomMemberService.updateInYn(directMessageRoomId, username, "Y");
                        }
                        continue;
                    } else if (k.equals("roomType")) {
                        continue;
                    }
                    WebSocketSession wss = (WebSocketSession) temp.get(k);
                    if (wss != null) {
                        try {
                            wss.sendMessage(new TextMessage(obj.toJSONString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // 클라이언트에게 채팅방을 만들기 위한 데이터 전송 - 특정 회원에게만
    private void sendDirectMessageRoomListMessage(String username, JSONObject obj) {
        LinkedHashMap<String, Object> temp = new LinkedHashMap<>();
        if (sessionList.size() > 0) {
            for (LinkedHashMap<String, Object> element : sessionList) {
                String roomType = (String) element.get("roomType");
                String roomId = (String) element.get("roomId");
                if (roomType.equals("directMessageRoomList") && roomId.equals(username)) {
                    temp = element;
                }
            }

            for (String k : temp.keySet()) {
                String directMessageRoomId = obj.get("directMessageRoomId").toString();
                if (k.equals("roomId") || k.equals("roomType")) continue;
                directMessageRoomMemberService.updateInYn(directMessageRoomId, username, "Y");
                WebSocketSession wss = (WebSocketSession) temp.get(k);
                if (wss != null) {
                    try {
                        obj.put("inYn", "N");
                        wss.sendMessage(new TextMessage(obj.toJSONString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
