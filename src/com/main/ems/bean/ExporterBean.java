package main.ems.bean;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;

@ManagedBean(name = "exporterBean")
@ViewScoped
public class ExporterBean {

    public UserBean getUserBean() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        UserBean userBean = (UserBean) fctx.getApplication().evaluateExpressionGet(fctx, "#{userBean}", UserBean.class);
        System.out.println(userBean.getUser());
        return userBean;
    }

    public void preProcessPDFEmployeesList(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "images" + File.separator + "pdf-img.png";
        Image.getInstance(logo).setWidthPercentage(10);

        pdf.add(Image.getInstance(logo));

        Font gray = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC, new Color(0x80, 0x80, 0x80));
        Font black = FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD, new Color(0x00, 0x00, 0x00));
        Paragraph p;
        p = new Paragraph();
        p.add(new Chunk("                  " + "Employees list", black));
        pdf.add(p);
        p = new Paragraph();
        p.add(new Chunk("               " + "Author:" + getUserBean().getUser().toString(), gray));

        pdf.add(p);
        pdf.add(new Paragraph(" "));


    }
}
