package com.example.job_service.repository;

import com.example.job_service.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>{
    List<Job> findByTitleContainingAndLocationContaining(String title, String location);
}
