package main.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.DocumentDao;
import main.ems.model.Document;
import main.ems.model.User;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    DocumentDao documentDao;

    public DocumentDao getDocumentDao() {
        return documentDao;
    }

    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    @Override
    @Transactional
    public List<User> getEmployees() {
        return documentDao.getEmployees();
    }

    @Override
    @Transactional
    public void insertDocument(Document document) {
        documentDao.insertDocument(document);
    }

    @Override
    @Transactional
    public List<Document> getDocumentsForUser(int userId) {
        return documentDao.getDocumentsForUser(userId);
    }

    @Override
    @Transactional
    public void deleteDocument(Document document) {
        documentDao.deleteDocument(document);
    }
}