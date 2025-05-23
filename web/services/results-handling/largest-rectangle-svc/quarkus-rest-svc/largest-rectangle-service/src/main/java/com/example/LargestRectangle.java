package com.example;

import java.util.*;
import java.util.stream.*;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LargestRectangle {
    public record Result(int area, String errorMessage) {}

    private static int largestRectangleArea(int[] heights) {
        var stack = new ArrayDeque<Integer>();
        int[] maxArea = {0};
        int[] h = Arrays.copyOf(heights, heights.length + 1);
        IntStream.range(0, h.length).forEach(i -> {
            while (!stack.isEmpty() && h[stack.peek()] > h[i]) {
                int height = h[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea[0] = Math.max(maxArea[0], height * width);
            }
            stack.push(i);
        });
        return maxArea[0];
    }

    private static int maximalRectangle(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) return 0;
        int[] heights = new int[matrix[0].length];
        return Arrays.stream(matrix).mapToInt(row -> {
            IntStream.range(0, row.length)
                     .forEach(j -> heights[j] = (row[j] == 1) ? heights[j] + 1 : 0);
            return largestRectangleArea(heights);
        }).max().orElse(0);
    }

    public static Result safeMaximalRectangle(int[][] matrix) {
        try {
            if (matrix == null) {
                throw new IllegalArgumentException("Matrix is null");
            }
            if (matrix.length == 0 || (matrix.length > 0 && matrix[0].length == 0)) {
                return new Result(0, null); // Valid empty matrix, return area 0
            }
            int rowLength = matrix[0].length;
            if (Arrays.stream(matrix).anyMatch(row -> row == null || row.length != rowLength)) {
                throw new IllegalArgumentException("Matrix is not rectangular or contains null rows");
            }
            if (Arrays.stream(matrix)
                      .flatMapToInt(Arrays::stream)
                      .anyMatch(val -> val != 0 && val != 1)) {
                throw new IllegalArgumentException("Matrix should contain only 0's and 1's");
            }
            int area = maximalRectangle(matrix);
            return new Result(area, null);
        } catch (Exception e) {
            return new Result(-1, "Error: " + e.getMessage());
        }
    }
}
