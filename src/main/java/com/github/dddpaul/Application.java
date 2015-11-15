package com.github.dddpaul;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplicationBuilder parentBuilder = new SpringApplicationBuilder(ApplicationConfiguration.class);
        parentBuilder.child(ServiceOneConfiguration.class).properties("server.port:8081").run(args);
        parentBuilder.child(ServiceTwoConfiguration.class).properties("server.port:8082").run(args);
    }

    static class SharedService {
        public String getMessage() {
            return "I'm shared service";
        }
    }

    @Configuration
    static class ApplicationConfiguration {
        @Bean
        public SharedService sharedService() {
            return new SharedService();
        }
    }

    @Configuration
    @EnableAutoConfiguration
    static class ServiceOneConfiguration {
        @Bean
        public ControllerOne controller(SharedService service) {
            return new ControllerOne(service);
        }
    }

    @Configuration
    @EnableAutoConfiguration
    static class ServiceTwoConfiguration {
        @Bean
        public ControllerTwo controller(SharedService service) {
            return new ControllerTwo(service);
        }
    }

    @RequestMapping("/one")
    private static class ControllerOne {
        private SharedService service;

        public ControllerOne(SharedService service) {
            this.service = service;
        }

        @RequestMapping
        @ResponseBody
        public String getMessage() {
            return "ControllerOne says \"" + this.service.getMessage() + "\"";
        }
    }

    @RequestMapping("/two")
    private static class ControllerTwo {
        private SharedService service;

        public ControllerTwo(SharedService service) {
            this.service = service;
        }

        @RequestMapping
        @ResponseBody
        public String getMessage() {
            return "ControllerTwo says \"" + this.service.getMessage() + "\"";
        }
    }
}
