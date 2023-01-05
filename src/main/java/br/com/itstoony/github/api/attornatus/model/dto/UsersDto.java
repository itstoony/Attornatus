package br.com.itstoony.github.api.attornatus.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UsersDto {

    private String name;
    private LocalDate birthDay;
    private String cep;

}
