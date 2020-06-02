package com.example.demo.Controller;

import com.example.demo.Service.CarsService;
import com.example.demo.Service.CustomerService;
import com.example.demo.Service.PickUpService;
import com.example.demo.Service.RentalService;
import com.example.demo.model.Cars;
import com.example.demo.model.PickUpPoint;
import com.example.demo.model.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

@Controller
@SessionAttributes("rental")
public class HomeController
{
    @Autowired
    RentalService rentalservice;

    @Autowired
    PickUpService pickUpService;

    @Autowired
    CarsService carservice;

    @Autowired
    CustomerService cosService;

    @ModelAttribute("rental")
    public Rental getRental()
    {
        System.out.println("laver ny rental");
        return new Rental();
    }

    @GetMapping("/")
    public String index(Model model)
    {
        List<Rental> rentalList = rentalservice.fetchAll();
        model.addAttribute("rentals", rentalList);

        return "home/index";
    }

    @GetMapping("/create")
    public String createForm(Model model)
    {
        List<PickUpPoint> PickUpList = pickUpService.fetchAll();
        model.addAttribute("point", PickUpList);
        System.out.println(PickUpList.size());
        //model.addAttribute("point", pickUpService.findPointById(id));


        //pickUpService.addPoint(p);
        return "home/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("rental") Rental r, PickUpPoint p)
    {
        System.out.println("New rental start \""+r.getFrom_Date()+"\" pickup.id "+r.getPickUP_id() + " Her kommer et rental id: " + r.getRental_id());

        System.out.println("New pickup place "+p.getPlace() + " afstand fra kontor " + p.getKmAway() + " km. Pickup ID = " + p.getPickUP_id());
        if(p.getPlace().length()>0)
        {
            PickUpPoint pp = new PickUpPoint(0,p.getPlace(), p.getKmAway());
            pickUpService.addPoint(pp);
            r.setPickUP_id(pp.getPickUP_id());
            r.setDropOf_id(pp.getPickUP_id());
        }

        System.out.println("create returnerer confirm siden");
        return "redirect:/chooseCar";  //"redirect:/";
    }

    @GetMapping("/chooseCar")
    public String chooseCarform(@ModelAttribute("rental") Rental r, Model model)
    {
        System.out.println("Reading biler");
        List<Cars> availableCarList = carservice.fetchAvailablePriceGroups(r.getFrom_Date(), r.getTo_Date());
        model.addAttribute("pgList", availableCarList);
        List<Cars> carList = carservice.fetchAll();
        System.out.println("Biler sz "+carList.size());
        model.addAttribute("carlist", carList);



        return "home/chooseCar";
    }

    @PostMapping("/chooseCar")
    public String chooseCarsubmit(@ModelAttribute("rental") Rental r, Model model)
    {
        System.out.println("CHOOSE dato fra"+r.getFrom_Date()+" til "+ r.getTo_Date() +" pris gruppe "+ r.getPrice_id());
        Cars c = rentalservice.chooseCar(r.getFrom_Date(), r.getTo_Date(), r.getPrice_id());
        System.out.println("CHOOSE bil id "+c.getDocumentation_id());
        r.setDocumentation_id(c.getDocumentation_id());

        //Udregn pris for bil og saeson
        rentalservice.getRentalPrice(r);
        System.out.println("Rental price "+r.getTotal_price());

        model.addAttribute("car", c);
        //rentalservice.createRental(r);
        return "redirect:/confirm";
    }

    @GetMapping("/confirm")
    public String confirmForm(@ModelAttribute("rental") Rental r, Model model)
    {
        System.out.println("Confirm-get new rental start \""+r.getFrom_Date()+"\" pickup.id "+r.getPickUP_id() + " Her kommer et rental id: " + r.getRental_id());
        model.addAttribute("pickUp", pickUpService.findPointById(r.getPickUP_id()));
        model.addAttribute("dropOf", pickUpService.findPointById(r.getDropOf_id()));
        model.addAttribute("car", carservice.findCarById(r.getDocumentation_id()));
        model.addAttribute("customer", cosService.findCustomerById(r.getCustomer_id()));
        return "home/confirm";
    }

    @PostMapping("/confirm")
    public String confirmSubmit(@ModelAttribute("rental") Rental r, SessionStatus status)//, PickUpPoint p)
    {
        System.out.println("Confirm-post rental start \""+r.getFrom_Date()+"\" pickup.id "+r.getPickUP_id() + " Her kommer et rental id: " + r.getRental_id());
        rentalservice.createRental(r);
        status.setComplete();
        return "redirect:/";
    }


    @GetMapping("/viewOne/{id}")
    public String viewOne(@PathVariable("id") int id, Model model)
    {
        Rental r = rentalservice.findRentalById(id);
        model.addAttribute("rent", r);
        model.addAttribute("pickUp", pickUpService.findPointById(r.getPickUP_id()));
        model.addAttribute("dropOf", pickUpService.findPointById(r.getDropOf_id()));
        model.addAttribute("car", carservice.findCarById(r.getDocumentation_id()));
        model.addAttribute("customer", cosService.findCustomerById(r.getCustomer_id()));
        return "home/ViewRental";
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

    /*@GetMapping("/update/{rental_id}")
    public String update(@PathVariable("rental_id") int id, Model model)
    {
        System.out.println("update rental id = " + id);
        Rental r = rentalservice.findRentalById(id);
        model.addAttribute("rental", r);
        System.out.println("update rental id "+ r.getRental_id() + " price " + r.getTotal_price());

        return "home/updateRental";
    }

    @PostMapping("/updateRental/")
    public String updateRental(@ModelAttribute("rental") Rental r)
    {
        System.out.println("updateRental rental id "+ r.getRental_id() + " price " + r.getTotal_price() + " kunde " + r.getCustomer_id());
        rentalservice.updateRental(r.getRental_id(), r);
        return "redirect:/";
    }*/

    @GetMapping("/returnering/{rental_id}")
    public String returnering(@PathVariable("rental_id") int id, Model model)
    {
        Rental r = rentalservice.findRentalById(id);
        System.out.println("RENTAL returnering id "+r.getRental_id()+" milage "+r.getMileage());
        model.addAttribute("rental", r);


        Cars c = carservice.findCarById(r.getDocumentation_id());
        model.addAttribute("car", c);
        return "home/returnering";
    }

    @PostMapping("/returnerBil/")
    public String returnerBil(@ModelAttribute("rental") Rental r) //@ModelAttribute("rental"))
    {
        System.out.println("RetunerBil " + r.getRental_id());
        rentalservice.returnerBil(r.getRental_id(),r);

        return "redirect:/";
    }
}
