package br.com.itstoony.github.api.attornatus.service;

import br.com.itstoony.github.api.attornatus.model.Address;
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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
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

        user.getAddress().get(0).setMain(true);
        insert(user);
        address.setUsers(user);
        addressService.insert(address);

        return user;
    }

    public Users insert(Users users) {
        return userRepository.save(users);
    }

    public Users findById(Long id) {
        var op = userRepository.findById(id);
        return op.orElseThrow(() -> new EntityNotFoundException("Invalid User ID"));
    }

    @Transactional
    public Users update(Users user, UserRecord userRecord) {

        if (userRecord.name() != null) {
            user.setName(userRecord.name());
            insert(user);
        }

        if (userRecord.birthDay() != null) {
            user.setBirthDay(userRecord.birthDay());
            insert(user);
        }

        return user;

    }

    public UsersDto toDto(Users user) {
        return UsersDto.builder()
                .id(user.getId())
                .name(user.getName())
                .birthDay(user.getBirthDay())
                .addressList(user.getAddress())
                .build();
    }

    @Transactional
    public Address addAddress(Users user, UserRecord record) {
        var address = addressService.findByCep(record.cep());
        address.setHouseNumber(record.number());
        addressService.insert(address);

        user.getAddress().add(address);
        insert(user);

        address.setUsers(user);
        addressService.insert(address);

        return address;
    }

    @Transactional
    public void setMainAddress(Users user, Address address) {
        var addressList = user.getAddress();
        addressList.forEach(a -> a.setMain(false));

        if (!user.getAddress().contains(address)) {
            throw new RuntimeException("Users addressList doesn't contain refered Address");
        }

        address.setMain(true);

        insert(user);
        addressService.insert(address);
    }

    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

}
