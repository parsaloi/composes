package co.example.model;

import io.vertx.core.json.JsonObject;
import java.util.Objects;

public record ResponseData(String message) {
  public ResponseData {
    Objects.requireNonNull(message, "message must no be null");
  }

  public JsonObject toJson() {
    return new JsonObject()
      .put("message", message);
  }

  public static ResponseData fromJson(JsonObject json) {
    Objects.requireNonNull(json, "json must not be null");
    return new ResponseData(json.getString("message"));
  }
}
