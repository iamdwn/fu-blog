//package tech.fublog.FuBlog.config;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.Contact;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.security.SecuritySchemes;
//import org.springframework.context.annotation.Configuration;
//
//import org.springdoc.core.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@Api
//@OpenAPIDefinition
//public class OpenApiConfig {
//
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("tech.fublog.FuBlog.controller")
//                .pathsToMatch("/api/v1/auth/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi privateApi() {
//        return GroupedOpenApi.builder()
//                .group("tech.fublog.FuBlog.controller")
//                .packagesToScan("tech.fublog.FuBlog.controller")
//                .build();
//    }
//}
//
