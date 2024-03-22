package com.thalitamartins.cadastroclientes.controller;

import com.thalitamartins.cadastroclientes.domain.Cliente;
import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import com.thalitamartins.cadastroclientes.exception.MensagemException;
import com.thalitamartins.cadastroclientes.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/listar")
    public Page<Cliente> findAllCliente(@PageableDefault(size = 10, sort = "nome", value = 10) Pageable pageable) {
        return clienteService.findAllPaciente(pageable);
    }

    @GetMapping("/listar/{clienteId}")
    public ResponseEntity findByClienteId(@PathVariable(name = "clienteId") Long clienteId) {
        try {
            var cliente = 1;
            return ResponseEntity.ok(cliente);
        } catch (MensagemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity create(@RequestBody DadosCliente dados) {
        try {
            var cliente = 1;
            return ResponseEntity.ok(cliente);
        } catch (MensagemException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity update(@RequestBody DadosCliente dados) {
        try {
            var cliente = 1;
            return ResponseEntity.ok(cliente);
        } catch (MensagemException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("deletar/{clienteId}")
    public ResponseEntity delete(@PathVariable Long clienteId){
        try {
            var cliente = 1;
            return ResponseEntity.ok(cliente);
        } catch (MensagemException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
