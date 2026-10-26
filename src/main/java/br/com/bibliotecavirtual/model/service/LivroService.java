package br.com.bibliotecavirtual.model.service;

import br.com.bibliotecavirtual.api.dto.request.LivroRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.LivroResponseDTO;

import java.util.List;

public interface LivroService {

    List<LivroResponseDTO> listar();

    LivroResponseDTO buscarPorId(Long id);

    LivroResponseDTO criar(LivroRequestDTO dto);

    LivroResponseDTO atualizar(Long id, LivroRequestDTO dto);

    void excluir(Long id);

    byte[] buscarQrCode(Long id);
}
