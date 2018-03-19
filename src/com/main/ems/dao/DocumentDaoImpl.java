package main.ems.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import main.ems.model.Document;
import main.ems.model.User;

@Repository
public class DocumentDaoImpl implements DocumentDao {

    @Autowired
    SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getEmployees() {
        String hql = "From User";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public void insertDocument(Document document) {
        sessionFactory.getCurrentSession().save(document);
    }

    @Override
    public List<Document> getDocumentsForUser(int userId) {
        String hql = "From Document where user.userId = :id";
        return sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", userId).list();
    }

    @Override
    public void deleteDocument(Document document) {
        sessionFactory.getCurrentSession().delete(document);
    }
}
