package com.thalitamartins.cadastroclientes.repository;

import com.thalitamartins.cadastroclientes.domain.Cliente;
import com.thalitamartins.cadastroclientes.dto.DadosCliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Page<Cliente> findAllByAtivoTrue(Pageable pageable);

}
