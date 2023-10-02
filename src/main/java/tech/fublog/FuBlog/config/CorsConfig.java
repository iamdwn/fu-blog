//package com.blogschool.blogs.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**") // Thay thế bằng đường dẫn API thực sự của bạn.
//                .allowedOrigins("http://localhost:5173") // Cho phép truy cập từ domain này.
//                .allowedMethods("GET", "POST", "PUT", "DELETE") // Cho phép các phương thức HTTP này.
//                .allowCredentials(true); // Cho phép sử dụng cookie và thông tin xác thực.
//
//        // Bạn có thể cấu hình thêm các tùy chọn CORS khác tùy theo nhu cầu của bạn.
//    }
//}
