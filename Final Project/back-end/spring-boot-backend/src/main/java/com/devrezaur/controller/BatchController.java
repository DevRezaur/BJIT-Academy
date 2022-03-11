package com.devrezaur.controller;

import com.devrezaur.model.Batch;
import com.devrezaur.payload.request.EnrollUserRequest;
import com.devrezaur.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/batch")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/all")
    public ResponseEntity<?> getAllBatches() {
        return ResponseEntity.status(200).body(batchService.getAllBatches());
    }

    @Secured("ROLE_USER")
    @PostMapping("/batches")
    public ResponseEntity<?> getEnrolledBatches(@RequestBody Map<String, String> userid) {
        return ResponseEntity.status(200).body(batchService.getEnrolledBatches(userid.get("userid")));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/{batchId}")
    public ResponseEntity<?> getBatchById(@PathVariable int batchId) {
        return ResponseEntity.status(200).body(batchService.getBatchById(batchId));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    public ResponseEntity<?> addNewBatch(@RequestBody Batch batch) {
        try {
            return ResponseEntity.status(201).body(batchService.addBatch(batch));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/update")
    public ResponseEntity<?> updateBatch(@RequestBody Batch batch) {
        try {
            return ResponseEntity.status(201).body(batchService.editBatch(batch));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/{batchId}/enrolledUsers")
    public ResponseEntity<?> getEnrolledUserList(@PathVariable int batchId) {
        return ResponseEntity.status(200).body(batchService.getEnrolledUserList(batchId));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollUser(@RequestBody EnrollUserRequest enrollUserRequest) {
        try {
            boolean isEnrolledSuccessfully = batchService.enrollUser(enrollUserRequest.getBatchId(), enrollUserRequest.getUserId());
            if (isEnrolledSuccessfully)
                return ResponseEntity.status(201).body("Successfully Enrolled User");
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }

        return ResponseEntity.status(400).body("Failed to Enrolled User");
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/un-enroll")
    public ResponseEntity<?> unEnrollUser(@RequestBody EnrollUserRequest enrollUserRequest) {
        try {
            boolean isUnEnrolledSuccessfully = batchService.unEnrollUser(enrollUserRequest.getBatchId(), enrollUserRequest.getUserId());
            if (isUnEnrolledSuccessfully)
                return ResponseEntity.status(201).body("Successfully Un-Enrolled User");
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }

        return ResponseEntity.status(400).body("Failed to Un-Enrolled User");
    }

}
