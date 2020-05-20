package com.example.demo.CarsService;

import com.example.demo.Model.Cars;
import com.example.demo.Model.Customer;
import com.example.demo.Repository.CarsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarsService {
    @Autowired
    CarsRepo carsRepo;

    public List<Cars> fetchAll(){

        return carsRepo.fetchAll();
    }

    public Cars addCar(Cars c){

        return carsRepo.addCar(c);
    }

    public Cars findCarById(int car_id){

        return carsRepo.findCarById(car_id);
    }

    public Boolean deleteCar(int documentation_id){

        return carsRepo.deleteCar(documentation_id);
    }

    public Cars updateCar(int car_id, Cars c){

        return carsRepo.updateCar(car_id, c);
    }
}
