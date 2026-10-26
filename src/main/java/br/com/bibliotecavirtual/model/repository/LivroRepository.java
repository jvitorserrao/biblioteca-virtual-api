package br.com.bibliotecavirtual.model.repository;

import br.com.bibliotecavirtual.model.entity.Livro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Override
    @EntityGraph(attributePaths = {"autor", "editora", "categoria"})
    List<Livro> findAll();

    @Override
    @EntityGraph(attributePaths = {"autor", "editora", "categoria"})
    Optional<Livro> findById(Long id);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);

    boolean existsByAutorId(Long autorId);

    boolean existsByEditoraId(Long editoraId);

    boolean existsByCategoriaId(Long categoriaId);
}
