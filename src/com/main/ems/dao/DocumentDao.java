package main.ems.dao;

import java.util.List;

import main.ems.model.Document;
import main.ems.model.User;

public interface DocumentDao {
    public List<User> getEmployees();

    public void insertDocument(Document document);

    public List<Document> getDocumentsForUser(int userId);

    public void deleteDocument(Document document);
} 
