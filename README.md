# Multiple HTTP connectors in Spring Boot

There some cases when you would like to use multiple connectors in one Spring Boot application. And one of those cases is when you application handles GET requests containing [percent-encoded](https://en.wikipedia.org/wiki/Percent-encoding) non-ASCII data in different charset. For example, one HTTP endpoint uses standard UTF-8 while the other uses [Windows-1251](https://en.wikipedia.org/wiki/Windows-1251).

According to [How do I change how GET parameters are interpreted?](http://wiki.apache.org/tomcat/FAQ/CharacterEncoding#Q2) the only way to specify GET request encoding is to use by-connector `URIEncoding` attribute. For example:

```xml
 <Connector port="8081" URIEncoding="utf-8"/>
 <Connector port="8082" URIEncoding="cp1251"/>
```

Then you have map servlets to different connectors somehow.

Spring Boot can help you out in this matter. Although it uses the only one URI encoding which is specified in `server.tomcat.uri-encoding` parameter ("UTF-8" by default, see [Appendix A. Common application properties](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)), it can fire up multiple child applications residing on different ports.

Implementation is really simple as you can see from [Application.java](src/main/java/com/github/dddpaul/Application.java).

Pros:
* no hacks and workarounds, pure Spring Boot solution :)
* controller unit tests are passed.
  
Cons:
* some mess with controllers mappings;
* integration tests (with full context initialization) are failed on non-ASCII requests; in fact, you have no option to point which connector are used in test;
* say bye-bye to Spring Boot actuators, you'll have to use some workarounds to plug them in.  

Links:
* [Spring-Boot : How can I add tomcat connectors to bind to controller](http://stackoverflow.com/questions/26111050/spring-boot-how-can-i-add-tomcat-connectors-to-bind-to-controller)
