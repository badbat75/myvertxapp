import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.badbat75.tests.myvertxapp;

public class Main {
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