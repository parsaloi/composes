package co.example.verticle;

import co.example.model.ResponseData;
import co.example.model.SignalmentData;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.eventbus.Message;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.rxjava3.ext.web.handler.CSPHandler;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpVerticle extends AbstractVerticle {

    private static final String INSERT_SIGNALMENT_ADDRESS = 
        System.getenv()
            .getOrDefault("INSERT_SIGNALMENT_ADDRESS", "signament.insert");

    @Override
    public Completable rxStart() {
        Router router = Router.router(vertx);

        // Set up security handlers
        setupSecurityHandlers(router);
        router.route().handler(BodyHandler.create());
        router
            .post("/insert-signalment")
            .handler(ctx -> {
                Single.just(ctx.body().asJsonObject())
                    .map(SignalmentData::fromJson)
                    .flatMap(data ->
                        vertx
                            .eventBus()
                            .<JsonObject>rxRequest(
                                INSERT_SIGNALMENT_ADDRESS,
                                data.toJson()
                            )
                    )
                    .map(Message::body)
                    .map(ResponseData::fromJson)
                    .flatMap(response -> sendResponse(ctx, 200, response))
                    .onErrorResumeNext(error -> handleError(ctx, error))
                    .subscribe(
                        responseData ->
                            logger.info(
                                "Response sent successfully: {}",
                                responseData
                            ),
                        throwable ->
                            logger.error(
                                "Error occurred while sending response",
                                throwable
                            )
                    );
            });

        return vertx
            .createHttpServer()
            .requestHandler(router)
            .rxListen(8082)
            .ignoreElement()
            .doOnComplete(() -> logger.info("HTTP server started on port 8082"))
            .doOnError(error ->
                logger.error("Failed to start HTTP server", error)
            );
    }

    private void setupSecurityHandlers(Router router) {
        // CORS handler configuration
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        //allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);

        router
            .route()
            .handler(
                CorsHandler.create()
                    .addRelativeOrigin("http://localhost:8000")
                    .allowedHeaders(allowedHeaders)
                    .allowedMethods(allowedMethods)
            );

        // CSP handler configuration
        router
            .route()
            .handler(
                CSPHandler.create()
                    .addDirective("default-src", "'self'")
                    .addDirective("script-src", "'self'")
                    .addDirective("style-src", "'self'")
                    .addDirective("img-src", "'self'")
                    .addDirective("connect-src", "'self' http://localhost:8082")
            );
    }

    private Single<ResponseData> sendResponse(
        RoutingContext ctx,
        int statusCode,
        ResponseData responseData
    ) {
        return Single.fromCallable(() -> {
            ctx
                .response()
                .setStatusCode(statusCode)
                .putHeader("Content-Type", "application/json")
                .end(responseData.toJson().encode());
            return responseData;
        });
    }

    private Single<ResponseData> handleError(
        RoutingContext ctx,
        Throwable error
    ) {
        return Single.fromCallable(() -> {
            ResponseData responseData;
            int statusCode;

            if (error instanceof IllegalArgumentException) {
                logger.warn("Client error: {}", error.getMessage());
                responseData = new ResponseData(
                    "Invalid input: " + error.getMessage()
                );
                statusCode = 400;
            } else {
                logger.error("Unexpected error", error);
                responseData = new ResponseData("Internal server error");
                statusCode = 500;
            }

            ctx
                .response()
                .setStatusCode(statusCode)
                .putHeader("Content-Type", "application/json")
                .end(responseData.toJson().encode());

            return responseData;
        });
    }

    private static final Logger logger = LoggerFactory.getLogger(
        HttpVerticle.class
    );
}
