package com.thalitamartins.cadastroclientes.service;

import com.thalitamartins.cadastroclientes.domain.Cliente;
import com.thalitamartins.cadastroclientes.domain.Contato;
import com.thalitamartins.cadastroclientes.exception.MensagemException;
import com.thalitamartins.cadastroclientes.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;


@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    @Autowired
    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    @Transactional
    public void create(Cliente cliente) {
        var contatos = cliente.getContato();
        for (Contato contato : contatos) {
            if (isNull(contato.getNome()) || isNull(contato.getTelefone()) || isNull(contato.getEmail())) {
                throw new MensagemException("Todos os campos do contato são de preenchimento obrigatório!");
            }
        }
        cliente.getContato().stream()
                .peek(contato -> contato.setCliente(cliente))
                .peek(contato -> contato.setAtivo(true))
                .forEach(this::save);
    }

    @Transactional
    public void save(Contato contato) {
        contatoRepository.save(contato);
    }
}
