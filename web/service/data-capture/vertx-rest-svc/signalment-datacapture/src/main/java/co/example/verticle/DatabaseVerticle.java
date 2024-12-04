package co.example.verticle;

import co.example.model.ResponseData;
import co.example.model.SignalmentData;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.pgclient.PgBuilder;
import io.vertx.rxjava3.sqlclient.Pool;
import io.vertx.rxjava3.sqlclient.Tuple;
import io.vertx.sqlclient.DatabaseException;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseVerticle extends AbstractVerticle {

    private static final String INSERT_SIGNALMENT_ADDRESS =
        System.getenv()
            .getOrDefault("INSERT_SIGNALMENT_ADDRESS", "signalment.insert");

    private Pool pgPool;

    @Override
    public Completable rxStart() {
        return setupDatabase().andThen(setupEventBusConsumer());
    }

    private Completable setupDatabase() {
        PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(5433)
            .setHost("127.0.0.3")
            .setDatabase("patientshealth")
            .setUser("postgres")
            .setPassword("gresing");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        pgPool = PgBuilder.pool()
            .with(poolOptions)
            .connectingTo(connectOptions)
            .using(vertx)
            .build();

        return Completable.complete();
    }

    private Completable setupEventBusConsumer() {
        vertx
            .eventBus()
            .<JsonObject>consumer(INSERT_SIGNALMENT_ADDRESS)
            .handler(message -> {
                Single.just(message.body())
                    .map(SignalmentData::fromJson)
                    .flatMap(this::insertSignalmentData)
                    .subscribe(
                        response -> message.reply(response.toJson()),
                        error -> {
                            logger.error("Error processing message", error);
                            message.fail(500, error.getMessage());
                        }
                    );
            });

        return Completable.complete();
    }

    private Single<ResponseData> insertSignalmentData(SignalmentData data) {
        String query = new StringBuilder()
            .append("INSERT INTO static_signalment ")
            .append(
                "(microchipID, DateOfBirth, Species, Breed, Gender, ReproductiveStatus, Color) "
            )
            .append("VALUES ($1, $2, $3, $4, $5, $6, $7)")
            .toString();

        return pgPool
            .preparedQuery(query)
            .rxExecute(
                Tuple.tuple()
                    .addUUID(data.microchipId())
                    .addString(data.dateOfBirth().toString())
                    .addString(data.breed())
                    .addString(data.gender())
                    .addString(data.reproductiveStatus())
                    .addString(data.color())
            )
            .map(result ->
                new ResponseData("Signalment data saved successfully")
            )
            .onErrorResumeNext(error -> {
                logger.error("Database error", error);
                if (error instanceof DatabaseException) {
                    return Single.error(
                        new RuntimeException("Database error occurred")
                    );
                }
                return Single.error(error);
            });
    }

    @Override
    public Completable rxStop() {
        return pgPool.close();
    }

    private static final Logger logger = LoggerFactory.getLogger(
        DatabaseVerticle.class
    );
}
