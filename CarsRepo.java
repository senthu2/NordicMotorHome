package com.example.demo.Repository;

import com.example.demo.Model.Cars;
import com.example.demo.Model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarsRepo {
    @Autowired
    JdbcTemplate template;

    public List<Cars> fetchAll(){
        String sql = "SELECT * FROM cars INNER JOIN documentation WHERE cars.car_id = documentation.car_id";
        RowMapper<Cars> rowMapper = new BeanPropertyRowMapper<>(Cars.class);
        return template.query(sql, rowMapper);
    }

    public Cars addCar(Cars c) {

        String sql1 = "INSERT INTO cars (brand, model, beds) VALUES (?,?,?)";
        template.update(sql1, c.getBrand(), c.getModel(), c.getBeds());

        String sql3 = "SELECT @car_id := last_insert_id()";
        template.execute(sql3);


        String sql2 = "INSERT INTO documentation (car_status, car_registration, mileage, price_group, car_id) VALUES (?,?,?,?,@car_id)";
        template.update(sql2, c.getCar_status(), c.getCar_registration(), c.getMileage(), c.getPrice_group());

        return null;
    }

    public Cars findCarById(int documentation_id){
        String sql = "SELECT cars.car_id,brand,model,beds,documentation_id,car_status,car_registration,mileage,price_group FROM documentation " +
                "JOIN cars ON cars.car_id = documentation.car_id WHERE documentation_id = ?";
        RowMapper<Cars> rowMapper = new BeanPropertyRowMapper<>(Cars.class);
        Cars c = template.queryForObject(sql, rowMapper, documentation_id);
        return c;
    }

    public Boolean deleteCar(int documentation_id){

        String sql = "DELETE FROM documentation WHERE documentation_id = ?";
        template.update(sql, documentation_id);

        String sql2 = "DELETE FROM cars WHERE car_id = ?";
        return template.update(sql2, documentation_id) < 0;
    }

    public Cars updateCar(int car_id, Cars c) {
        String sql = "UPDATE cars SET brand = ?, model = ?, beds = ? WHERE car_id = ?";
        template.update(sql, c.getBrand(), c.getModel(), c.getBeds(), c.getCar_id());

        String sql1 = "UPDATE documentation SET car_status = ?, car_registration = ?, mileage = ?, price_group = ? WHERE documentation_id =  ?";
        template.update(sql1, c.getCar_status(), c.getCar_registration(), c.getMileage(), c.getPrice_group(), c.getCar_id());
        return null;
    }
}