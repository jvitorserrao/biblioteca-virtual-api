package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.Editora;

public record EditoraResponseDTO(
        Long id,
        String nome,
        String cnpj,
        String cidade,
        String site
) {
    public static EditoraResponseDTO from(Editora editora) {
        return new EditoraResponseDTO(
                editora.getId(),
                editora.getNome(),
                editora.getCnpj(),
                editora.getCidade(),
                editora.getSite()
        );
    }
}
