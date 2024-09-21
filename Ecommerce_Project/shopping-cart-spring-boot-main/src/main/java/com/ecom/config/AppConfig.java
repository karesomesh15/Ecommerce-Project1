package com.ecom.config;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
@Configuration
public class AppConfig {
 @Bean
 public RestTemplate restTemplate() {
 return new RestTemplate();
 }
public ResponseEntity<Map> getForEntity(String apiUrl, Class<Map> class1) {
// TODO Auto-generated method stub
return null;
}
}