package controllers;

import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import dto.BookServiceRecord;
import org.springframework.web.util.UriUtils;
import services.CustomerService;
import services.JwtService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookservice")
public class BooksServiceGateway {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private JwtService jwtService;
    //@Value("${bookservice.addbook.url}")
    private static final String bookUrl = "http://localhost:8081/books";

    @PostMapping("/addbook")
    public ResponseEntity<String> addBook(@RequestBody BookServiceRecord request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<BookServiceRecord> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForEntity(bookUrl + "/addbook", entity, String.class);
    }

    @GetMapping("/getallbooks")
    public ResponseEntity<List<Map<String, String>>> getallbooks() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(bookUrl + "/getallbooks", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Map<String, String>>>() {});
    }

    @GetMapping("/findbytitle")
    public ResponseEntity<Map<String, String>> findByTitle(@RequestParam String title) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = bookUrl + "/getbytitle?title=" + title;
        return restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, String>>() {});
    }

    @GetMapping("/rentabook")
    public ResponseEntity<String> rentABook(@RequestParam String title, @RequestHeader("Authorization") String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = bookUrl + "/rentabook?title=" + title;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<String>() {});

        if (response.getBody().equals("successfully rented a book") && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String userEmail = jwtService.extractEmail(token);
            try {
                System.out.println("EXTRACTED EMAIL " + userEmail);
                customerService.addToRented(title, userEmail);
                return ResponseEntity.ok("successfully rented a book: " + title);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("something went wrong trying to rent a book");
        }
    }

    @GetMapping("/bringbookback")
    public ResponseEntity<String> bringBookBack(@RequestParam String title, @RequestHeader("Authorization") String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = bookUrl + "/bringbookback?title=" + title;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<String>() {});

        if(response.getBody().equals("book has been brought back successfully") && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String userEmail = jwtService.extractEmail(token);
            try {
                customerService.bringBookBack(title, userEmail);
                return ResponseEntity.ok("book has been brought back successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("something went wrong bringing a book back");
        }
    }
}