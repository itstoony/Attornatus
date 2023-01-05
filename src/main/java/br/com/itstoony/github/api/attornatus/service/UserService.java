package br.com.itstoony.github.api.attornatus.service;

import br.com.itstoony.github.api.attornatus.model.Users;
import br.com.itstoony.github.api.attornatus.model.dto.UserRecord;
import br.com.itstoony.github.api.attornatus.model.dto.UsersDto;
import br.com.itstoony.github.api.attornatus.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        address.setHouseNumber(userRecord.number());
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

    public Users findById(Long id) {

        var op = userRepository.findById(id);

        if (op.isEmpty()) {
            throw new EntityNotFoundException("Invalid ID");
        }

        return op.get();
    }

    @Transactional
    public void update(Users user, UserRecord userRecord) {

        if (userRecord.name() != null) {
            user.setName(userRecord.name());
        }

        if (userRecord.birthDay() != null) {
            user.setBirthDay(userRecord.birthDay());
        }

        insert(user);
    }

    public UsersDto toDto(Users user) {
        return UsersDto.builder()
                .id(user.getId())
                .name(user.getName())
                .birthDay(user.getBirthDay())
                .addressList(user.getAddress())
                .build();
    }
}
