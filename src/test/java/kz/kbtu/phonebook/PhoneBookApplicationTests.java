package kz.kbtu.phonebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kbtu.phonebook.model.Contact;
import kz.kbtu.phonebook.model.User;
import kz.kbtu.phonebook.service.ContactService;
import kz.kbtu.phonebook.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PhoneBookUnitTests {
    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Test
    public void testGetUserById() {
        User user = new User(1L, "anelkad");
        User retrievedUser = userService.getUserById(1L);
        assertThat(retrievedUser).isEqualTo(user);
    }

    @Test
    public void testGetContactByUsername() {
        String username = "anelkad";
        Pageable pageable = PageRequest.of(0, 1);
        Page<Contact> retrievedContact = contactService.findContactByUsername(username, pageable);
        assertThat(retrievedContact.getContent().get(0).getUser().getUsername()).isEqualTo(username);
    }

    @Test
    public void testGetContactByPhoneNumber() {
        String phoneNumber = "+77777456687";
        Pageable pageable = PageRequest.of(0, 1);
        Page<Contact> retrievedContact = contactService.findContactByPhoneNumber(phoneNumber, pageable);
        assertThat(retrievedContact.getContent().get(0).getPhoneNumber()).isEqualTo(phoneNumber);
    }
}

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PhoneBookApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class PhoneBookIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateContact() throws Exception {
        User user = new User(1L, "anelkad");
        Contact contact = new Contact(
                1L,
                "Almaty",
                "+77474354123",
                user
        );

        String json = new ObjectMapper().writeValueAsString(contact);

        mockMvc.perform(post("/admin/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetContactByUserName() throws Exception {
        String username = "anelkad";
        mockMvc.perform(get("/admin/contacts/username")
                        .param("username", username))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetContactByPhoneNumber() throws Exception {
        String number = "+77777456687";
        mockMvc.perform(get("/admin/contacts/number")
                        .param("phonenumber", number))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateContact() throws Exception {
        User user = new User(1L, "anelkad");
        Long id = 2L;
        Contact contact = new Contact(
                id,
                "Almaty",
                "+77474354123",
                user
        );

        String json = new ObjectMapper().writeValueAsString(contact);

        mockMvc.perform(put("/admin/contacts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteContact() throws Exception {
        Long id = 4L;
        mockMvc.perform(delete("/admin/contacts/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User(4L, "Anel");

        String json = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateUser() throws Exception {
        Long id = 4L;
        User user = new User(4L, "Anel123");

        String json = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser() throws Exception {
        Long id = 4L;
        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());
    }

}
