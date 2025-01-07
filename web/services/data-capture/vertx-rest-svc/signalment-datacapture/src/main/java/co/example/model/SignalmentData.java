package co.example.model;

import io.vertx.core.json.JsonObject;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.time.Period;
import java.util.UUID;

public record SignalmentData(
    UUID microchipId,
    LocalDateTime dateOfBirth,
    String species,
    String breed,
    String gender,
    String reproductiveStatus,
    String color
) {
    public SignalmentData {
        Objects.requireNonNull(microchipId, "microchipId must not be null");

        Objects.requireNonNull(dateOfBirth, "dateOfBirth must not be null");
        if (dateOfBirth.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                "Date of birth cannot be in the future"
            );
        }
        Period age = Period.between(
            dateOfBirth.toLocalDate(),
            LocalDateTime.now().toLocalDate()
        );
        if (age.getYears() > 30) {
            throw new IllegalArgumentException(
                "Animal age cannot exceed 30 years"
            );
        }

        Objects.requireNonNull(species, "species must not be null");
        if (species.trim().isEmpty() || species.length() > 50) {
            throw new IllegalArgumentException(
                "Species must not be empty and cannot exceed 50 characters"
            );
        }

        // Validate breed
        Objects.requireNonNull(breed, "breed must not be null");
        if (breed.trim().isEmpty() || breed.length() > 50) {
            throw new IllegalArgumentException(
                "Breed must not be empty and cannot exceed 50 characters"
            );
        }

        Objects.requireNonNull(gender, "gender must not be null");
        if (gender.trim().isEmpty() || gender.length() > 50) {
            throw new IllegalArgumentException(
                "Gender must not be empty and cannot exceed 50 characters"
            );
        }

        Objects.requireNonNull(
            reproductiveStatus,
            "reproductiveStatus must not be null"
        );
        if (
            reproductiveStatus.trim().isEmpty() ||
            reproductiveStatus.length() > 50
        ) {
            throw new IllegalArgumentException(
                "Reproductive status must not be empty and cannot exceed 50 characters"
            );
        }

        Objects.requireNonNull(color, "color must not be null");
        if (color.trim().isEmpty() || color.length() > 50) {
            throw new IllegalArgumentException(
                "Color must not be empty and cannot exceed 50 characters"
            );
        }
    }

    public JsonObject toJson() {
        return new JsonObject()
            .put("microchipId", microchipId.toString())
            .put(
                "dateOfBirth",
                new JsonObject()
                    .put("year", dateOfBirth.getYear())
                    .put("month", dateOfBirth.getMonthValue())
                    .put("dayOfMonth", dateOfBirth.getDayOfMonth())
                    .put("hour", dateOfBirth.getHour())
                    .put("minute", dateOfBirth.getMinute())
                    .put("second", dateOfBirth.getSecond())
            )
            .put("species", species)
            .put("breed", breed)
            .put("gender", gender)
            .put("reproductiveStatus", reproductiveStatus)
            .put("color", color);
    }

    public static SignalmentData fromJson(JsonObject json) {
        Objects.requireNonNull(json, "json must not be null");

        JsonObject dateOfBirthJson = json.getJsonObject("dateOfBirth");
        if (dateOfBirthJson == null) {
            throw new IllegalArgumentException("dateOfBirth object is missing");
        }

        LocalDateTime dateOfBirth = LocalDateTime.of(
            dateOfBirthJson.getInteger("year"),
            dateOfBirthJson.getInteger("month"),
            dateOfBirthJson.getInteger("dayOfMonth"),
            dateOfBirthJson.getInteger("hour", 0),
            dateOfBirthJson.getInteger("minute", 0),
            dateOfBirthJson.getInteger("second", 0)
        );

        return new SignalmentData(
            UUID.fromString(json.getString("microchipId")),
            dateOfBirth,
            json.getString("species"),
            json.getString("breed"),
            json.getString("gender"),
            json.getString("reproductiveStatus"),
            json.getString("color")
        );
    }
}
