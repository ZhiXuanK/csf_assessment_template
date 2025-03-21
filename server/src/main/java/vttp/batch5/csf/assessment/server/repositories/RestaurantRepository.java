package vttp.batch5.csf.assessment.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //select username, password from customers where username="fred";
    public final String Q_GET_PASSWORD = """
            SELECT password FROM customers WHERE username=?
            """;

    public String getPassword(String username){
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(Q_GET_PASSWORD, username);
        rs.next();
        return rs.getString("password");
    }


}
