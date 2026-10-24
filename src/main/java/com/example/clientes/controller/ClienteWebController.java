package com.example.clientes.controller;

import com.example.clientes.model.Cliente;
import com.example.clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteWebController {

    @Autowired
    private ClienteRepository clienteRepository;


    @GetMapping
    public String listarClientes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            Model model) {

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

        model.addAttribute("clientes", clientes);
        model.addAttribute("nome", nome);
        model.addAttribute("cpf", cpf);
        return "index";
    }


    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "form";
    }


    @PostMapping("/salvar")
    public String salvarCliente(Cliente cliente) {
        if (cliente.getAtivo() == null) {
            cliente.setAtivo(true);
        }
        clienteRepository.save(cliente);
        return "redirect:/clientes";
    }


    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        model.addAttribute("cliente", cliente);
        return "form";
    }


    @GetMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id) {
        clienteRepository.findById(id).ifPresent(cliente -> {
            cliente.setAtivo(false);
            clienteRepository.save(cliente);
        });
        return "redirect:/clientes";
    }
}