package com.devrezaur.unit.service;

import com.devrezaur.model.Batch;
import com.devrezaur.payload.response.BatchResponse;
import com.devrezaur.repository.BatchRepository;
import com.devrezaur.service.BatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

class BatchServiceTest {

    @Mock
    private BatchRepository batchRepository;
    @InjectMocks
    private BatchService batchService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Get All Batches For Success Case")
    void getAllBatchesForSuccessCAse() {
        Batch batch = new Batch();
        batch.setBatchId(1);
        batch.setBatchName("Java Batch 01");
        batch.setDescription("This is a demo description of Java Batch 01. This batch started at 1 October 2021, and expects to finish it's training activity at 31 December 2021.");
        batch.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchRepository.findAll()).thenReturn(List.of(batch));

        List<BatchResponse> batchResponseList = batchService.getAllBatches();
        assertNotNull(batchResponseList);
        assertEquals(1, batchResponseList.get(0).getBatchId());
        assertEquals("Java Batch 01", batchResponseList.get(0).getBatchName());
        assertEquals("This is a demo description of Java Batch 01. This batch started at 1 October 2021, and expects to finish it's training activity at 31 December 2021.", batchResponseList.get(0).getDescription());
        assertEquals("https://devrezaur.com/File-Bucket/image/spring.jpg", batchResponseList.get(0).getImageUrl());
    }

    @Test
    @DisplayName("Test Get All Batches For Empty Cases")
    void getAllBatchesForEmptyCase() {
        List<Batch> batchList = new ArrayList<>();
        Mockito.when(batchRepository.findAll()).thenReturn(batchList);

        List<BatchResponse> batchResponseList = batchService.getAllBatches();
        assertTrue(batchResponseList.isEmpty());
    }

    @Test
    @DisplayName("Test Get Batch By Id For Success Case")
    void getBatchByIdForSuccessCase() {
        Batch batch = new Batch();
        batch.setBatchId(1);
        batch.setBatchName("Java Batch 01");
        batch.setDescription("This is a demo description of Java Batch 01. This batch started at 1 October 2021, and expects to finish it's training activity at 31 December 2021.");
        batch.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchRepository.findBatchByBatchId(1)).thenReturn(batch);

        BatchResponse batchResponse = batchService.getBatchById(1);
        assertNotNull(batchResponse);
        assertEquals(1, batchResponse.getBatchId());
        assertEquals("Java Batch 01", batchResponse.getBatchName());
        assertEquals("This is a demo description of Java Batch 01. This batch started at 1 October 2021, and expects to finish it's training activity at 31 December 2021.", batchResponse.getDescription());
        assertEquals("https://devrezaur.com/File-Bucket/image/spring.jpg", batchResponse.getImageUrl());
    }

    @Test
    @DisplayName("Test Get Batch By Id For Null Case")
    void getBatchByIdForNullCase() {
        Mockito.when(batchRepository.findBatchByBatchId(anyInt())).thenReturn(null);

        BatchResponse batchResponse = batchService.getBatchById(1);
        assertNull(batchResponse);
    }

    @Test
    @DisplayName("Test Add Batch Method For Success Case")
    void addBatchForSuccessCase() throws Exception {
        Batch batch = new Batch();
        batch.setBatchName("Java Batch 01");
        batch.setDescription("This is a demo description of Java Batch 01. This batch started at 1 October 2021, and expects to finish it's training activity at 31 December 2021.");
        batch.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchRepository.save(any(Batch.class))).thenReturn(batch);

        BatchResponse batchResponse = batchService.addBatch(batch);
        assertNotNull(batchResponse);
        assertEquals("Java Batch 01", batchResponse.getBatchName());
        assertEquals("This is a demo description of Java Batch 01. This batch started at 1 October 2021, and expects to finish it's training activity at 31 December 2021.", batchResponse.getDescription());
        assertEquals("https://devrezaur.com/File-Bucket/image/spring.jpg", batchResponse.getImageUrl());
    }

    @Test
    @DisplayName("Test Add Batch Method For Failure Case")
    void addBatchForFailureCase() {
        Mockito.when(batchRepository.save(any(Batch.class))).thenThrow(new RuntimeException());

        Batch batch = new Batch();
        batch.setBatchName("Java Batch 01");
        batch.setDescription("This is a demo description of Java Batch 01. This batch started at 1 October 2021, and expects to finish it's training activity at 31 December 2021.");
        batch.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        try {
            BatchResponse batchResponse = batchService.addBatch(batch);
        } catch (Exception ex) {
            assertEquals("Can not add batch", ex.getMessage());
        }
    }
}