package com.thalitamartins.cadastroclientes.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Entity
@Table(name = "cliente")
public class Cliente {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String SEQ_GENERATOR = "cliente_id_seq_gen";
    private static final String SEQ_NAME = "cliente_id_seq";

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_GENERATOR)
    @SequenceGenerator(name = SEQ_GENERATOR, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    private String nome;

    private String cpf;

    private LocalDate dataNascimento;

    private Boolean ativo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cliente")
    @JsonIgnoreProperties("cliente")
    private List<Contato> contato = new ArrayList<>();

    public Cliente (DadosCliente dados){
        this.ativo = true;
        this.nome = dados.nome;
        this.cpf = dados.cpf;
        this.dataNascimento = dados.dataNascimento;
        this.contato = dados.contatoList;
    }
}
