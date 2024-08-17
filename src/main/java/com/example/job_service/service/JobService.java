package com.example.job_service.service;

import com.example.job_service.models.Job;
import com.example.job_service.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Job createJob(Job job) {
        System.out.println(job.getTitle());
        job.setPostedDate(new Date());
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs(){
        return jobRepository.findAll();
    }

    public Job updateJob(Long id, Job jobDetails){
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setCompany(jobDetails.getCompany());
        job.setLocation(jobDetails.getLocation());
        job.setIndustry(jobDetails.getIndustry());
        job.setPostedDate(jobDetails.getPostedDate());
        job.setExpiryDate(jobDetails.getExpiryDate());

        return jobRepository.save(job);
    }

    public Job deleteJob(Long id){
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        jobRepository.delete(job);
        return job;
    }
}
