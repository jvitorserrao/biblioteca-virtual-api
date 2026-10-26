package br.com.bibliotecavirtual.model.service.impl;

import br.com.bibliotecavirtual.api.dto.request.MaterialDigitalRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.MaterialDigitalResponseDTO;
import br.com.bibliotecavirtual.model.entity.Categoria;
import br.com.bibliotecavirtual.model.entity.Livro;
import br.com.bibliotecavirtual.model.entity.MaterialDigital;
import br.com.bibliotecavirtual.model.exception.RecursoNaoEncontradoException;
import br.com.bibliotecavirtual.model.exception.RegraNegocioException;
import br.com.bibliotecavirtual.model.repository.CategoriaRepository;
import br.com.bibliotecavirtual.model.repository.LivroRepository;
import br.com.bibliotecavirtual.model.repository.MaterialDigitalRepository;
import br.com.bibliotecavirtual.model.service.MaterialDigitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialDigitalServiceImpl implements MaterialDigitalService {

    private final MaterialDigitalRepository materialDigitalRepository;
    private final CategoriaRepository categoriaRepository;
    private final LivroRepository livroRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaterialDigitalResponseDTO> listar() {
        return materialDigitalRepository.findAll().stream()
                .map(MaterialDigitalResponseDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialDigitalResponseDTO buscarPorId(Long id) {
        return MaterialDigitalResponseDTO.from(buscarMaterial(id));
    }

    @Override
    @Transactional
    public MaterialDigitalResponseDTO criar(MaterialDigitalRequestDTO dto) {
        MaterialDigital material = preencherDto(new MaterialDigital(), dto);
        return MaterialDigitalResponseDTO.from(materialDigitalRepository.save(material));
    }

    @Override
    @Transactional
    public MaterialDigitalResponseDTO atualizar(Long id, MaterialDigitalRequestDTO dto) {
        MaterialDigital material = preencherDto(buscarMaterial(id), dto);
        return MaterialDigitalResponseDTO.from(materialDigitalRepository.save(material));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        materialDigitalRepository.delete(buscarMaterial(id));
    }

    private MaterialDigital buscarMaterial(Long id) {
        return materialDigitalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Material digital não encontrado com id " + id));
    }

    private MaterialDigital preencherDto(MaterialDigital material, MaterialDigitalRequestDTO dto) {
        material.setTitulo(dto.titulo());
        material.setTipo(dto.tipo());
        material.setUrl(dto.url());
        material.setDescricao(dto.descricao());
        material.setSerie(dto.serie());
        material.setCategoria(buscarCategoriaAtiva(dto.categoriaId()));
        material.setLivro(dto.livroId() == null ? null : buscarLivro(dto.livroId()));
        return material;
    }

    private Categoria buscarCategoriaAtiva(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com id " + id));

        if (!categoria.getAtiva()) {
            throw new RegraNegocioException("A categoria " + categoria.getNome() + " está inativa e não pode receber materiais");
        }

        return categoria;
    }

    private Livro buscarLivro(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com id " + id));
    }
}
