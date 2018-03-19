package main.ems.dao;

import java.util.List;

import main.ems.model.Job;
import main.ems.model.User;

public interface JobDao {
    public List<Job> getJobs();

    public String addJob(Job job);

    public String editJob(Job job);

    public List<Job> getJobByName(String jobName);

    public List<User> getEmployeesByJob(int jobId);

    public String deleteJob(Job job);

    public Boolean checkHasEmployees(Job job);
} 
