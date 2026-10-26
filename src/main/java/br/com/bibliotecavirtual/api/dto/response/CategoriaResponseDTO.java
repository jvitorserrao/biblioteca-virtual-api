package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.Categoria;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        String descricao,
        String areaConhecimento,
        Boolean ativa
) {
    public static CategoriaResponseDTO from(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getAreaConhecimento(),
                categoria.getAtiva()
        );
    }
}
