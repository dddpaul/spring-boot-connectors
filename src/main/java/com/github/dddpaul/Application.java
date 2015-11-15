package com.github.dddpaul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
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
        public String getMessage(String name) {
            return String.format("Hello, %s, I'm shared service", name);
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

            @RequestMapping(produces = "text/plain;charset=utf-8")
            @ResponseBody
            public String getMessage(String name) {
                return "ControllerOne says \"" + service.getMessage(name) + "\"";
            }
        }
    }

    @Configuration
    @EnableAutoConfiguration
    static class ServiceTwoConfiguration {
        @Bean
        EmbeddedServletContainerFactory servletContainer() {
            TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
            tomcat.setUriEncoding("cp1251");
            return tomcat;
        }

        @Controller
        @RequestMapping("/two")
        static class ControllerTwo {
            @Autowired
            private SharedService service;

            @RequestMapping(produces = "text/plain;charset=utf-8")
            @ResponseBody
            public String getMessage(String name) {
                System.out.println(name);
                System.out.println(service.getMessage(name));
                return "ControllerTwo says \"" + service.getMessage(name) + "\"";
            }
        }
    }
}
