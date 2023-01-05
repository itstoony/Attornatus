package br.com.itstoony.github.api.attornatus.model;

import jakarta.persistence.*;
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
    private String name;

    @Column(name = "birthDay")
    private LocalDate birthDay;

    @Column(name = "registrationDate")
    private LocalDate registrationDate;

    @OneToMany(mappedBy = "users")
    private List<Address> address;

}
