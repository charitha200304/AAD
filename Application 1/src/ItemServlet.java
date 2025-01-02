import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/item")
public class ItemServlet extends HttpServlet {
    private final List<ItemDTO> itemList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Item Servlet Works");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@123");
            ResultSet resultSet = connection.prepareStatement("select * from item").executeQuery();
            resp.setContentType("application/json");
            //create json array
            JsonArrayBuilder allItems = Json.createArrayBuilder();
            while (resultSet.next()) {
                String code = resultSet.getString("code");
                String name = resultSet.getString("description");
                String qty = resultSet.getString("qtyOnHand");
                String price = resultSet.getString("unitPrice");
                System.out.println(code + " " + name + " " + qty + " " + price);
                //create a single json object
                JsonObjectBuilder item = Json.createObjectBuilder();
                item.add("code", code);
                item.add("name", name);
                item.add("qty", qty);
                item.add("price", price);
                allItems.add(item.build());
                itemList.add(new ItemDTO(code, name, Integer.parseInt(qty), Double.parseDouble(price)));

            }
            resp.getWriter().println(allItems.build().toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String qty = req.getParameter("qty");
        String price = req.getParameter("price");
        System.out.println(code + " " + name + " " + qty + " " + price);
        if (code == null || code.isEmpty() || name == null || name.isEmpty() || qty == null || qty.isEmpty() || price == null || price.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("invalid input");
        } else {
            try {
                ItemDTO itemDTO = new ItemDTO(code, name, Integer.parseInt(qty), Double.parseDouble(price));
                itemList.add(itemDTO);
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@123");
                connection.prepareStatement("insert into item values('" + code + "','" + name + "'," + qty + "," + price + ")").execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String qty = req.getParameter("qty");
        String price = req.getParameter("price");
        System.out.println(code + " " + name + " " + qty + " " + price);
        if (code == null || code.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("invalid code");
        } else {
            try {
                ItemDTO itemDTO = getItem(code);
                if (itemDTO == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("item not found");
                } else {
                    itemDTO.setName(name);
                    itemDTO.setQty(Integer.parseInt(qty));
                    itemDTO.setPrice(Double.parseDouble(price));
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@123");
                    connection.prepareStatement("update item set description='" + name + "',qtyOnHand=" + qty + ",unitPrice=" + price + " where code='" + code + "'").execute();
                    resp.getWriter().write("item updated");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if (code == null || code.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("invalid code");
        } else {
            try {
                ItemDTO itemDTO = getItem(code);
                if (itemDTO == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("item not found");
                } else {
                    itemList.remove(itemDTO);
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@123");
                    connection.prepareStatement("delete from item where code='" + code + "'").execute();
                    resp.getWriter().write("item deleted");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ItemDTO getItem(String code) {
        for (ItemDTO itemDTO : itemList) {
            if (itemDTO.getCode().equals(code)) {
                return itemDTO;
            }
        }
        return null;
}
}