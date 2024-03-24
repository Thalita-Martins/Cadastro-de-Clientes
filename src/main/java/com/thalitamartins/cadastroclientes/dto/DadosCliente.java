package com.thalitamartins.cadastroclientes.dto;

import com.thalitamartins.cadastroclientes.domain.Cliente;
import com.thalitamartins.cadastroclientes.domain.Contato;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class DadosCliente {

    private Long id;
    public String nome;
    public String cpf;
    @JsonFormat(pattern = "dd/MM/yyyy")
    public LocalDate dataNascimento;
    public Boolean ativo;
    public List<Contato> contatoList = new ArrayList<>();

    public DadosCliente dados(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
        this.dataNascimento = cliente.getDataNascimento();
        this.contatoList = cliente.getContato();
        this.ativo = cliente.getAtivo();
        return this;
    }
}
