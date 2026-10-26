package br.com.bibliotecavirtual.model.service;

import br.com.bibliotecavirtual.api.dto.request.CategoriaRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {

    List<CategoriaResponseDTO> listar();

    CategoriaResponseDTO buscarPorId(Long id);

    CategoriaResponseDTO criar(CategoriaRequestDTO dto);

    CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto);

    void excluir(Long id);
}
