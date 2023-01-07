package br.com.itstoony.github.api.attornatus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "houseNumber")
    private String houseNumber;

    @Column(name = "street")
    private String street;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "city")
    private String city;

    @Column(name = "states")
    private String states;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "main")
    private Boolean main;

    @JsonIgnore
    @ManyToOne
    private Users users;

}
