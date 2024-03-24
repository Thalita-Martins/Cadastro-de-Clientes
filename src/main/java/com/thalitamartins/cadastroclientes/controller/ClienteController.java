package com.thalitamartins.cadastroclientes.controller;

import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import com.thalitamartins.cadastroclientes.exception.MensagemException;
import com.thalitamartins.cadastroclientes.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/listar")
    public Page<DadosCliente> findAllCliente(@RequestParam(value = "nome", required = false) String nome,
                                             @RequestParam(value = "cpf", required = false) String cpf,
                                             @RequestParam(value = "dataNascimento", required = false) LocalDate dataNascimento,
                                             Pageable pageable) {
        return clienteService.findAllCliente(nome, cpf, dataNascimento, pageable);
    }

    @GetMapping("/listar/{clienteId}")
    public ResponseEntity findByClienteId(@PathVariable(name = "clienteId") Long clienteId) {
        try {
            var cliente = clienteService.findByClienteId(clienteId);
            return ResponseEntity.ok(cliente);
        } catch (MensagemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity create(@RequestBody DadosCliente dadosCliente) {
        try {
            var cliente = clienteService.create(dadosCliente);
            return ResponseEntity.ok(cliente);
        } catch (MensagemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity update(@RequestBody DadosCliente dadosCliente) {
        try {
            var cliente = clienteService.update(dadosCliente);
            return ResponseEntity.ok(cliente);
        } catch (MensagemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("deletar/{clienteId}")
    public ResponseEntity delete(@PathVariable Long clienteId) {
        try {
            var cliente = clienteService.delete(clienteId);
            return ResponseEntity.ok(cliente);
        } catch (MensagemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
