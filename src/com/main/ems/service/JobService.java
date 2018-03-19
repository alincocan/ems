package main.ems.service;

import java.util.List;

import main.ems.model.Job;
import main.ems.model.User;

public interface JobService {
    public List<Job> getJobs();

    public String addJob(Job job);

    public String editJob(Job job);

    public List<Job> getJobByName(String jobName);

    public List<User> getEmployeesByJob(int jobId);

    public String deleteJob(Job job);

    public Boolean checkHasEmployees(Job job);
}	
