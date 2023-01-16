package br.com.itstoony.github.api.attornatus.Controller;

import br.com.itstoony.github.api.attornatus.model.Address;
import br.com.itstoony.github.api.attornatus.model.Users;
import br.com.itstoony.github.api.attornatus.model.dto.UserRecord;
import br.com.itstoony.github.api.attornatus.model.dto.UsersDto;
import br.com.itstoony.github.api.attornatus.service.AddressService;
import br.com.itstoony.github.api.attornatus.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
public class UserControllerTest {

    static String USER_API = "/users";

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    AddressService addressService;

    @Test
    @DisplayName("Should create an user")
    public void createUserTest() throws Exception {

        UserRecord record = createRecord();

        Users savedUser = createUsers();

        BDDMockito.given(userService.insertByRecord(record)).willReturn(savedUser);
        BDDMockito.given(addressService.findByCep(record.cep())).willReturn(createAddress());

        String json = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(record);

        var request = MockMvcRequestBuilders
                .post(USER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should throw an error when theres not enough input data")
    public void createInvalidUserTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new UserRecord("", null, "", ""));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar um usuário pelo ID")
    public void findByIdTest() throws Exception {
        Long id = 1L;

        UsersDto userDto = createUserDto(id);
        Users user = createUsers();

        Mockito.when(userService.findById(id)).thenReturn(user);
        Mockito.when(userService.toDto(user)).thenReturn(userDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(USER_API + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        UsersDto dto = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(result.getResponse().getContentAsString(), UsersDto.class);

        assertThat(id).isEqualTo(dto.getId());
        assertThat(dto.getName()).isEqualTo(user.getName());
        assertThat(dto.getBirthDay()).isEqualTo(user.getBirthDay());
        assertThat(dto.getAddressList()).isEqualTo(user.getAddress());
    }

    @Test
    @DisplayName("Should update an user")
    public void updateTest() throws Exception {
        // scenary
        Long id = 1L;
        UserRecord record = createRecord();
        String json = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(record);

        Users user = Users.builder()
                .name("Maria")
                .birthDay(LocalDate.of(1995, 2, 2))
                .registrationDate(LocalDate.now())
                .address(new ArrayList<>())
                .build();

        Users updatedUser = Users.builder()
                .name(record.name())
                .birthDay(record.birthDay())
                .build();

        BDDMockito.given(userService.findById(id)).willReturn(user);
        BDDMockito.given(userService.update(user, record)).willReturn(updatedUser);
        BDDMockito.given(userService.toDto(Mockito.any(Users.class))).willReturn(createUserDto(id));

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(USER_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // validation
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(record.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("birthDay").value(String.valueOf(record.birthDay())));

    }

    @Test
    @DisplayName("Should retrieve the addressList from an user")
    public void getAddressTest() throws Exception {
        // scenary
        Long id = 1L;
        Users user = createUsers();
        user.setId(id);
        user.getAddress().add(createAddress());

        BDDMockito.given(userService.findById(id)).willReturn(user);

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(USER_API + "/" + id + "/address")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        List<Address> addressList = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        // validation
        assertThat(addressList.get(0).getId()).isEqualTo(createAddress().getId());
        assertThat(addressList.get(0).getZipcode()).isEqualTo(createAddress().getZipcode());
        assertThat(addressList.get(0).getCity()).isEqualTo(createAddress().getCity());
        assertThat(addressList.get(0).getStates()).isEqualTo(createAddress().getStates());
        assertThat(addressList.get(0).getCity()).isEqualTo(createAddress().getCity());
        assertThat(addressList.get(0).getStreet()).isEqualTo(createAddress().getStreet());
        assertThat(addressList.get(0).getNeighborhood()).isEqualTo(createAddress().getNeighborhood());
        assertThat(addressList.get(0).getHouseNumber()).isEqualTo(createAddress().getHouseNumber());


    }

    @Test
    @DisplayName("Should add an address to an users addressList")
    public void addAddressTest() throws Exception {
        // scenary
        Long id = 1L;
        Users user = createUsers();
        user.setId(id);
        UserRecord record = createRecord();
        Address address = createAddress();
        user.getAddress().add(address);

        String json = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(record);

        BDDMockito.given(userService.findById(id)).willReturn(user);
        BDDMockito.given(addressService.findByCep(record.cep())).willReturn(address);
        BDDMockito.given(userService.addAddress(user, record)).willReturn(address);

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USER_API + "/" + id + "/address")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn();

        // validation
        assertThat(user.getAddress()).contains(address);

    }

    @Test
    @DisplayName("Should set an address as the main address of an user")
    public void setMainAddressTest() throws Exception {
        //scenario
        Long userId = 1L;
        Long addressId = 2L;
        Users user = createUsers();
        user.setId(userId);
        Address address = createAddress();
        address.setMain(true);
        address.setId(addressId);
        user.getAddress().add(address);

        BDDMockito.given(userService.findById(userId)).willReturn(user);
        BDDMockito.given(addressService.findById(addressId)).willReturn(address);
        BDDMockito.given(userService.insert(user)).willReturn(user);
        BDDMockito.given(addressService.insert(address)).willReturn(address);

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(USER_API + "/" + userId + "/address/" + addressId)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        //validation
        assertThat(address.getMain()).isTrue();
        assertThat(user.getAddress()).allMatch(a -> !a.getMain() || a.equals(address));
    }


    private static UsersDto createUserDto(Long id) {
        return UsersDto.builder()
                .id(id)
                .name("John")
                .birthDay(LocalDate.of(2000, 1, 2))
                .addressList(new ArrayList<>())
                .build();
    }

    public static Users createUsers() {
        return Users.builder()
                .name("John")
                .birthDay(LocalDate.of(2000, 1, 2))
                .registrationDate(LocalDate.now())
                .address(new ArrayList<>())
                .build();
    }

    public static UserRecord createRecord() {
        return new UserRecord("John", LocalDate.of(2000, 1, 2), "73050000", "52");
    }

    public static Address createAddress() {
        return Address.builder()
                .id(1L)
                .users(null)
                .city("NI")
                .states("")
                .street("")
                .neighborhood("")
                .zipcode("73050000")
                .houseNumber("52")
                .build();
    }

}
