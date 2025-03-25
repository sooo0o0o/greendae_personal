package kr.co.greendae_personal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기본 static 경로 설정 (예: /static/ 경로로 접근할 수 있도록 설정)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}