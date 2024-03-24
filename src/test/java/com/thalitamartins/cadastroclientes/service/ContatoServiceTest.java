package com.thalitamartins.cadastroclientes.service;

import com.thalitamartins.cadastroclientes.domain.Cliente;
import com.thalitamartins.cadastroclientes.domain.Contato;
import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import com.thalitamartins.cadastroclientes.exception.MensagemException;
import com.thalitamartins.cadastroclientes.repository.ContatoRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    public void testCreateContato() {
        DadosCliente dadosCliente = new DadosCliente();
        Contato contato = new Contato();
        contato.setNome("João");
        contato.setTelefone("123456789");
        contato.setEmail("joao@example.com");
        dadosCliente.setContatoList(Collections.singletonList(contato));
        Cliente cliente = new Cliente(dadosCliente);

        ContatoRepository contatoRepository = mock(ContatoRepository.class);
        ContatoService contatoService = new ContatoService(contatoRepository);
        contatoService.create(cliente);
        verify(contatoRepository, times(1)).save(contato);
    }

    @Test
    public void testCreateContatoSemCamposObrigatorios() {
        DadosCliente dadosCliente = new DadosCliente();
        Contato contato = new Contato();
        dadosCliente.setContatoList(Collections.singletonList(contato));
        Cliente cliente = new Cliente(dadosCliente);

        ContatoRepository contatoRepository = mock(ContatoRepository.class);
        ContatoService contatoService = new ContatoService(contatoRepository);
        assertThrows(MensagemException.class, () -> contatoService.create(cliente));
        verify(contatoRepository, never()).save(any());
    }
}
