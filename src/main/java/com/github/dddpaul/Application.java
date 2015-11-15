package com.github.dddpaul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplicationBuilder parentBuilder = new SpringApplicationBuilder(Application.class);
        parentBuilder.child(ServiceOneConfiguration.class).properties("server.port:8081").run(args);
        parentBuilder.child(ServiceTwoConfiguration.class).properties("server.port:8082").run(args);
    }

    @Service
    static class SharedService {
        public String getMessage() {
            return "I'm shared service";
        }
    }

    @Configuration
    @EnableAutoConfiguration
    static class ServiceOneConfiguration {
        @Controller
        @RequestMapping("/one")
        static class ControllerOne {
            @Autowired
            private SharedService service;

            @RequestMapping
            @ResponseBody
            public String getMessage() {
                return "ControllerOne says \"" + service.getMessage() + "\"";
            }
        }
    }

    @Configuration
    @EnableAutoConfiguration
    static class ServiceTwoConfiguration {
        @Controller
        @RequestMapping("/two")
        static class ControllerOne {
            @Autowired
            private SharedService service;

            @RequestMapping
            @ResponseBody
            public String getMessage() {
                return "ControllerTwo says \"" + service.getMessage() + "\"";
            }
        }
    }
}
