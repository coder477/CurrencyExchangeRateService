package com.bk.exchange.config;



import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExchangeRateAPIConfig {
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
				.rootUri("https://api.exchangeratesapi.io")
				.messageConverters(new MappingJackson2HttpMessageConverter()).
				build();
	}
}
