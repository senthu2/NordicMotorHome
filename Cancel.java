package com.example.demo.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cancel {
    @Id
    private int cancel_id;
    private int cancel_price;
    private String cancel_date;
    private int cancel_days;

    public Cancel() {
    }

    public Cancel(int cancel_id, int cancel_price, String cancel_date, int cancel_days) {
        this.cancel_id = cancel_id;
        this.cancel_price = cancel_price;
        this.cancel_date = cancel_date;
        this.cancel_days = cancel_days;
    }

    public int getCancel_id() {
        return cancel_id;
    }

    public void setCancel_id(int cancel_id) {
        this.cancel_id = cancel_id;
    }

    public int getCancel_price() {
        return cancel_price;
    }

    public void setCancel_price(int cancel_price) {
        this.cancel_price = cancel_price;
    }

    public String getCancel_date() {
        return cancel_date;
    }

    public void setCancel_date(String cancel_date) {
        this.cancel_date = cancel_date;
    }

    public int getCancel_days() {
        return cancel_days;
    }

    public void setCancel_days(int cancel_days) {
        this.cancel_days = cancel_days;
    }
}
