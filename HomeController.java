package com.example.demo.Controller;

import com.example.demo.Model.*;
import com.example.demo.Service.*;
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
public class HomeController {
    @Autowired
    StaffService staffService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CarsService carsService;
    @Autowired
    RentalService rentalservice;
    @Autowired
    CancelService cancelService;
    @Autowired
    PickUpService pickUpService;

    @ModelAttribute("rental")
    public Rental getRental()
    {
        System.out.println("laver ny rental");
        return new Rental();
    }

    @GetMapping("/")
    public String index(Model model){
        List<Staff> staffList = staffService.fetchAll();
        model.addAttribute("staff", staffList);

        List<Customer> customerList = customerService.fetchAll();
        model.addAttribute("customers", customerList);

        List<Cars> carsList = carsService.fetchAll();
        model.addAttribute("cars", carsList);

        List<Rental> rentalList = rentalservice.fetchAll();
        model.addAttribute("rentals", rentalList);

        List<Cancel> cancelList = cancelService.fetchAll();
        model.addAttribute("cancel", cancelList);

        return "home/index";
    }

    @GetMapping("/createStaff")
    public String createStaff(){
        return "home/create/createStaff";
    }

    @PostMapping("/createStaff")
    public String createStaff(@ModelAttribute Staff staff){
        staffService.addStaff(staff);
        return "redirect:/";
    }
    @GetMapping("/createCustomer")
    public String createCustomer(){
        return "home/create/createCustomer";
    }

    @PostMapping("/createCustomer")
    public String createCustomer(@ModelAttribute Customer customer){
        customerService.addCustomer(customer);
        return "redirect:/";
    }

    @GetMapping("/createCar")
    public String createCar(){
        return "home/create/createCar";
    }

    @PostMapping("/createCar")
    public String createCar(@ModelAttribute Cars cars){
        carsService.addCar(cars);
        return "redirect:/";
    }

    @GetMapping("/createRental")
    public String createForm(Model model)
    {
        List<Customer> customerList = customerService.fetchAll();
        model.addAttribute("customers", customerList);

        List<PickUpPoint> PickUpList = pickUpService.fetchAll();
        model.addAttribute("point", PickUpList);
        System.out.println(PickUpList.size());
        //model.addAttribute("point", pickUpService.findPointById(id));


        //pickUpService.addPoint(p);
        return "home/manageRental/createRental";
    }

    @PostMapping("/createRental")
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
        List<Cars> availableCarList = carsService.fetchAvailablePriceGroups(r.getFrom_Date(), r.getTo_Date());
        model.addAttribute("pgList", availableCarList);
        List<Cars> carList = carsService.fetchAll();
        System.out.println("Biler sz "+carList.size());
        model.addAttribute("carlist", carList);

