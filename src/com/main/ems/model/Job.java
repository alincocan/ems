package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {
    int jobId;
    String jobName;
    String jobDescription;

    public Job() {

    }

    public Job(String jobName, String jobDescription) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
    }

    @Id
    @GenericGenerator(
            name = "sequence",
            strategy = "sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence",
                            value = "sequence"
                    )

            })
    @GeneratedValue(generator = "sequence")
    @Column(name = "job_id")
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Column(name = "job_name")
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Column(name = "job_description")
    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    @Override
    public String toString() {
        return jobName;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Job) ? jobId == ((Job) other).jobId : (other == this);
    }

    @Override
    public int hashCode() {
        return (this.getClass().hashCode() + jobId);

    }
}
