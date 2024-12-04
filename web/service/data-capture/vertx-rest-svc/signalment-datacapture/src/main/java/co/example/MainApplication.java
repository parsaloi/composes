package co.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.example.verticle.DatabaseVerticle;
import co.example.verticle.HttpVerticle;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.Vertx;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.DeploymentOptions;

public class MainApplication extends AbstractVerticle {
  
    @Override
    public Completable rxStart() {
        return vertx.deployVerticle(DatabaseVerticle::new, new DeploymentOptions().setInstances(1))
            .flatMap(id -> {
                logger.info("Database Verticle deployed: {}", id);
                return vertx.deployVerticle(HttpVerticle::new, new DeploymentOptions().setInstances(1));
            })
            .ignoreElement();  // Convert Single<String> to Completable
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainApplication::new, new DeploymentOptions().setInstances(1))
            .subscribe(
                id -> logger.info("Main Verticle deployed: {}", id),
                error -> {
                    logger.error("Failed to deploy main verticle", error);
                    vertx.close();
                }
            );
    }
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
}
