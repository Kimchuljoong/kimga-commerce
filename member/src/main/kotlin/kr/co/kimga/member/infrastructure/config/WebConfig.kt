package kr.co.kimga.member.infrastructure.config

import kr.co.kimga.member.infrastructure.interceptor.AuthInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .excludePathPatterns("/api/v1/auth/login")
            .addPathPatterns("/api/v1/auth/**")
            .excludePathPatterns("/api/v1/member/new")
            .addPathPatterns("/api/v1/member/**")
    }
}