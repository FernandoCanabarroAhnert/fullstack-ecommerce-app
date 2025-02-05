package com.fernandocanabarro.fullstack_ecommerce_app.config.general;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fernandocanabarro.fullstack_ecommerce_app.interceptor.GlobalAttributesInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final GlobalAttributesInterceptor globalAttributesInterceptor;

    public WebConfig(GlobalAttributesInterceptor globalAttributesInterceptor) {
        this.globalAttributesInterceptor = globalAttributesInterceptor;
    }

    @SuppressWarnings("null")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalAttributesInterceptor);
    }
}
