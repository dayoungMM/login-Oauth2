package com.moondy.loginoauth2.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

//우선 RestTemplate를 매번 새로 생성하기 보다 빈으로 만들어서 의존성을 주입받도록 한다.
@Configuration
public class RestTemplateConfig {
    //HTTP get,post 요청을 날릴때 일정한 형식에 맞춰주는 template
    @Bean
    public RestTemplate restTemplate() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return new RestTemplate(factory);
    }
}

////활용 예시
//restTemplate.exchange(url, httpMethod, new HttpEntity<>(body, httpHeaders), class);
//restTemplate.postForEntity(url, parameter,class);