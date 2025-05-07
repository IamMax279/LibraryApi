package controllers;

import com.max420.maxlibrary.MaxLibraryApplication;
import dto.CustomerDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = MaxLibraryApplication.class)
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerCustomer_validInput_returnsSuccess() throws Exception {
        mockMvc.perform(post("/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"m\",\"lastName\":\"k\",\"email\":\"email@gmail.com\",\"password\":\"password\",\"role\":\"borrower\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Added a new customer successfully"));
    }

    @Test
    void registerCustomer_invalidInput_throwsError() throws Exception {
        mockMvc.perform(post("/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"m\",\"lastName\":\"\",\"email\":\"mail@gmail.com\",\"password\":\"pass\",\"role\":\"borrower\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("All fields must be filled"));
    }

    @Test
    void registerCustomer_invalidRole_throwsError() throws Exception {
        mockMvc.perform(post("/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"m\",\"lastName\":\"k\",\"email\":\"mail@gmail.com\",\"password\":\"pass\",\"role\":\"borrow\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Role must be either borrower or librarian"));
    }
}
