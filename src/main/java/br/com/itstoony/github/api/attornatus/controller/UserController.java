package br.com.itstoony.github.api.attornatus.controller;

import br.com.itstoony.github.api.attornatus.model.Address;
import br.com.itstoony.github.api.attornatus.model.dto.UserRecord;
import br.com.itstoony.github.api.attornatus.model.dto.UsersDto;
import br.com.itstoony.github.api.attornatus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserRecord record) {
        var user = userService.insertByRecord(record);

        userService.insert(user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UsersDto> findById(@PathVariable Long id) {
        var user = userService.findById(id);

        var dto = userService.toDto(user);

        return ResponseEntity.ok(dto);
    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UserRecord record) {

        var user = userService.findById(id);

        userService.update(user, record);

        return ResponseEntity.ok().build();

    }

    @GetMapping(path = "/{id}/address")
    public ResponseEntity<List<Address>> getAddress(@PathVariable Long id) {
        var user = userService.findById(id);

        return ResponseEntity.ok(user.getAddress());
    }

    @PostMapping(path = "/{id}/address")
    public ResponseEntity<Void> addAddress(@PathVariable Long id, @RequestBody UserRecord record) {
        var user = userService.findById(id);

        var address = userService.addAddress(user, record);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(address.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

}
