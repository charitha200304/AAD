import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ietf.jgss.Oid;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/placeOrder")
public class placeOrderServlet extends HttpServlet {

    private final List<placeOrderDTO> orderList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Request is received Place Order");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@123");
            ResultSet resultSet = connection.prepareStatement("select * from orders").executeQuery();
            resp.setContentType("application/json");
            //create json array
            JsonArrayBuilder allOrders = Json.createArrayBuilder();
            while (resultSet.next()) {
                String orderId = resultSet.getString("orderId");
                String customerId = resultSet.getString("customerId");
                String date = resultSet.getString("orderDate");
                String itemCode = resultSet.getString("itemCode");
                int qty = resultSet.getInt("qty");
                double unitPrice = resultSet.getDouble("unitPrice");
                System.out.println(orderId + " " + customerId + " " + date + " " + itemCode + " " + qty + " " + unitPrice);
                //create a single json object
                JsonObjectBuilder order = Json.createObjectBuilder();
                order.add("orderId", orderId);
                order.add("customerId", customerId);
                order.add("orderDate", date);
                order.add("itemCode", itemCode);
                order.add("qty", qty);
                order.add("unitPrice", unitPrice);
                allOrders.add(order.build());
                orderList.add(new placeOrderDTO(orderId, customerId, itemCode, qty, unitPrice, date));

            }
            resp.getWriter().println(allOrders.build().toString());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
