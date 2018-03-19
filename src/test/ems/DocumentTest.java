package ems;

import main.ems.dao.DocumentDao;
import main.ems.model.Document;
import main.ems.model.User;
import main.ems.service.DocumentService;
import main.ems.service.DocumentServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;

public class DocumentTest extends AbstractTest {

    @Mock
    private DocumentDao documentDao;

    @InjectMocks
    private DocumentService documentService = new DocumentServiceImpl();

    @Before
    public void setUp() {
        this.authenticateUser();
    }

    /**
     *  test pentru vizualizarea propriilor documente
     */

    @Test
    public void getMyDocumentsTest() {
        List<Document> documente = new ArrayList<Document>();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        IntStream.range(0,10).forEach( iter -> {
            Document document = new Document();
            document.setDocumentName("document " + iter);
            document.setDocumentContent(new byte[0]);
            document.setUser(user);
            documente.add(document);
        });

        when(documentDao.getDocumentsForUser(user.getUserId())).thenReturn(documente);

        List<Document> result = documentService.getDocumentsForUser(user.getUserId());

        Assert.assertEquals(result.size(), 10);

        IntStream.range(0,10).forEach( iter -> {
           Assert.assertNotNull(result.get(iter));
           Assert.assertNotNull(result.get(iter).getUser());
           Assert.assertEquals(result.get(iter).getUser().getUserId(), user.getUserId());
        });
    }
}
