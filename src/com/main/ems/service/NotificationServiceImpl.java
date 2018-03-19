package main.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.NotificationDao;
import main.ems.model.Notification;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationDao notificationDao;

    public NotificationDao getNotificationDao() {
        return notificationDao;
    }

    public void setNotificationDao(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    @Transactional
    public void insertNotification(Notification notification) {
        notificationDao.insertNotification(notification);
    }

    @Override
    @Transactional
    public List<Notification> getNotifications(int empId) {
        return notificationDao.getNotifications(empId);
    }

    @Override
    @Transactional
    public void updateNotification(int empId, String link) {
        notificationDao.updateNotification(empId, link);
    }
}