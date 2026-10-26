package br.com.bibliotecavirtual.model.service;

import br.com.bibliotecavirtual.api.dto.request.EditoraRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.EditoraResponseDTO;

import java.util.List;

public interface EditoraService {

    List<EditoraResponseDTO> listar();

    EditoraResponseDTO buscarPorId(Long id);

    EditoraResponseDTO criar(EditoraRequestDTO dto);

    EditoraResponseDTO atualizar(Long id, EditoraRequestDTO dto);

    void excluir(Long id);
}
