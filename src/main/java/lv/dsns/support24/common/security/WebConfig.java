//    package lv.dsns.support24.common.security;
//
//    import com.fasterxml.jackson.databind.ObjectMapper;
//    import com.fasterxml.jackson.databind.SerializationFeature;
//    import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//    import org.springframework.context.annotation.Bean;
//    import org.springframework.context.annotation.Configuration;
//    import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//    import org.springframework.web.servlet.config.annotation.CorsRegistry;
//    import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//    @Configuration
//    @EnableWebMvc
//    public class WebConfig implements WebMvcConfigurer {
//
//        @Override
//        public void addCorsMappings(CorsRegistry registry) {
//            registry.addMapping("/**")
//                    .allowedOrigins("*")
//                    .allowedHeaders("*")
//                    .allowedMethods("GET","PATCH","POST","OPTIONS","DELETE");
//        }
//
//        @Bean
//        public ObjectMapper objectMapper() {
//            return Jackson2ObjectMapperBuilder.json()
//                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                    .modules(new JavaTimeModule())
//                    .build();
//        }
//    }