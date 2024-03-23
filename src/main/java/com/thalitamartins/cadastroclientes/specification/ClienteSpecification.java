package com.thalitamartins.cadastroclientes.specification;

import com.thalitamartins.cadastroclientes.domain.Cliente;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class ClienteSpecification implements SpecificationDefault<Cliente> {

    private Specification<Cliente> nome(String nome) {
        return ((root, query, cb) -> cb.like(root.get("nome"), nome));
    }

    private Specification<Cliente> cpf(String cpf) {
        return ((root, query, cb) -> cb.like(root.get("cpf"), cpf));
    }

    private Specification<Cliente> dataNascimento(LocalDate data) {
        return ((root, query, cb) -> cb.equal(root.get("dataNascimento"), data));
    }

    private Specification<Cliente> isAtivo() {
        return (root, query, cb) -> cb.isTrue(root.get("ativo"));
    }

    public Specification<Cliente> filters(String nome, String cpf, LocalDate dataNascimento) {
        var builder = this.builder();

        builder.and(isAtivo());

        Optional.ofNullable(nome).map(this::nome).ifPresent(builder::and);
        Optional.ofNullable(cpf).map(this::cpf).ifPresent(builder::and);
        Optional.ofNullable(dataNascimento).map(this::dataNascimento).ifPresent(builder::and);

        return builder.build();
    }
}
