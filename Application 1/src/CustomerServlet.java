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

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
    private final List<CustomerDTO> customerList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("customer servlet");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@123");
            ResultSet resultSet = connection.prepareStatement("select * from customer").executeQuery();
            resp.setContentType("application/json");
            //create json array
            JsonArrayBuilder allCustomer = Json.createArrayBuilder();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                System.out.println(id + " " + name + " " + address);
                //create a single json object
                JsonObjectBuilder customer = Json.createObjectBuilder();
                customer.add("id", id);
                customer.add("name", name);
                customer.add("address", address);
                allCustomer.add(customer.build());
                customerList.add(new CustomerDTO(id, name, address));

            }
            resp.getWriter().println(allCustomer.build().toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        System.out.println(id + " " + name + " " + address);
        if (id == null || id.isEmpty() || name == null || name.isEmpty() || address == null || address.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("invalid input");
        } else {
            try {
                CustomerDTO customerDTO = new CustomerDTO(id, name, address);
                customerList.add(customerDTO);
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@123");
                connection.prepareStatement("insert into customer values('" + id + "','" + name + "','" + address + "')").execute();
            } catch (Exception e) {
                throw new RuntimeException(e);

            }
            System.out.println(id + " " + name + " " + address);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        System.out.println(id + " " + name + " " + address);

        if (id == null || id.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("invalid id");
        } else {
            try {
                CustomerDTO customerDTO = getCustomer(id);
                if (customerDTO == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("customer not found");
                } else {
                    customerDTO.setName(name);
                    customerDTO.setAddress(address);
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@123");
                    connection.prepareStatement("update customer set name='" + name + "',address='" + address + "' where id='" + id + "'").execute();
                    resp.getWriter().write("customer updated");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("invalid id");
        } else {
            try {
                CustomerDTO customerDTO = getCustomer(id);
                if (customerDTO == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("customer not found");
                } else {
                    customerList.remove(customerDTO);
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@123");
                    connection.prepareStatement("delete from customer where id='" + id + "'").execute();
                    resp.getWriter().write("customer deleted");
                }
            } catch (Exception e) {
            }
        }
    }

    private CustomerDTO getCustomer(String id) {
        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getId().equals(id)) {
                return customerDTO;
            }
        }
        return null;
    }

}