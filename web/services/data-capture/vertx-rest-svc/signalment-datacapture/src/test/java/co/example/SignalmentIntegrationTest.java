package co.example.signalment_datacapture;

import co.example.signalment_datacapture.model.SignalmentData;
import co.example.signalment_datacapture.verticle.DatabaseVerticle;
import co.example.signalment_datacapture.verticle.HttpVerticle;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.client.predicate.ResponsePredicate;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.sqlclient.Pool;
import io.vertx.rxjava3.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import net.jqwik.api.Property;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Provide;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
class SignalmentIntegrationTest {
    private Vertx vertx;
    private WebClient webClient;
    private Pool pgPool;

    @BeforeEach
    void setUp(VertxTestContext testContext) {
        vertx = Vertx.vertx();
        webClient = WebClient.create(vertx);

        // Setup test database connection
        PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(5432)
            .setHost("localhost")
            .setDatabase("patientshealth_test")
            .setUser("test_user")
            .setPassword("test_password");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        pgPool = PgBuilder.pool()
            .with(poolOptions)
            .connectingTo(connectOptions)
            .using(vertx)
            .build();

        // Deploy verticles
        vertx.rxDeployVerticle(new DatabaseVerticle())
            .flatMap(id -> vertx.rxDeployVerticle(new HttpVerticle()))
            .subscribe(
                success -> testContext.completeNow(),
                testContext::failNow
            );
    }

    @AfterEach
    void tearDown(VertxTestContext testContext) {
        pgPool.close()
            .andThen(vertx.close())
            .subscribe(
                () -> testContext.completeNow(),
                testContext::failNow
            );
    }

    @Test
    void testValidSignalmentInsertion(VertxTestContext testContext) {
        SignalmentData validData = new SignalmentData(
            UUID.randomUUID(),
            LocalDateTime.now().minusYears(2),
            "Canine",
            "Labrador",
            "Male",
            "Neutered",
            "Black"
        );

        webClient.post(8082, "localhost", "/insert-signalment")
            .putHeader("Origin", "http://localhost:8000")
            .putHeader("Content-Type", "application/json")
            .sendJson(validData.toJson())
            .subscribe(
                response -> {
                    assertEquals(200, response.statusCode());
                    JsonObject body = response.bodyAsJsonObject();
                    assertEquals("Signalment data saved successfully", body.getString("message"));
                    testContext.completeNow();
                },
                testContext::failNow
            );
    }

    @Test
    void testCORSHeaders(VertxTestContext testContext) {
        webClient.post(8082, "localhost", "/insert-signalment")
            .putHeader("Origin", "http://localhost:8000")
            .putHeader("Content-Type", "application/json")
            .sendJson(new JsonObject())
            .subscribe(
                response -> {
                    assertEquals("http://localhost:8000", 
                        response.getHeader("Access-Control-Allow-Origin"));
                    testContext.completeNow();
                },
                testContext::failNow
            );
    }

    @Property(tries = 100)
    void propertyTestSignalmentValidation(
            @ForAll("validMicrochipIds") UUID microchipId,
            @ForAll("validDatesOfBirth") LocalDateTime dateOfBirth,
            @ForAll("validSpecies") String species,
            @ForAll("validBreeds") String breed,
            @ForAll("validGenders") String gender,
            @ForAll("validReproductiveStatuses") String reproductiveStatus,
            @ForAll("validColors") String color,
            VertxTestContext testContext) {
        
        SignalmentData testData = new SignalmentData(
            microchipId, dateOfBirth, species, breed, 
            gender, reproductiveStatus, color
        );

        webClient.post(8082, "localhost", "/insert-signalment")
            .putHeader("Origin", "http://localhost:8000")
            .putHeader("Content-Type", "application/json")
            .sendJson(testData.toJson())
            .subscribe(
                response -> {
                    assertEquals(200, response.statusCode());
                    testContext.completeNow();
                },
                error -> {
                    fail("Property test failed: " + error.getMessage());
                    testContext.failNow(error);
                }
            );
    }

    @Test
    void testConcurrentInsertions(VertxTestContext testContext) {
        int concurrentRequests = 100;
        List<Single<JsonObject>> requests = new ArrayList<>();

        for (int i = 0; i < concurrentRequests; i++) {
            SignalmentData testData = new SignalmentData(
                UUID.randomUUID(),
                LocalDateTime.now().minusYears(1),
                "Canine",
                "Mixed",
                "Female",
                "Spayed",
                "Brown"
            );

            requests.add(
                webClient.post(8082, "localhost", "/insert-signalment")
                    .putHeader("Origin", "http://localhost:8000")
                    .putHeader("Content-Type", "application/json")
                    .sendJson(testData.toJson())
                    .map(response -> response.bodyAsJsonObject())
            );
        }

        Single.zip(requests, responses -> {
            for (Object response : responses) {
                JsonObject jsonResponse = (JsonObject) response;
                assertEquals("Signalment data saved successfully", 
                    jsonResponse.getString("message"));
            }
            return responses;
        })
        .subscribe(
            success -> testContext.completeNow(),
            testContext::failNow
        );
    }

    // Jqwik Arbitrary providers
    @Provide
    Arbitrary<UUID> validMicrochipIds() {
        return Arbitraries.create(UUID::randomUUID);
    }

    @Provide
    Arbitrary<LocalDateTime> validDatesOfBirth() {
        return Arbitraries.dates()
            .atTheEarliest(LocalDateTime.now().minusYears(30))
            .atTheLatest(LocalDateTime.now())
            .map(date -> LocalDateTime.of(
                date.getYear(), 
                date.getMonth(), 
                date.getDayOfMonth(), 
                0, 0));
    }

    @Provide
    Arbitrary<String> validSpecies() {
        return Arbitraries.strings()
            .withCharRange('a', 'z')
            .ofMinLength(2)
            .ofMaxLength(50);
    }

    @Provide
    Arbitrary<String> validBreeds() {
        return Arbitraries.strings()
            .withCharRange('a', 'z')
            .ofMinLength(2)
            .ofMaxLength(50);
    }

    @Provide
    Arbitrary<String> validGenders() {
        return Arbitraries.strings()
            .withCharRange('a', 'z')
            .ofMinLength(2)
            .ofMaxLength(50);
    }

    @Provide
    Arbitrary<String> validReproductiveStatuses() {
        return Arbitraries.strings()
            .withCharRange('a', 'z')
            .ofMinLength(2)
            .ofMaxLength(50);
    }

    @Provide
    Arbitrary<String> validColors() {
        return Arbitraries.strings()
            .withCharRange('a', 'z')
            .ofMinLength(2)
            .ofMaxLength(50);
    }

    // ... similar providers for other fields ...
}
