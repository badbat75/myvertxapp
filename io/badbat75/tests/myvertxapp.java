package io.badbat75.tests;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
//import io.vertx.ext.web.RoutingContext;
//import io.vertx.ext.web.handler.BodyHandler;
//import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class myvertxapp extends AbstractVerticle {
    private static final int serverPort = 8080;

    private Future<Void> startHttpServer() {
        Future<Void> future = Future.future();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
//        router.get("/").handler(this::indexHandler);
//        router.get("/wiki/:page").handler(this::pageRenderingHandler);
//        router.post().handler(BodyHandler.create());

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

    // Only needed for embedded Java code!!!
    public static void main(String[] args) throws java.io.IOException {
        final String[] deployid = {""};
        Future<Void> future = Future.future();
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new myvertxapp(), ar -> {
            if (ar.succeeded()) {
                deployid[0] = ar.result();
                System.out.println("Deployed. ID: "+deployid[0]+"\n");
        //        future.complete();
            } else {
                System.out.println("Not deployed\n");
                future.fail(ar.cause());
                System.exit(1);
            }                   
        });
        System.out.println("Press a key to undeploy\n");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        System.out.println("Undeploying: "+deployid[0]);
        vertx.undeploy(deployid[0], ar -> {
            if (ar.succeeded()) {
                System.out.println("Undeployed\n");
                future.complete();
                System.exit(0);
            } else {
                System.out.println("Not undeployed\n");
                future.fail(ar.cause());
                System.exit(1);
            }                   
        });
        
    }

}