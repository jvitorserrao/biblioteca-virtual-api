package br.com.bibliotecavirtual.model.service.impl;

import br.com.bibliotecavirtual.api.dto.request.AutorRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.AutorResponseDTO;
import br.com.bibliotecavirtual.model.entity.Autor;
import br.com.bibliotecavirtual.model.exception.RecursoNaoEncontradoException;
import br.com.bibliotecavirtual.model.exception.RegraNegocioException;
import br.com.bibliotecavirtual.model.repository.AutorRepository;
import br.com.bibliotecavirtual.model.repository.LivroRepository;
import br.com.bibliotecavirtual.model.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listar() {
        return autorRepository.findAll().stream()
                .map(AutorResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(Long id) {
        return AutorResponseDTO.from(buscarAutor(id));
    }

    @Override
    @Transactional
    public AutorResponseDTO criar(AutorRequestDTO dto) {
        Autor autor = preencherDto(new Autor(), dto);
        return AutorResponseDTO.from(autorRepository.save(autor));
    }

    @Override
    @Transactional
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor autor = preencherDto(buscarAutor(id), dto);
        return AutorResponseDTO.from(autorRepository.save(autor));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Autor autor = buscarAutor(id);

        if (livroRepository.existsByAutorId(id)) {
            throw new RegraNegocioException("Não é possível excluir o autor " + autor.getNome() + " pois existem livros vinculados a ele");
        }

        autorRepository.delete(autor);
    }

    private Autor buscarAutor(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado com id " + id));
    }

    private Autor preencherDto(Autor autor, AutorRequestDTO dto) {
        autor.setNome(dto.nome());
        autor.setNacionalidade(dto.nacionalidade());
        autor.setDataNascimento(dto.dataNascimento());
        autor.setBiografia(dto.biografia());
        return autor;
    }
}
