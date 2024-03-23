package com.thalitamartins.cadastroclientes.service;

import br.com.caelum.stella.validation.InvalidStateException;
import com.thalitamartins.cadastroclientes.domain.Cliente;
import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import com.thalitamartins.cadastroclientes.exception.MensagemException;
import com.thalitamartins.cadastroclientes.repository.ClienteRepository;
import com.thalitamartins.cadastroclientes.repository.ContatoRepository;
import com.thalitamartins.cadastroclientes.specification.ClienteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.com.caelum.stella.validation.CPFValidator;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;

import java.time.LocalDate;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteSpecification clienteSpecification;
    private final ContatoRepository contatoRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, ClienteSpecification clienteSpecification,
                          ContatoRepository contatoRepository) {
        this.clienteRepository = clienteRepository;
        this.clienteSpecification = clienteSpecification;
        this.contatoRepository = contatoRepository;
    }

    @Transactional(readOnly = true)
    public Page<DadosCliente> findAllCliente(String nome, String cpf, LocalDate dataNascimento, Pageable pageable) {
        var clientes = clienteRepository.findAll(clienteSpecification.filters(nome, cpf, dataNascimento), pageable);
        var dadosCliente = new DadosCliente();

        return clientes.map(dadosCliente::dados);
    }

    @Transactional(readOnly = true)
    public DadosCliente findByClienteId(Long clienteId) {
        var dadosCliente = new DadosCliente();

        return dadosCliente.dados(clienteRepository.findById(clienteId)
                .orElseThrow(() -> new MensagemException("Cliente não encontrado")));
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public DadosCliente create(DadosCliente dadosCliente) {
        validarDadosCliente(dadosCliente);
        var cliente = new Cliente(dadosCliente);

        cliente.getContato().stream()
                .peek(contato -> contato.setCliente(cliente))
                .peek(contato -> contato.setAtivo(true))
                .forEach(contatoRepository::save);
        save(cliente);
        return dadosCliente.dados(cliente);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public DadosCliente update(DadosCliente dadosCliente) {
        var cliente = clienteRepository.findById(dadosCliente.getId())
                .orElseThrow(() -> new MensagemException("Cliente não encontrado"));
        cliente.update(dadosCliente);
        save(cliente);

        return dadosCliente.dados(cliente);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean delete(Long dadosCliente) {
        var cliente = clienteRepository.findById(dadosCliente)
                .orElseThrow(() -> new MensagemException("Cliente não encontrado"));
        cliente.setAtivo(false);

        save(cliente);

        return cliente.getAtivo();
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

    public Boolean validarEmail(String email) {
        try {
            InternetAddress validar = new InternetAddress(email);
            validar.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(Cliente cliente) {
        clienteRepository.save(cliente);
    }
}
