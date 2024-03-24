package com.thalitamartins.cadastroclientes.service;

import com.thalitamartins.cadastroclientes.domain.Cliente;
import com.thalitamartins.cadastroclientes.domain.Contato;
import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import com.thalitamartins.cadastroclientes.exception.MensagemException;
import com.thalitamartins.cadastroclientes.repository.ClienteRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ContatoService contatoService;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void testCreateCliente() {

        DadosCliente dadosCliente = new DadosCliente();
        dadosCliente.setNome("Cliente Teste");
        dadosCliente.setCpf("40663315050");
        dadosCliente.setDataNascimento(LocalDate.of(1989, 5, 11));

        Contato contato = new Contato();
        contato.setNome("contato teste");
        contato.setTelefone("988457126");
        contato.setEmail("contatoteste@teste.com.br");
        dadosCliente.setContatoList(Collections.singletonList(contato));

        DadosCliente clienteCriado = clienteService.create(dadosCliente);
        assertNotNull(clienteCriado);
    }

    @Test
    void testCreateClienteSemCamposObrigatorios() {
        DadosCliente dadosCliente = new DadosCliente();
        assertThrows(MensagemException.class, () -> clienteService.create(dadosCliente));
    }

    @Test
    void testCreateClienteCpfDuplicado() {
        DadosCliente dadosCliente = new DadosCliente();
        dadosCliente.setNome("Cliente Teste");
        dadosCliente.setCpf("40663315050");
        dadosCliente.setDataNascimento(LocalDate.of(1975, 12, 4));

        clienteRepository.save(new Cliente(dadosCliente));
        assertThrows(MensagemException.class, () -> clienteService.create(dadosCliente));
    }
}
