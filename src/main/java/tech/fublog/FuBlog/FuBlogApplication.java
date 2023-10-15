package tech.fublog.FuBlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tech.fublog.FuBlog.repository.BlogPostRepository;
@SpringBootApplication
public class FuBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuBlogApplication.class, args);
	}

}
