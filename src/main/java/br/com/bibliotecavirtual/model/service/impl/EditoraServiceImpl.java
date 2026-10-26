package br.com.bibliotecavirtual.model.service.impl;

import br.com.bibliotecavirtual.api.dto.request.EditoraRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.EditoraResponseDTO;
import br.com.bibliotecavirtual.model.entity.Editora;
import br.com.bibliotecavirtual.model.exception.RecursoNaoEncontradoException;
import br.com.bibliotecavirtual.model.exception.RegraNegocioException;
import br.com.bibliotecavirtual.model.repository.EditoraRepository;
import br.com.bibliotecavirtual.model.repository.LivroRepository;
import br.com.bibliotecavirtual.model.service.EditoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditoraServiceImpl implements EditoraService {

    private final EditoraRepository editoraRepository;
    private final LivroRepository livroRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EditoraResponseDTO> listar() {
        return editoraRepository.findAll().stream()
                .map(EditoraResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EditoraResponseDTO buscarPorId(Long id) {
        return EditoraResponseDTO.from(buscarEditora(id));
    }

    @Override
    @Transactional
    public EditoraResponseDTO criar(EditoraRequestDTO dto) {
        if (editoraRepository.existsByCnpj(dto.cnpj())) {
            throw new RegraNegocioException("Já existe uma editora cadastrada com o CNPJ " + dto.cnpj());
        }

        Editora editora = preencherDto(Editora.builder(), dto);
        return EditoraResponseDTO.from(editoraRepository.save(editora));
    }

    @Override
    @Transactional
    public EditoraResponseDTO atualizar(Long id, EditoraRequestDTO dto) {
        Editora editora = buscarEditora(id);

        if (editoraRepository.existsByCnpjAndIdNot(dto.cnpj(), id)) {
            throw new RegraNegocioException("Já existe outra editora cadastrada com o CNPJ " + dto.cnpj());
        }

        return EditoraResponseDTO.from(editoraRepository.save(preencherDto(editora.toBuilder(), dto)));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Editora editora = buscarEditora(id);

        if (livroRepository.existsByEditoraId(id)) {
            throw new RegraNegocioException("Não é possível excluir a editora " + editora.getNome() + " pois ela possui livros cadastrados");
        }

        editoraRepository.delete(editora);
    }

    private Editora buscarEditora(Long id) {
        return editoraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Editora não encontrada com id " + id));
    }

    private Editora preencherDto(Editora.EditoraBuilder builder, EditoraRequestDTO dto) {
        return builder
                .nome(dto.nome())
                .cnpj(dto.cnpj())
                .cidade(dto.cidade())
                .site(dto.site())
                .build();
    }
}
