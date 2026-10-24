package com.example.clientes.repository;

import com.example.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    List<Cliente> findByCpfContaining(String cpf);

    List<Cliente> findByNomeContainingIgnoreCaseAndCpfContaining(String nome, String cpf);
}