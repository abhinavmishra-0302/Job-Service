package com.example.job_service.service;

import com.example.job_service.models.Job;
import com.example.job_service.repository.JobRepository;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange exchange;

    public static final String JOB_TITLE_REQUEST_QUEUE = "jobTitleRequestQueue";

    public static final String JOB_TITLE_RESPONSE_ROUTING_KEY = "jobTitleResponse";

    public Job createJob(Job job) {
        System.out.println(job.getTitle());
        job.setPostedDate(new Date());
        return jobRepository.save(job);
    }

    public Job getJob(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @RabbitListener(queues = JOB_TITLE_REQUEST_QUEUE)
    public void handleJobTitleRequest(Long jobId) {
        // Fetch the job title from the database
        String jobTitle = jobRepository.findTitleById(jobId);

        System.out.println("Job Service: "+jobId);
        System.out.println("Job Service: "+jobTitle);

        // Send the response back to user-profile-service
        rabbitTemplate.convertAndSend(exchange.getName(), JOB_TITLE_RESPONSE_ROUTING_KEY, jobTitle);
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
        job.setRequirements(jobDetails.getRequirements());
        job.setResponsibilities(jobDetails.getResponsibilities());
        job.setEmploymentType(jobDetails.getEmploymentType());
        job.setSalary(jobDetails.getSalary());
        job.setPostedBy(jobDetails.getPostedBy());
        job.setSkills(jobDetails.getSkills());

        return jobRepository.save(job);
    }

    public Job deleteJob(Long id){
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        jobRepository.delete(job);
        return job;
    }
}
