package main.ems.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DisplayImageFromDatabase extends HttpServlet implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
                    "system", "Carucu12boi");
            return con;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = getConnection();
        InputStream image = null;
        try {
            String id = request.getParameter("Image_id");

            stmt = con.createStatement();
            if (id != null) {
                String query = "select avatar_content,avatar_name from users where user_id = " + id;
                byte[] imageContent = null;
                rs = stmt.executeQuery(query);
                int size = 0;
                if (rs.next()) {
                    imageContent = new byte[1048576];
                    size = 0;
                    image = rs.getBinaryStream(1);
                    String name = rs.getString(2);
                    response.reset();
                    if (name != null) {
                        String type = name.substring(name.lastIndexOf(".") + 1);
                        if (type.equals("jpg")) {
                            response.setContentType("image/jpg");
                        } else if (type.equals("png")) {
                            response.setContentType("image/png");
                        }
                    }
                    if (image != null) {
                        while ((size = image.read(imageContent)) != -1) {
                            response.getOutputStream().
                                    write(imageContent, 0, size);
                        }
                    } else {
                        showUnknownPicture(response, imageContent, size, image);
                    }
                } else {
                    showUnknownPicture(response, imageContent, size, image);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void showUnknownPicture(HttpServletResponse response, byte[] imageContent, int size, InputStream image) throws ServletException {
        imageContent = new byte[1048576];
        size = 0;
        File picture = new File("C:/Users/alin_/Desktop/img.gif");
        try {
            image = new FileInputStream(picture);
            response.reset();

            response.setContentType("image/gif");

            if (image != null) {
                while ((size = image.read(imageContent)) != -1) {
                    response.getOutputStream().
                            write(imageContent, 0, size);
                }
            }
            if (image != null) {
                while ((size = image.read(imageContent)) != -1) {
                    response.getOutputStream().
                            write(imageContent, 0, size);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
