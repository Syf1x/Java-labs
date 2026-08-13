package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.JobInfo;
import com.office.employeemanagement.dto.JobStatus;
import com.office.employeemanagement.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JobRegistry {

    private final Map<String, JobInfo> jobs = new ConcurrentHashMap<>();

    public String createJob() {
        String id = UUID.randomUUID().toString();
        jobs.put(id, new JobInfo(id, JobStatus.PENDING, null, null));
        return id;
    }

    public void markRunning(String id) {
        jobs.computeIfPresent(id, (key, previous) -> new JobInfo(id, JobStatus.RUNNING, null, null));
    }

    public void markDone(String id, int createdCount) {
        jobs.computeIfPresent(id, (key, previous) -> new JobInfo(id, JobStatus.DONE, createdCount, null));
    }

    public void markFailed(String id, String error) {
        jobs.computeIfPresent(id, (key, previous) -> new JobInfo(id, JobStatus.FAILED, null, error));
    }

    public JobInfo get(String id) {
        JobInfo info = jobs.get(id);
        if (info == null) {
            throw new ResourceNotFoundException("Job not found: " + id);
        }
        return info;
    }
}
