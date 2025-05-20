package com.example.controller;

import com.example.service.LargestRectangleService;
import com.example.model.ErrorResponse;
import com.example.model.SuccessResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/largest-rectangle")
@CrossOrigin(origins = {"http://localhost:8000", "http://localhost:8001"})
public class LargestRectangleController {
    
    private final LargestRectangleService service;

    @Autowired
    public LargestRectangleController(LargestRectangleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> calculateLargestRectangle(@RequestBody int[][] matrix) {
        var result = service.safeMaximalRectangle(matrix);
        if (result.errorMessage() != null) {
            return ResponseEntity.badRequest()
                               .body(new ErrorResponse(result.errorMessage()));
        }
        return ResponseEntity.ok(new SuccessResponse(result.area()));
    }
}
