package tech.fublog.FuBlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class FuBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuBlogApplication.class, args);
	}

}
