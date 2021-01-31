package com.orkun.routes;

import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import io.quarkus.vertx.web.Route;


import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyDeclarativeRoutes {

    // neither path nor regex is set - match a path derived from the method name
    @Route(methods = HttpMethod.GET)
    void hello(RoutingContext rc){
        rc.response().end("Hello from the my world");
    }

    @Route(path = "/world")
    String helloWorld(){
        return "Hello worlds!";
    }

    @Route(path = "/greetings", methods = HttpMethod.GET)
    void greetings(RoutingExchange ex) {
        ex.ok("hello " + ex.getParam("name").orElse("world"));
    }
}
