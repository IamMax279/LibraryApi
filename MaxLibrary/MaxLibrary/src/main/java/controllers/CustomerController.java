package controllers;

import dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.CustomerService;
import services.JwtService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerDto customerDto) {
        try {
            customerService.createCustomer(customerDto);
            return ResponseEntity.ok("Added a new customer successfully");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CustomerDto customerDto) {
        if(customerService.customerExists(customerDto).equals("customer exists")) {
            String token = jwtService.generateToken(customerDto.getEmail());
            return ResponseEntity.ok("customer found successfully " + token);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("customer not found");
        }
    }
}