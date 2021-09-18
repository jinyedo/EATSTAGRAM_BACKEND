package daelim.project.eatstagram.websocket;

import daelim.project.eatstagram.service.contentReply.ContentReplyDTO;
import daelim.project.eatstagram.service.contentReply.ContentReplyService;
import daelim.project.eatstagram.service.directMessage.DirectMessageDTO;
import daelim.project.eatstagram.service.directMessage.DirectMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketHandler extends TextWebSocketHandler {

    List<LinkedHashMap<String, Object>> sessionList = new ArrayList<>(); // 웹소켓 세션을 담아둘 리스트

    private final ContentReplyService contentReplyService;
    private final DirectMessageService directMessageService;

    @SuppressWarnings("unchecked")
    @Override // 소켓 연결
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        String url = session.getUri().toString();
        log.info("[웹소켓 연결] URL : " + url + ", sessionId: " + session.getId());
        String roomType = url.split("/")[4]; // 연결된 방의 type
        String roomId = url.split("/")[5]; // 연결된 방의 ID

        boolean flag = false;
        int idx = 0;

        if (sessionList.size() > 0) {
            for (int i=0; i<sessionList.size(); i++) {
                String rn = (String) sessionList.get(i).get("roomId");
                if (rn.equals(roomId)) {
                    flag = true;
                    idx = i;
                    break;
                }
            }
        }

        if (flag) { // 존재하는 방이라면 세션만 추가한다.
            LinkedHashMap<String, Object> map = sessionList.get(idx);
            map.put(session.getId(), session);
        } else { // 최초 생성하는 방이라면 방번호와 세션을 추가한다.
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("roomType", roomType);
            map.put("roomId", roomId);
            map.put(session.getId(), session);
            sessionList.add(map);
        }
    }

    // 메시지 발송
    @Override // Text 데이터가 들어오면 실행
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("----- request websocket text data -----");
        JSONObject obj = jsonToObjectParse(message.getPayload()); // JSON 데이터를 JSONObject 로 파싱한다.
        log.info("requestObj : " + obj);

        String requestMsg = (String) obj.get("msg");
        String requestMsgType = (String) obj.get("type");
        String requestRoomType = (String) obj.get("roomType");
        String requestRoomId = (String) obj.get("roomId"); // 방의 번호를 받는다.
        String requestUsername = (String) obj.get("username"); // 회원의 ID 를 받는다.

        if (requestRoomType.equals("contentReply")) {
            contentReplyService.save(ContentReplyDTO.builder()
                    .reply(requestMsg)
                    .contentId(requestRoomId)
                    .username(requestUsername)
                    .build());
        } else if (requestRoomType.equals("directMessage")) {
            directMessageService.save(DirectMessageDTO.builder()
                    .directMessage(requestMsg)
                    .directMessageType(requestMsgType)
                    .directMessageRoomId(requestRoomId)
                    .username(requestUsername)
                    .build());
        }

        LinkedHashMap<String, Object> temp = new LinkedHashMap<>();

        if (sessionList.size() > 0) {
            for (LinkedHashMap<String, Object> linkedHashMap : sessionList) {
                String roomId = (String) linkedHashMap.get("roomId"); // 세션리스트의 저장된 방 번호를 가져와
                if (roomId.equals(requestRoomId)) { // 같은값이 방이 존재한다면
                    temp = linkedHashMap; // 해당 방번호의 세션리스트의 존재하는 모든 object 값을 가져온다.
                    break;
                }
            }

            // 해당 방의 세션들만 찾아서 메시지를 발송해준다.
            for (String k : temp.keySet()) {
                if (k.equals("roomType") || k.equals("roomId")) continue; // key 가 roomType 이거나 roomId 면 건너뛴다.
                WebSocketSession wss = (WebSocketSession) temp.get(k);
                if (wss != null) {
                    try {
                        obj.put("regDate", LocalDateTime.now().toString());
                        wss.sendMessage(new TextMessage(obj.toJSONString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        log.info("---------------------------------------");
    }

    @Override // 소켓 종료
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (sessionList.size() > 0) {
            log.info("[웹소켓 종료] URL : " + session.getUri().toString() + ", sessionId: " + session.getId());
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
}