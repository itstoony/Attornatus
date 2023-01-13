package br.com.itstoony.github.api.attornatus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Column(name = "birthDay")
    @NotNull(message = "birthday should not be null")
    private LocalDate birthDay;

    @Column(name = "registrationDate")
    private LocalDate registrationDate;

    @OneToMany(mappedBy = "users")
    private List<Address> address;

}
