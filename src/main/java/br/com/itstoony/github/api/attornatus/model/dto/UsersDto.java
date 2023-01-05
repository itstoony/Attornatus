package br.com.itstoony.github.api.attornatus.model.dto;

import br.com.itstoony.github.api.attornatus.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UsersDto {

    private Long id;
    private String name;
    private LocalDate birthDay;
    private List<Address> addressList;

}
