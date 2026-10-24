package com.example.clientes.controller;

import com.example.clientes.model.Cliente;
import com.example.clientes.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@Valid @RequestBody Cliente cliente) {
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf) {

        List<Cliente> clientes;

        if (nome != null && !nome.isEmpty() && cpf != null && !cpf.isEmpty()) {
            clientes = clienteRepository.findByNomeContainingIgnoreCaseAndCpfContaining(nome, cpf);
        } else if (nome != null && !nome.isEmpty()) {
            clientes = clienteRepository.findByNomeContainingIgnoreCase(nome);
        } else if (cpf != null && !cpf.isEmpty()) {
            clientes = clienteRepository.findByCpfContaining(cpf);
        } else {
            clientes = clienteRepository.findAll();
        }

        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteAtualizado) {
        return clienteRepository.findById(id)
                .map(clienteExistente -> {
                    clienteExistente.setNome(clienteAtualizado.getNome());
                    clienteExistente.setCpf(clienteAtualizado.getCpf());
                    clienteExistente.setTelefone(clienteAtualizado.getTelefone());
                    clienteExistente.setCep(clienteAtualizado.getCep());
                    clienteExistente.setCidade(clienteAtualizado.getCidade());
                    clienteExistente.setEstado(clienteAtualizado.getEstado());
                    clienteExistente.setEndereco(clienteAtualizado.getEndereco());
                    clienteExistente.setBairro(clienteAtualizado.getBairro());
                    clienteExistente.setNumero(clienteAtualizado.getNumero());
                    clienteExistente.setComplemento(clienteAtualizado.getComplemento());

                    Cliente salvo = clienteRepository.save(clienteExistente);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarCliente(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setAtivo(false);
                    clienteRepository.save(cliente);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}