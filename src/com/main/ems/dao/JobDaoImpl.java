package main.ems.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.support.WebApplicationContextUtils;

import main.ems.model.Job;
import main.ems.model.User;

@Repository
public class JobDaoImpl extends HttpServlet implements JobDao {

    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private DataSource getDataSource() {
        ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        DataSource dataSource = (DataSource) appContext.getBean("dataSourceCV");
        return dataSource;
    }

    @Override
    public List<Job> getJobs() {
        String hql = "FROM Job";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public String addJob(Job job) {
        try {
            sessionFactory.getCurrentSession().save(job);

            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "Carucu12boi");
            PreparedStatement ps = con.prepareStatement("insert into cv_jobs (job_name,description)values (?,?)");
            ps.setString(1, job.getJobName());
            ps.setString(2, job.getJobDescription());
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error";
        }
        return null;
    }

    @Override
    public String editJob(Job job) {
        try {
            sessionFactory.getCurrentSession().update(job);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error";
        }
        return null;
    }

    @Override
    public List<Job> getJobByName(String jobName) {
        String hql = "FROM Job where jobName = :name";
        List<Job> jobs;
        try {
            return sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", jobName).list();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getEmployeesByJob(int jobId) {
        String hql = "FROM User where job.jobId = :id";
        try {
            return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", jobId).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String deleteJob(Job job) {
        try {
            sessionFactory.getCurrentSession().delete(job);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error";
        }

        return null;
    }

    @Override
    public Boolean checkHasEmployees(Job job) {
        List<User> users = new ArrayList<User>();
        String hql = "From User where job.jobId = :param";
        try {
            users = sessionFactory.getCurrentSession().createQuery(hql).setParameter("param", job.getJobId()).list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (users.size() > 0)
            return true;
        else return false;
    }
}
