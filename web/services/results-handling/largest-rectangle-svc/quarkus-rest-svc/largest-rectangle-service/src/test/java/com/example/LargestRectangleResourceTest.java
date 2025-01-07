package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class LargestRectangleResourceTest {

    @Test
    public void testValidMatrix() {
        int[][] validMatrix = {
            {1, 0, 1, 0},
            {1, 0, 1, 1},
            {1, 1, 1, 1},
            {1, 0, 0, 1}
        };

        given()
            .contentType("application/json")
            .body(validMatrix)
        .when()
            .post("/largest-rectangle")
        .then()
            .statusCode(200)
            .body("largestRectangleArea", is(4));
    }

    @Test
    public void testInvalidMatrix() {
        int[][] invalidMatrix = {
            {1, 0, 2},
            {1, 1, 1}
        };

        given()
            .contentType("application/json")
            .body(invalidMatrix)
        .when()
            .post("/largest-rectangle")
        .then()
            .statusCode(400)
            .body("error", is("Error: Matrix should contain only 0's and 1's"));
    }
}
