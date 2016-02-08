package ru.live.toofast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

@SpringBootApplication
@EnableRetry
public class DiningHallSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiningHallSimulatorApplication.class, args);
    }



}
