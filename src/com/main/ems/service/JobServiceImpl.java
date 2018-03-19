package main.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.JobDao;
import main.ems.model.Job;
import main.ems.model.User;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    JobDao jobDao;

    public JobDao getJobDao() {
        return jobDao;
    }

    public void setJobDao(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    @Override
    @Transactional
    public List<Job> getJobs() {
        return jobDao.getJobs();
    }

    @Override
    @Transactional
    public String addJob(Job job) {
        return jobDao.addJob(job);
    }

    @Override
    @Transactional
    public String editJob(Job job) {
        return jobDao.editJob(job);
    }

    @Override
    @Transactional
    public List<Job> getJobByName(String jobName) {
        return jobDao.getJobByName(jobName);
    }

    @Override
    @Transactional
    public List<User> getEmployeesByJob(int jobId) {
        return jobDao.getEmployeesByJob(jobId);
    }

    @Override
    @Transactional
    public String deleteJob(Job job) {
        return jobDao.deleteJob(job);
    }

    @Override
    @Transactional
    public Boolean checkHasEmployees(Job job) {
        return jobDao.checkHasEmployees(job);
    }
}
