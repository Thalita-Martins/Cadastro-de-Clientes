package com.thalitamartins.cadastroclientes.service;

import com.thalitamartins.cadastroclientes.domain.Cliente;
import com.thalitamartins.cadastroclientes.domain.Contato;
import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import com.thalitamartins.cadastroclientes.exception.MensagemException;
import com.thalitamartins.cadastroclientes.repository.ClienteRepository;
import com.thalitamartins.cadastroclientes.specification.ClienteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteSpecification clienteSpecification;
    private final ContatoService contatoService;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, ClienteSpecification clienteSpecification,
                          ContatoService contatoService) {
        this.clienteRepository = clienteRepository;
        this.clienteSpecification = clienteSpecification;
        this.contatoService = contatoService;
    }

    @Transactional(readOnly = true)
    public Page<DadosCliente> findAllCliente(String nome, String cpf, LocalDate dataNascimento, Pageable pageable) {
        Page<Cliente> clientes = clienteRepository.findAll(clienteSpecification.filters(nome, cpf, dataNascimento),
                pageable);

        return clientes.map(cliente -> {
            DadosCliente dadosCliente = new DadosCliente();
            dadosCliente.dados(cliente);

            return dadosCliente;
        });
    }

    @Transactional(readOnly = true)
    public DadosCliente findByClienteId(Long clienteId) {
        var dadosCliente = new DadosCliente();

        return dadosCliente.dados(clienteRepository.findById(clienteId)
                .orElseThrow(() -> new MensagemException("Cliente não encontrado")));
    }

    @Transactional
    public DadosCliente create(DadosCliente dadosCliente) {
        if (isNull(dadosCliente.nome) || isNull(dadosCliente.cpf) || isNull(dadosCliente.dataNascimento)) {
            throw new MensagemException("Todos os campos são obrigatórios preencher!");
        }
        if (clienteRepository.existsByCpf(dadosCliente.getCpf())) {
            throw new MensagemException("Cliente já cadastrado por este Cpf!");
        }
        var cliente = new Cliente(dadosCliente);
        cliente.validarDadosCliente(dadosCliente);
        contatoService.create(cliente);

        save(cliente);
        return dadosCliente.dados(cliente);
    }

    @Transactional
    public DadosCliente update(DadosCliente dadosCliente) {
        var clienteRecuperado = clienteRepository.findById(dadosCliente.getId())
                .orElseThrow(() -> new MensagemException("Cliente não encontrado"));

        clienteRecuperado.setNome(dadosCliente.getNome());
        clienteRecuperado.setCpf(dadosCliente.getCpf());
        clienteRecuperado.setDataNascimento(dadosCliente.getDataNascimento());

        List<Contato> contatos = dadosCliente.getContatoList();
        for (Contato contato : contatos) {
            if (isNull(contato.getNome()) || isNull(contato.getTelefone()) || isNull(contato.getEmail())) {
                throw new MensagemException("Todos os campos do contato são de preenchimento obrigatório!");
            }
            contato.setCliente(clienteRecuperado);
        }
        clienteRecuperado.setContato(contatos);
        save(clienteRecuperado);

        return dadosCliente.dados(clienteRecuperado);
    }

    @Transactional
    public Boolean delete(Long clienteId) {
        var cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new MensagemException("Cliente não encontrado"));
        cliente.setAtivo(false);
        save(cliente);

        return cliente.getAtivo();
    }

    @Transactional
    public void save(Cliente cliente) {
        clienteRepository.save(cliente);
    }
}
