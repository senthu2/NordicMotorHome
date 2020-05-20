package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Rental
{
    @Id
    private int rental_id;
    private int documentation_id;
    private int customer_id;
    private int days_of_rental;
    private double total_price;
    private boolean rent_table;
    private boolean rent_chairs;
    private boolean rent_car_seat;
    private boolean rent_bike_rack;
    private boolean rent_bed_linnen;

    public Rental()
    {

    }

    public Rental(int rental_id, int documentation_id, int customer_id, int days_of_rental, int total_price, boolean rent_table, boolean rent_chairs, boolean rent_car_seat, boolean rent_bike_rack, boolean rent_bed_linnen)
    {
        this.rental_id = rental_id;
        this.documentation_id = documentation_id;
        this.customer_id = customer_id;
        this.days_of_rental = days_of_rental;
        this.total_price = total_price;
        this.rent_table = rent_table;
        this.rent_chairs = rent_chairs;
        this.rent_car_seat = rent_car_seat;
        this.rent_bike_rack = rent_bike_rack;
        this.rent_bed_linnen = rent_bed_linnen;
    }


    public int getRental_id()
    {
        return rental_id;
    }

    public void setRental_id(int rental_id)
    {
        this.rental_id = rental_id;
    }

    public int getDocumentation_id()
    {
        return documentation_id;
    }

    public void setDocumentation_id(int documentation_id)
    {
        this.documentation_id = documentation_id;
    }

    public int getCustomer_id()
    {
        return customer_id;
    }

    public void setCustomer_id(int customer_id)
    {
        this.customer_id = customer_id;
    }

    public int getDays_of_rental()
    {
        return days_of_rental;
    }

    public void setDays_of_rental(int days_of_rental)
    {
        this.days_of_rental = days_of_rental;
    }

    public double getTotal_price()
    {
        return total_price;
    }

    public void setTotal_price(double total_price)
    {
        this.total_price = total_price;
    }

    public boolean isRent_table() {
        return rent_table;
    }

    public void setRent_table(boolean rent_table) {
        this.rent_table = rent_table;
    }

    public boolean isRent_chairs() {
        return rent_chairs;
    }

    public void setRent_chairs(boolean rent_chairs) {
        this.rent_chairs = rent_chairs;
    }

    public boolean isRent_car_seat() {
        return rent_car_seat;
    }

    public void setRent_car_seat(boolean rent_car_seat) {
        this.rent_car_seat = rent_car_seat;
    }

    public boolean isRent_bike_rack() {
        return rent_bike_rack;
    }

    public void setRent_bike_rack(boolean rent_bike_rack) {
        this.rent_bike_rack = rent_bike_rack;
    }

    public boolean isRent_bed_linnen() {
        return rent_bed_linnen;
    }

    public void setRent_bed_linnen(boolean rent_bed_linnen) {
        this.rent_bed_linnen = rent_bed_linnen;
    }
}
