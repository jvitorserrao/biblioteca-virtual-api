package br.com.bibliotecavirtual.model.service;

import br.com.bibliotecavirtual.api.dto.request.MaterialDigitalRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.MaterialDigitalResponseDTO;

import java.util.List;

public interface MaterialDigitalService {

    List<MaterialDigitalResponseDTO> listar();

    MaterialDigitalResponseDTO buscarPorId(Long id);

    MaterialDigitalResponseDTO criar(MaterialDigitalRequestDTO dto);

    MaterialDigitalResponseDTO atualizar(Long id, MaterialDigitalRequestDTO dto);

    void excluir(Long id);
}
