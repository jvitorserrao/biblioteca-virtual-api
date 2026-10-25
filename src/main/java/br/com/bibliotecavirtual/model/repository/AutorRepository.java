package br.com.bibliotecavirtual.model.repository;

import br.com.bibliotecavirtual.model.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
