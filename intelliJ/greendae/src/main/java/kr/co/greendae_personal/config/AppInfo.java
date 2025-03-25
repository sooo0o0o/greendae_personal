package kr.co.greendae_personal.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class AppInfo {

    @Value("${spring.application.name}")    //application.yml 파일에 속성값으로 초기화
    private String appName;

    @Value("${spring.application.version}")
    private String appVersion;
}
