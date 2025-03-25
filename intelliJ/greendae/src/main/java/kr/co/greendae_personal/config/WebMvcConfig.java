package kr.co.greendae_personal.config;

import kr.co.greendae_personal.interceptor.AppInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    private final AppInfo appInfo;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new AppInterceptor(appInfo));

    }

}
