package br.com.bibliotecavirtual.model.service.impl;

import br.com.bibliotecavirtual.api.dto.request.CategoriaRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.CategoriaResponseDTO;
import br.com.bibliotecavirtual.model.entity.Categoria;
import br.com.bibliotecavirtual.model.exception.RecursoNaoEncontradoException;
import br.com.bibliotecavirtual.model.exception.RegraNegocioException;
import br.com.bibliotecavirtual.model.repository.CategoriaRepository;
import br.com.bibliotecavirtual.model.repository.LivroRepository;
import br.com.bibliotecavirtual.model.repository.MaterialDigitalRepository;
import br.com.bibliotecavirtual.model.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final LivroRepository livroRepository;
    private final MaterialDigitalRepository materialDigitalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        return CategoriaResponseDTO.from(buscarCategoria(id));
    }

    @Override
    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new RegraNegocioException("Já existe uma categoria com o nome " + dto.nome());
        }

        Categoria categoria = preencherDto(Categoria.builder(), dto);
        return CategoriaResponseDTO.from(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = buscarCategoria(id);

        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(dto.nome(), id)) {
            throw new RegraNegocioException("Já existe outra categoria com o nome " + dto.nome());
        }

        return CategoriaResponseDTO.from(categoriaRepository.save(preencherDto(categoria.toBuilder(), dto)));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscarCategoria(id);

        if (livroRepository.existsByCategoriaId(id)) {
            throw new RegraNegocioException("Não é possível excluir a categoria " + categoria.getNome() + " pois ela possui livros cadastrados");
        }

        if (materialDigitalRepository.existsByCategoriaId(id)) {
            throw new RegraNegocioException("Não é possível excluir a categoria " + categoria.getNome() + " pois ela possui materiais digitais cadastrados");
        }

        categoriaRepository.delete(categoria);
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com id " + id));
    }

    private Categoria preencherDto(Categoria.CategoriaBuilder builder, CategoriaRequestDTO dto) {
        return builder
                .nome(dto.nome())
                .descricao(dto.descricao())
                .areaConhecimento(dto.areaConhecimento())
                .ativa(dto.ativa() == null || dto.ativa())
                .build();
    }
}
