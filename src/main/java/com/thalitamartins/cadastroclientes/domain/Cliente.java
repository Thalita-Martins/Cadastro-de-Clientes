package com.thalitamartins.cadastroclientes.domain;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import com.thalitamartins.cadastroclientes.exception.MensagemException;
import jakarta.persistence.*;
import lombok.*;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

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
    private List<Contato> contato = new ArrayList<>();

    public Cliente(DadosCliente dados) {
        this.ativo = true;
        this.nome = dados.nome;
        this.cpf = dados.cpf;
        this.dataNascimento = dados.dataNascimento;
        this.contato = dados.contatoList;
    }

    public void validarDadosCliente(DadosCliente dadosCliente) {
        if (!validarCpf(dadosCliente.getCpf())) {
            throw new MensagemException("CPF inválido!");
        }

        if (!validarDataNascimento(dadosCliente.getDataNascimento())) {
            throw new MensagemException("Data nascimento informada como futura!");
        }

        if (dadosCliente.getContatoList().isEmpty()) {
            throw new MensagemException("Cliente precisa de pelo menos 1 contato!");
        }

        if (dadosCliente.contatoList.stream().noneMatch(contato -> validarEmail(contato.getEmail()))) {
            throw new MensagemException("E-mail do contato inválido!");
        }
    }

    public boolean validarCpf(String cpf) {
        CPFValidator validar = new CPFValidator();
        try {
            validar.assertValid(cpf);
            return true;
        } catch (InvalidStateException e) {
            return false;
        }
    }

    public Boolean validarDataNascimento(LocalDate data) {
        var dataAtual = LocalDate.now();
        return data.isBefore(dataAtual);
    }

    public boolean validarEmail(String email) {
        EmailValidator validador = EmailValidator.getInstance();
        return validador.isValid(email);
    }
}
