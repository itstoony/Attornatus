package br.com.itstoony.github.api.attornatus.service;

import br.com.itstoony.github.api.attornatus.dto.AddressDto;
import br.com.itstoony.github.api.attornatus.model.Address;
import br.com.itstoony.github.api.attornatus.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    /*
     * Consuming "via cep" webservice to get location by zipcode "cep"
     * https://viacep.com.br/
     */
    public AddressDto findCepDto(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return new RestTemplate().getForObject(url, AddressDto.class);
    }

    public Address fromDto(AddressDto dto) {
        return Address.builder()
                .city(dto.getLocalidade())
                .states(dto.getUf())
                .neighborhood(dto.getBairro())
                .street(dto.getLogradouro())
                .zipcode(dto.getCep())
                .build();
    }

    public void insert(Address address) {
        addressRepository.save(address);
    }

    public Address findByCep(String cep) {
        AddressDto dto = findCepDto(cep);
        return fromDto(dto);
    }

}
