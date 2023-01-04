package br.com.itstoony.github.api.attornatus.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthDay")
    private LocalDate birthDay;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

}
