package com.thalitamartins.cadastroclientes.service;

import com.thalitamartins.cadastroclientes.domain.Cliente;
import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import com.thalitamartins.cadastroclientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService( ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public Page<Cliente> findAllPaciente(Pageable pageable) {
        return clienteRepository.findAllByAtivoTrue(pageable);
    }
}
