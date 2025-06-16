package com.emiteai.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.emiteai.api.model.entity.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    boolean existsByCpf(String cpf);
    Optional<Pessoa> findByCpf(String cpf);
}
