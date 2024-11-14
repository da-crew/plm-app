package com.studentgroup.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.studentgroup.app.services.FilesStorageService;

import jakarta.annotation.Resource;


@SpringBootApplication
public class App implements CommandLineRunner{

    @Resource
    FilesStorageService storageService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
				registry.addMapping("/auth").allowedOrigins("*");
				registry.addMapping("/test/users").allowedOrigins("*");
			}
		};
	}

    @Override
    public void run(String... args) throws Exception {
        storageService.init();
    }
}