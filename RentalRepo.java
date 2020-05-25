package com.example.demo.Repository;

import com.example.demo.model.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RentalRepo
{
    @Autowired
    JdbcTemplate template;
    public List<Rental> fetchAll()
    {
        String sql = "select r.*, d.mileage from NMP.Rentals r\n" +
                " inner join NMP.documentation d\n" +
                " on r.documentation_id = d.documentation_id";
        //String sql = "select * from Rentals";
        RowMapper<Rental> rowmapper = new BeanPropertyRowMapper<>(Rental.class);
        return template.query(sql, rowmapper);
    }

    public Rental createRental(Rental r)
    {
        String sql = "insert into Rentals (rental_id, documentation_id, customer_id, days_of_rental, total_price, rent_table, rent_chairs, rent_car_seat, rent_bike_rack, rent_bed_linnen) values(?,?,?,?,?,?,?,?,?,?)";
        template.update(sql, r.getRental_id(), r.getDocumentation_id(), r.getCustomer_id(), r.getDays_of_rental(), r.getTotal_price(), r.isRent_table(), r.isRent_chairs(), r.isRent_car_seat(), r.isRent_bike_rack(), r.isRent_bed_linnen());
        return r;
    }

    public Rental findRentalById(int id)
    {
        //String sql = "SELECT * FROM Rentals where rental_id = ?";
        String sql = "select r.*, d.mileage from NMP.Rentals r\n"
                    + " inner join NMP.documentation d\n"
                    + " on r.documentation_id = d.documentation_id"
                    + " where rental_id = ?";
        RowMapper<Rental> rowmapper = new BeanPropertyRowMapper<>(Rental.class);
        Rental r = template.queryForObject(sql, rowmapper,id);
        return r;
    }

    public Boolean deleteRental(int id)
    {
        String sql = "DELETE FROM Rentals WHERE rental_id = ?";
        return template.update(sql, id) > 0;
    }

    public Rental updateRental(int id, Rental r)
    {
        String sql = "UPDATE Rentals set total_price = ? WHERE rental_id = ?";
        System.out.println("SQL '"+ sql + "'");
        System.out.println("pris " + r.getTotal_price()+" id "+r.getRental_id());
        template.update(sql, r.getTotal_price(), r.getRental_id());
        System.out.println("Kommer koden mon ogsaa her til? " + r.getTotal_price());
        return r;
    }

    public Rental returnerBil(int id, Rental r)
    {
        String sql = "UPDATE Rentals set tank_Filled = ?, overDriven = ?, total_price = ? where rental_id = ?";
        String sql1 = "UPDATE documentation set mileage = ? where documentation_id = ?";
        template.update(sql, r.isTank_Filled(),r.getOverDriven(), r.getTotal_price(), r.getRental_id());
        System.out.println("THEAs SQL har  overdriven = "+r.getOverDriven()+" tank_Filled = " + r.isTank_Filled() + " og doc.id = "+ r.getDocumentation_id());
        template.update(sql1, r.getMileage(), r.getDocumentation_id());

        //System.out.println("dok_id = " + r.getDocumentation_id());
        return r;
    }

    public Rental setTankPrice(int id, Rental r)
    {

        String sql = "update Rentals set tank_Filled = ? where rental_id = ?";
        template.update(sql, r.isTank_Filled(), r.getRental_id());
        return r;
    }

    /*public Rental getSeason()
    {
        String sql = "SELECT price_factor from seasons "
    }*/
}