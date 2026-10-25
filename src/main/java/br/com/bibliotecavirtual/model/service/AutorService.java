package br.com.bibliotecavirtual.model.service;

import br.com.bibliotecavirtual.api.dto.request.AutorRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.AutorResponseDTO;

import java.util.List;

public interface AutorService {

    List<AutorResponseDTO> listar();

    AutorResponseDTO buscarPorId(Long id);

    AutorResponseDTO criar(AutorRequestDTO dto);

    AutorResponseDTO atualizar(Long id, AutorRequestDTO dto);

    void excluir(Long id);
}
