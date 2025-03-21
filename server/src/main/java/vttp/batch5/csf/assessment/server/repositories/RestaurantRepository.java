package vttp.batch5.csf.assessment.server.repositories;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.models.PaymentReceipt;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //select username, password from customers where username="fred";
    // public final String Q_GET_PASSWORD = """
    //         SELECT password FROM customers WHERE username=?
    //         """;
    
    //select count(*) from customers where username="fred" and password=sha2("fred",224);
    public final String Q_CHECK_PASSWORD="""
            SELECT count(*) FROM customers WHERE username=? AND password=sha2(?, 224)
            """;

    public final String Q_INSERT_PLACE_ORDERS = """
            INSERT INTO place_orders(order_id, payment_id, order_date, total, username) VALUES (?, ?, ?, ?, ?)
            """;

    // public String getPassword(String username){
    //     final SqlRowSet rs = jdbcTemplate.queryForRowSet(Q_GET_PASSWORD, username);
    //     rs.next();
    //     return rs.getString("password");
    // }

    public boolean checkPassword(String username, String password){
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(Q_CHECK_PASSWORD, username, password);
        rs.next();
        return rs.getInt("count(*)") == 1;
    }

    public void insertDetails(JsonObject order, PaymentReceipt receipt, Double totalPrice){
        jdbcTemplate.update(
            Q_INSERT_PLACE_ORDERS, receipt.getOrder_id(), receipt.getPayment_id(), new Date(receipt.getTimestamp()), totalPrice, order.getString("username")
        );
    }


}
