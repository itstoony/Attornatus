package br.com.itstoony.github.api.attornatus.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserRecord(@NotEmpty String name, @NotNull LocalDate birthDay, String cep, String number) {

}
