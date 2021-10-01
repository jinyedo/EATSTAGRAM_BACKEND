package daelim.project.eatstagram;

import daelim.project.eatstagram.storage.StorageProperties;
import daelim.project.eatstagram.storage.StorageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class EatstagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatstagramApplication.class, args);
    }

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("현재 시간: " + new Date());
    }

    @Bean
    CommandLineRunner init(StorageRepository storageRepository) {
        return (args) -> {
            // storageRepository.deleteAll();
            storageRepository.init();
        };
    }
}
