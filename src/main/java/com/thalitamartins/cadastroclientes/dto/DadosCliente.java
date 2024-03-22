package com.thalitamartins.cadastroclientes.dto;


import com.thalitamartins.cadastroclientes.domain.Contato;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DadosCliente {

    public String nome;

    public String cpf;

    public LocalDate dataNascimento;

    public List<Contato> contatoList = new ArrayList<>();

}
