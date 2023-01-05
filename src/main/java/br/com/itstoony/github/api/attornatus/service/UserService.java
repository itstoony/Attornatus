package br.com.itstoony.github.api.attornatus.service;

import br.com.itstoony.github.api.attornatus.model.Users;
import br.com.itstoony.github.api.attornatus.model.dto.UserRecord;
import br.com.itstoony.github.api.attornatus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;


    public Users insertByRecord(UserRecord userRecord) {

        var address = addressService.findByCep(userRecord.cep());
        addressService.insert(address);

        Users user = Users.builder()
                .name(userRecord.name())
                .address(new ArrayList<>(Collections.singletonList(address)))
                .birthDay(userRecord.birthDay())
                .registrationDate(LocalDate.now())
                .build();

        insert(user);
        address.setUsers(user);
        addressService.insert(address);

        return user;
    }

    public void insert(Users users) {
        userRepository.save(users);
    }

}
