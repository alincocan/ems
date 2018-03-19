package main.ems.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Notification;

@Repository
public class NotificationDaoImpl implements NotificationDao {

    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void insertNotification(Notification notification) {
        sessionFactory.getCurrentSession().save(notification);
    }

    @Override
    public List<Notification> getNotifications(int empId) {
        String hql = "From Notification where toEmp.userId = :id and seen = :seen order by notifDate desc";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", empId).setParameter("seen", "N").list();
    }

    @Override
    public void updateNotification(int empId, String link) {
        String hql = "From Notification where toEmp .userId= :id and link = :link and seen = :seen";
        List<Notification> getNotification = sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", empId).setParameter("link", link).setParameter("seen", "N").list();

        for (Notification notif : getNotification) {
            notif.setSeen("Y");
            sessionFactory.getCurrentSession().update(notif);
        }
    }
}