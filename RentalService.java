package com.example.demo.Service;

import com.example.demo.Repository.RentalRepo;
import com.example.demo.model.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService
{
    @Autowired
    RentalRepo rentalrepo;
    public List<Rental> fetchAll()
    {
        return rentalrepo.fetchAll();
    }

    public Rental createRental(Rental r)
    {
        return rentalrepo.createRental(r);
    }

    public Rental findRentalById(int id)
    {
        return rentalrepo.findRentalById(id);
    }

    public Boolean deleteRental(int id)
    {
        return rentalrepo.deleteRental(id);
    }

    public Rental updateRental(int id, Rental r)
    {
        return rentalrepo.updateRental(id, r);
    }

    /*public Rental endRental(int id, Rental r)
    {
        return rentalrepo.endRental(id, r);
    }*/

    public double setSeasonPrice(Rental r)
    {
        r.setTotal_price(r.getTotal_price()* prisfaktor());
        return r.getTotal_price();
    }

    public Rental returnerBil(int id, Rental r)
    {
        System.out.println("returnerbil xxxx id = "+id + " rental.id = "+r.getRental_id());
        System.out.println("Det her er mileage " + r.getMileage());

        overDriven(r);
        System.out.println("Det her er mileage " + r.getMileage());
        return rentalrepo.returnerBil(id, r);
    }

    public double prisfaktor()   //fixME
    {
        boolean highSeason = false;
        boolean middleseason = true;
        boolean lowseason = false;

        if(highSeason)
        {
            return 1.60;
        }
        if(middleseason)
        {
            return 1.30;
        }
        return 1.0;
    }

    public int tankFilled()
    {
        boolean isTankFilled = false;

        if(isTankFilled)
        {
            return 0;
        }
        return 70;
    }

    public Rental setTankPrice(int id, Rental r)
    {
        //r.setTotal_price(r.getTotal_price()+tankFilled());
        //return r.getTotal_price();
        return rentalrepo.setTankPrice(id, r);
    }

    /*public void overDriven(Rental r)
    {
        int kmToDrive = 400*r.getDays_of_rental();
        int driven = (r.getOverDriven())-r.getMileage();
        int KmTooMany = driven-kmToDrive;

        if (driven >= kmToDrive)
        {
            r.setTotal_price(r.getTotal_price() + KmTooMany);
        }
        r.setMileage(r.getOverDriven());
    } */

    public void overDriven(Rental r)
    {
         r.setTotal_price(r.getTotal_price()+Math.max(0, (r.getOverDriven()-r.getMileage())-(r.getDays_of_rental()*400)));
         System.out.println("OverDriven = " + r.getOverDriven() + ". Milage = " + r.getMileage() + ". DaysofRental = " + r.getDays_of_rental());
        //r.setMileage(r.getOverDriven());

    }
}
