package com.example.demo.Repository;

import com.example.demo.Model.Cancel;
import com.example.demo.Model.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CancelRepo {
    @Autowired
    JdbcTemplate template;

    public List<Cancel> fetchAll(){
        String sql = "SELECT * FROM cancel";
        RowMapper<Cancel> rowMapper = new BeanPropertyRowMapper<>(Cancel.class);
        return template.query(sql, rowMapper);
    }

    public Cancel createCancel(int rental_id, Cancel ca, Rental r)
    {
        String sql = "INSERT INTO cancel(cancel_price, cancel_days)\n" +
                "SELECT total_price, from_date FROM rentals\n" +
                "WHERE rental_id = ?;";
        template.update(sql, rental_id);

        String sqlL = "SELECT @cancel_id := last_insert_id()";
        template.execute(sqlL);

        String sql1 = "UPDATE cancel\n" +
                "SET cancel_date = CURDATE()\n" +
                "WHERE cancel_id = @cancel_id";
        template.execute(sql1);

        String sql2 = "UPDATE cancel\n" +
                "SET cancel_days = DATEDIFF(cancel_days,CURDATE())\n" +
                "WHERE cancel_id = @cancel_id";
        template.execute(sql2);

        String sql3 = "UPDATE cancel \n" +
                "SET cancel_price = cancel_price / 100 * 20 \n" +
                "WHERE (cancel_id = @cancel_id \n" +
                "AND (cancel_days > 49)); \n";
        template.execute(sql3);

                String sql4 = "UPDATE cancel \n" +
                "SET cancel_price = cancel_price / 100 * 50 \n" +
                "WHERE (cancel_id = @cancel_id \n" +
                "AND (cancel_days > 15) \n" +
                "AND (cancel_days < 50)); \n";
        template.execute(sql4);

            String sql5 = "UPDATE cancel\n" +
                "SET cancel_price = cancel_price / 100 * 80 \n" +
                "WHERE (cancel_id = @cancel_id \n" +
                "AND (cancel_days > 1) \n" +
                "AND (cancel_days < 14)); \n";
        template.execute(sql5);

            String sql6 = "UPDATE cancel\n" +
                "SET cancel_price = cancel_price / 100 * 95 \n" +
                "WHERE (cancel_id = @cancel_id \n" +
                "AND (cancel_days < 2));";
        template.execute(sql6);

        String sql7 = "UPDATE rentals \n" +
                "SET total_price = (SELECT cancel_price FROM cancel WHERE cancel_id = @cancel_id) \n" +
                "WHERE rental_id = ?;";
        template.update(sql7, rental_id);

        return null;
    }

    public Cancel findCancelById(int cancel_id)
    {
        String sqlL = "SELECT @cancel_id := last_insert_id()";
        template.execute(sqlL);
        String sql = "SELECT * FROM cancel where cancel_id = @cancel_id";
        RowMapper<Cancel> rowMapper = new BeanPropertyRowMapper<>(Cancel.class);
        Cancel ca = template.queryForObject(sql, rowMapper,cancel_id);
        return ca;
    }

    public Boolean deleteCancel(int cancel_id)
    {
        String sql = "DELETE FROM cancel WHERE cancel_id = ?";
        return template.update(sql, cancel_id) < 0;
    }
}
