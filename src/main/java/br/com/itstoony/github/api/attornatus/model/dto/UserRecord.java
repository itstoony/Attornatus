package br.com.itstoony.github.api.attornatus.model.dto;

import java.time.LocalDate;

public record UserRecord(String name, LocalDate birthDay, String cep) {

}
