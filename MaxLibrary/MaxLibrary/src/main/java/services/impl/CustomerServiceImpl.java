package services.impl;

import dto.CustomerDto;
import models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.CustomerRepository;
import services.CustomerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public void createCustomer(CustomerDto customerDto) throws Exception {
        Customer customer = mapToCustomer(customerDto);
        customerRepository.save(customer);
    }

    @Override
    public String customerExists(CustomerDto customerDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(customerDto.getEmail(), customerDto.getPassword()));
        if(authentication.isAuthenticated()) {
            return "customer exists";
        } else {
            return "customer does not exist";
        }
    }

    @Override
    public Customer findByEmail(String email) throws Exception {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isEmpty()) {
            throw new Exception("customer with such email does not exist");
        }
        return customer.get();
    }

    @Override
    public void addToRented(String title, String email) throws Exception {
        try {
            Customer customer = findByEmail(email);
            List<String> rented = customer.getRented();

            if(rented.contains(title)) {
                throw new Exception("you can't rent the same book twice");
            }

            rented.add(title);
            customer.setRented(rented);
            customerRepository.save(customer);
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void bringBookBack(String title, String email) throws Exception {
        try {
            Customer customer = findByEmail(email);
            List<String> rented = customer.getRented();

            if (!rented.contains(title)) {
                throw new Exception("you can't bring back a book that you didn't rent");
            }

            rented.remove(title);
            customer.setRented(rented);
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private Customer mapToCustomer(CustomerDto customerDto) throws Exception {
        if(customerDto.getFirstName().isEmpty() || customerDto.getLastName().isEmpty() || customerDto.getEmail().isEmpty() || customerDto.getPassword().isEmpty() || customerDto.getRole().isEmpty()) {
            throw new Exception("All fields must be filled");
        }

        if(!Arrays.asList("borrower", "librarian").contains(customerDto.getRole())) {
            throw new Exception("Role must be either borrower or librarian");
        }

        return Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .email(customerDto.getEmail())
                .password(encoder.encode(customerDto.getPassword()))
                .role(customerDto.getRole())
                .build();
    }
}
