package com.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/largest-rectangle")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LargestRectangleResource {

    @POST
    public Response calculateLargestRectangle(int[][] matrix) {
        LargestRectangle.Result result = LargestRectangle.safeMaximalRectangle(matrix);
        if (result.errorMessage() != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorResponse(result.errorMessage()))
                           .build();
        }
        return Response.ok(new SuccessResponse(result.area())).build();
    }

    public static class SuccessResponse {
        public int largestRectangleArea;

        public SuccessResponse(int area) {
            this.largestRectangleArea = area;
        }
    }

    public static class ErrorResponse {
        public String error;

        public ErrorResponse(String message) {
            this.error = message;
        }
    }
}