        return "home/manageRental/chooseCar";
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
        System.out.println("Confirm-get new rental start \""+r.getFrom_Date()+"\" pickup.id "+r.getPickUP_id() + " Her kommer et rental id: " + r.getRental_id());        model.addAttribute("pickUp", pickUpService.findPointById(r.getPickUP_id()));
        model.addAttribute("pickUp", pickUpService.findPointById(r.getPickUP_id()));
        model.addAttribute("dropOf", pickUpService.findPointById(r.getDropOf_id()));
        model.addAttribute("car", carsService.findCarById(r.getDocumentation_id()));
        model.addAttribute("customer", customerService.findCustomerById(r.getCustomer_id()));
        return "home/manageRental/confirm";
    }

    @PostMapping("/confirm")
    public String confirmSubmit(@ModelAttribute("rental") Rental r, SessionStatus status)//, PickUpPoint p)
    {
        System.out.println("Confirm-post rental start \""+r.getFrom_Date()+"\" pickup.id "+r.getPickUP_id() + " Her kommer et rental id: " + r.getRental_id());
        rentalservice.createRental(r);
        status.setComplete();
        return "redirect:/";
    }

    @GetMapping("/viewOneStaff/{staff_id}")
    public String viewOneStaff(@PathVariable("staff_id") int staff_id, Model model){
        model.addAttribute("staff", staffService.findStaffByID(staff_id));
        return "home/viewOne/viewOneStaff";
    }
    @GetMapping("/viewOneCustomer/{customer_id}")
    public String viewOne(@PathVariable("customer_id") int customer_id, Model model) {
        model.addAttribute("customer", customerService.findCustomerById(customer_id));
        return "home/viewOne/viewOneCustomer";
    }
    @GetMapping("/viewOneCar/{documentation_id}")
    public String viewOneCar(@PathVariable("documentation_id") int documentation_id, Model model) {
        model.addAttribute("cars", carsService.findCarById(documentation_id));
        return "home/viewOne/viewOneCar";
    }
    @GetMapping("/viewOne/{rental_id}")
    public String viewOneRental(@PathVariable("rental_id") int rental_id, Model model)
    {
        Rental r = rentalservice.findRentalById(rental_id);
        model.addAttribute("rent", r);
        model.addAttribute("pickUp", pickUpService.findPointById(r.getPickUP_id()));
        model.addAttribute("dropOf", pickUpService.findPointById(r.getDropOf_id()));
        model.addAttribute("car", carsService.findCarById(r.getDocumentation_id()));
        model.addAttribute("customer", customerService.findCustomerById(r.getCustomer_id()));
        return "home/viewOne/viewOneRental";
    }

    @GetMapping("/deleteStaff/{staff_id}")
    public String deleteStaff(@PathVariable("staff_id") int staff_id){
        boolean del = staffService.deleteStaff(staff_id);
        if (del){
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }
    @GetMapping("/deleteCustomer/{customer_id}")
    public String deleteCustomer(@PathVariable("customer_id") int customer_id){
        boolean deleted =customerService.deleteCustomer(customer_id);
        if(deleted){
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }
    @GetMapping("/deleteCar/{documentation_id}")
    public String deleteCar(@PathVariable("documentation_id") int documentation_id){
        boolean deleted =carsService.deleteCar(documentation_id);
        if(deleted){
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }
    @GetMapping("/deleteRental/{rental_id}")
    public String deleteRental(@PathVariable("rental_id") int rental_id){
        boolean deleted =rentalservice.deleteRental(rental_id);
        if(deleted){
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/updateStaff/{staff_id}")
    public String update(@PathVariable("staff_id") int staff_id, Model model){
        model.addAttribute("staff", staffService.findStaffByID(staff_id));
        return "home/update/updateStaff";
    }

    @PostMapping("/updateStaffUp")
    public String updateStaff(@ModelAttribute Staff staff){
        staffService.updateStaff(staff.getStaff_id(), staff);
        return "redirect:/";
    }
    @GetMapping("/updateCustomer/{customer_id}")
    public String updateCustomer(@PathVariable("customer_id") int customer_id, Model model){
        model.addAttribute("customer", customerService.findCustomerById(customer_id));
        return "home/update/updateCustomer";
    }
    @PostMapping("/updateCustomerUp")
    public String updateCustomerUp(@ModelAttribute Customer customer){
        customerService.updateCustomer(customer.getCustomer_id(), customer);
        return "redirect:/";
    }

    @GetMapping("/updateCar/{car_id}")
    public String updateCar(@PathVariable("car_id") int car_id, Model model){
        model.addAttribute("cars", carsService.findCarById(car_id));
        return "home/update/updateCar";
    }
    @PostMapping("/updateCarUp")
    public String updateCarUp(@ModelAttribute Cars cars){
        carsService.updateCar(cars.getCar_id(), cars);
        return "redirect:/";
    }

    @GetMapping("/updateRental/{rental_id}")
    public String updateRental(@PathVariable("rental_id") int rental_id, Model model){
        model.addAttribute("rental", rentalservice.findRentalById(rental_id));
        return "home/update/updateRental";
    }

   /* @PostMapping("/updateRentalUp")
    public String updateRentalUp(@ModelAttribute Rental rental){
        rentalservice.updateRental(rental.getRental_id(), rental);
        return "redirect:/";
    }*/

    @GetMapping("/cancelRental/{rental_id}")
    public String cancelRental(@PathVariable("rental_id") int rental_id, Model model, @ModelAttribute Cancel cancel, @ModelAttribute Rental rental){
        model.addAttribute("rental", rentalservice.findRentalById(rental_id));
        cancelService.createCancel(rental.getRental_id(), cancel, rental);
        model.addAttribute("rentalUP", rentalservice.findRentalById(rental_id));

        return "home/manageRental/cancelRental";
    }

    @RequestMapping("/cancelRentalUp/{rental_id}")
    public String cancelRentalUp(@PathVariable("rental_id") int rental_id) {
        boolean deleted = rentalservice.deleteRental(rental_id);
        if (deleted) {
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/returnering/{rental_id}")
    public String returnering(@PathVariable("rental_id") int id, Model model)
    {
        Rental r = rentalservice.findRentalById(id);
        System.out.println("RENTAL returnering id "+r.getRental_id()+" milage "+r.getMileage());
        model.addAttribute("rental", r);


        Cars c = carsService.findCarById(r.getDocumentation_id());
        model.addAttribute("car", c);
        return "home/manageRental/returnering";
    }

    @PostMapping("/returnerBil/")
    public String returnerBil(@ModelAttribute("rental") Rental r) //@ModelAttribute("rental"))
    {
        System.out.println("RetunerBil " + r.getRental_id());
        rentalservice.returnerBil(r.getRental_id(),r);

        return "redirect:/";
    }
}

