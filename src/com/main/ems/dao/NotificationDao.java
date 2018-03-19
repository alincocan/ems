package main.ems.dao;

import java.util.List;

import main.ems.model.Notification;

public interface NotificationDao {
    public void insertNotification(Notification notification);

    public List<Notification> getNotifications(int empId);

    public void updateNotification(int empId, String link);
}