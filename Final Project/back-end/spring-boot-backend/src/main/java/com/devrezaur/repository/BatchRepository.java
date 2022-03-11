package com.devrezaur.repository;

import com.devrezaur.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {

    Batch findBatchByBatchId(int batchId);

    List<Batch> findByEnrolledUsers_userId(String userid);

}
