package br.com.bibliotecavirtual.model.repository;

import br.com.bibliotecavirtual.model.entity.MaterialDigital;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialDigitalRepository extends JpaRepository<MaterialDigital, Long> {

    @Override
    @EntityGraph(attributePaths = {"categoria", "livro"})
    List<MaterialDigital> findAll();

    @Override
    @EntityGraph(attributePaths = {"categoria", "livro"})
    Optional<MaterialDigital> findById(Long id);

    boolean existsByCategoriaId(Long categoriaId);

    boolean existsByLivroId(Long livroId);
}
