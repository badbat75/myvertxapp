package io.badbat75.tests;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
//import io.vertx.ext.web.handler.BodyHandler;
//import io.vertx.ext.web.handler.StaticHandler;

public class myvertxapp extends AbstractVerticle {
    private static final int serverPort = 8080;

    private Future<Void> startHttpServer() {
        Future<Void> future = Future.future();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                .putHeader("content-type", "text/html")
                .end("<html><h1>Hello World from Vert.x-Web!</h1><h2>Context: /</h2></html>");
        });

        router.route().handler(routingContext -> {
            // Get the given context
            String givenContext = routingContext.normalisedPath();
            HttpServerResponse response = routingContext.response();

            response
                .putHeader("content-type", "text/html")
                .setStatusCode(404)
                .end("<html><h1>Error!!!</h1><h2>You are in "+givenContext+"</h2></html>");
        });

        server
            .requestHandler(router::accept)

            .listen(serverPort, ar -> {
                if (ar.succeeded()) {
                    System.out.println("HTTP Server running on port "+serverPort+"\n");
                    future.complete();
                } else {
                    System.out.println("Could not start an HTTP Server\n"+ar.cause());
                    future.fail(ar.cause());
                }                   
            });
        return future;
    }

    public void start(Future<Void> future) throws Exception {

        Future<Void> steps = startHttpServer();

        steps.setHandler(ar -> {
            if (ar.succeeded()) {
                System.out.println("Service started successfully\n");
                future.complete();
            } else {
                System.out.println("Service has not been started\n");
                future.fail(ar.cause());
            }
        });
    }   

}