package com.devrezaur.service;

import com.devrezaur.model.Batch;
import com.devrezaur.model.User;
import com.devrezaur.payload.response.BatchResponse;
import com.devrezaur.payload.response.UserSummaryResponse;
import com.devrezaur.repository.BatchRepository;
import com.devrezaur.utility.UserInfoResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoResponseBuilder userInfoResponseBuilder;

    public List<BatchResponse> getAllBatches() {
        List<Batch> batchList = batchRepository.findAll();
        List<BatchResponse> batchResponseList = new ArrayList<>();

        for (Batch batch : batchList) {
            batchResponseList.add(new BatchResponse(batch.getBatchId(), batch.getBatchName(), batch.getDescription(), batch.getImageUrl()));
        }

        return batchResponseList;
    }

    public BatchResponse getBatchById(int batchId) {
        Batch batch = batchRepository.findBatchByBatchId(batchId);
        if (batch == null) return null;
        return new BatchResponse(batch.getBatchId(), batch.getBatchName(), batch.getDescription(), batch.getImageUrl());
    }

    public BatchResponse addBatch(Batch batch) throws Exception {
        try {
            batch = batchRepository.save(batch);
            return new BatchResponse(batch.getBatchId(), batch.getBatchName(), batch.getDescription(), batch.getImageUrl());
        } catch (Exception ex) {
            throw new Exception("Can not add batch");
        }
    }

    public BatchResponse editBatch(Batch batch) throws Exception {
        try {
            Batch existingBatch = batchRepository.findBatchByBatchId(batch.getBatchId());
            existingBatch.setBatchName(batch.getBatchName());
            existingBatch.setDescription(batch.getDescription());
            existingBatch.setImageUrl(batch.getImageUrl());

            batch = batchRepository.save(existingBatch);
            return new BatchResponse(batch.getBatchId(), batch.getBatchName(), batch.getDescription(), batch.getImageUrl());
        } catch (Exception ex) {
            throw new Exception("Can not update batch");
        }
    }

    @Transactional
    public Boolean enrollUser(int batchId, String userId) throws Exception {
        User user = userService.findUserByUserId(userId);

        if (user != null) {
            Batch batch = batchRepository.findBatchByBatchId(batchId);
            if (batch.getEnrolledUsers().contains(user))
                throw new Exception("User already enrolled");

            batch.getEnrolledUsers().add(user);
            return true;
        }
        throw new Exception("Can not enroll user");
    }

    @Transactional
    public Boolean unEnrollUser(int batchId, String userId) throws Exception {
        User user = userService.findUserByUserId(userId);

        if (user != null) {
            Batch batch = batchRepository.findBatchByBatchId(batchId);
            if (!batch.getEnrolledUsers().contains(user))
                throw new Exception("User already un-enrolled");

            batch.getEnrolledUsers().remove(user);
            return true;
        }
        throw new Exception("Can not unEnroll user");
    }

    @Transactional
    public List<UserSummaryResponse> getEnrolledUserList(int batchId) {
        Batch batch = batchRepository.findBatchByBatchId(batchId);
        return userInfoResponseBuilder.buildAllUserSummary(batch.getEnrolledUsers());
    }

    public List<BatchResponse> getEnrolledBatches(String userId) {
        List<Batch> batchList = batchRepository.findByEnrolledUsers_userId(userId);
        List<BatchResponse> batchResponseList = new ArrayList<>();

        for (Batch batch : batchList) {
            batchResponseList.add(new BatchResponse(batch.getBatchId(), batch.getBatchName(), batch.getDescription(), batch.getImageUrl()));
        }

        return batchResponseList;
    }
}
