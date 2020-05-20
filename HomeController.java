package com.example.demo.Controller;

import com.example.demo.Service.RentalService;
import com.example.demo.model.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@ControllerAdvice

public class HomeController
{
    @Autowired
    RentalService rentalservice;

    @GetMapping("/")
    public String index(Model model)
    {
        List<Rental> rentalList = rentalservice.fetchAll();
        model.addAttribute("rentals", rentalList);
        return "home/index";
    }

    @GetMapping("/create")
    public String create()
    {
        return "home/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Rental r)
    {
        rentalservice.createRental(r);
        return "redirect:/";
    }

    @GetMapping("/viewOne/{id}")
    public String viewOne(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("rental", rentalservice.findRentalById(id));
        return "ViewRental";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")int id)
    {
        boolean deleted = rentalservice.deleteRental(id);
        if(deleted)
        {
            return "redirect:/";
        }
        return "redirect:/";
    }

    @GetMapping("/update/{rental_id}")
    public String update(@PathVariable("rental_id") int id, Model model)
    {
        System.out.println("update rental id = " + id);
        Rental r = rentalservice.findRentalById(id);
        model.addAttribute("rental", r);
        System.out.println("update rental id "+ r.getRental_id() + " price " + r.getTotal_price());

        return "updateRental";
    }

    @PostMapping("/updateRental/")
    public String updateRental(@ModelAttribute("rental") Rental r)
    {
        System.out.println("updateRental rental id "+ r.getRental_id() + " price " + r.getTotal_price() + " kunde " + r.getCustomer_id());
        rentalservice.updateRental(r.getRental_id(), r);
        return "redirect:/";
    }
}
