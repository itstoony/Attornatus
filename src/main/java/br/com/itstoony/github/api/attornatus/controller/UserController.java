package br.com.itstoony.github.api.attornatus.controller;

import br.com.itstoony.github.api.attornatus.model.Address;
import br.com.itstoony.github.api.attornatus.model.Users;
import br.com.itstoony.github.api.attornatus.model.dto.UserRecord;
import br.com.itstoony.github.api.attornatus.model.dto.UsersDto;
import br.com.itstoony.github.api.attornatus.service.AddressService;
import br.com.itstoony.github.api.attornatus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "API responsible for users maintenance.")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;


    @Operation(summary = "Create a user", description = "Create a new user with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to create user")
    })
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid UserRecord record) {
        var user = userService.insertByRecord(record);

        userService.insert(user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Find an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details successfully obtained."),
            @ApiResponse(responseCode = "400", description = "Falied to find user.")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<UsersDto> findById(@PathVariable Long id) {
        var user = userService.findById(id);

        var dto = userService.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Update an user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated."),
            @ApiResponse(responseCode = "400", description = "Failed to update user.")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<UsersDto> update(@PathVariable Long id, @RequestBody @Valid UserRecord record) {

        var user = userService.findById(id);

        Users updatedUser = userService.update(user, record);

        UsersDto dto = userService.toDto(updatedUser);

        return ResponseEntity.ok().body(dto);

    }

    @Operation(summary = "Get all addresses from an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users addressList successfully obtained."),
            @ApiResponse(responseCode = "400", description = "Failed to get users addressList.")
    })
    @GetMapping(path = "/{id}/address")
    public ResponseEntity<List<Address>> getAddress(@PathVariable Long id) {
        var user = userService.findById(id);

        return ResponseEntity.ok(user.getAddress());
    }

    @Operation(summary = "Add a new address to an user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New address added successfully."),
            @ApiResponse(responseCode = "400", description = "Failed to add new address")
    })
    @PostMapping(path = "/{id}/address")
    public ResponseEntity<Void> addAddress(@PathVariable Long id, @RequestBody UserRecord record) {
        var user = userService.findById(id);

        var address = userService.addAddress(user, record);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(address.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Set an address as a users main address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address setted as main successfully."),
            @ApiResponse(responseCode = "400", description = "Failed to set address as main")
    })
    @PutMapping(path = "/{userId}/address/{addressId}")
    public ResponseEntity<Void> setMainAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        var user = userService.findById(userId);

        var address = addressService.findById(addressId);

        userService.setMainAddress(user, address);

        return ResponseEntity.ok().build();
    }

}
