package br.com.itstoony.github.api.attornatus.dto;

import lombok.Data;

@Data
public class AddressDto {

    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String ebge;
    private String gia;
    private String ddd;
    private String siafi;

}
