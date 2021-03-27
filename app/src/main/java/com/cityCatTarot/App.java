package com.cityCatTarot;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
    public String getGreeting() {
        return "Hello, city cat tarot!!";
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("=========== Server Start ===========");
        System.out.println(new App().getGreeting());
    }

    @Bean
    public Mapper dozerMapper(){
        return DozerBeanMapperBuilder.buildDefault();
    }
}
