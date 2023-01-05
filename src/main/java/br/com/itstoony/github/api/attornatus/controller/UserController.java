package br.com.itstoony.github.api.attornatus.controller;

import br.com.itstoony.github.api.attornatus.model.dto.UserRecord;
import br.com.itstoony.github.api.attornatus.model.dto.UsersDto;
import br.com.itstoony.github.api.attornatus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserRecord userRecord) {
        var user = userService.insertByRecord(userRecord);

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
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UserRecord userRecord) {

        var user = userService.findById(id);

        userService.update(user, userRecord);

        return ResponseEntity.ok().build();

    }

}
