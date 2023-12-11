package kz.kbtu.phonebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kbtu.phonebook.model.Contact;
import kz.kbtu.phonebook.model.User;
import kz.kbtu.phonebook.repository.ContactRepository;
import kz.kbtu.phonebook.repository.UserRepository;
import kz.kbtu.phonebook.service.ContactService;
import kz.kbtu.phonebook.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PhoneBookUnitTests {
    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContactRepository contactRepository;

    @Test
    public void testGetUserById() {
        User user = new User(1L, "anelkad");
        User retrievedUser = userService.getUserById(1L);
        assertThat(retrievedUser).isEqualTo(user);
    }

    @Test
    public void testGetContactByUsername() {
        String username = "anelkad";
        Pageable pageable= PageRequest.of(0, 1);
        Page<Contact> retrievedContact = contactService.findContactByUsername(username, pageable);
        assertThat(retrievedContact.getContent().get(0).getUser().getUsername()).isEqualTo(username);
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
}
