package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.Editora;
import lombok.Builder;

@Builder
public record EditoraResponseDTO(
        Long id,
        String nome,
        String cnpj,
        String cidade,
        String site
) {
    public static EditoraResponseDTO from(Editora editora) {
        return EditoraResponseDTO.builder()
                .id(editora.getId())
                .nome(editora.getNome())
                .cnpj(editora.getCnpj())
                .cidade(editora.getCidade())
                .site(editora.getSite())
                .build();
    }
}
