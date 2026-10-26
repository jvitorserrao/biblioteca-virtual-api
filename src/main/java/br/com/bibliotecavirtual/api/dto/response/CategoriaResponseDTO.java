package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.Categoria;
import lombok.Builder;

@Builder
public record CategoriaResponseDTO(
        Long id,
        String nome,
        String descricao,
        String areaConhecimento,
        Boolean ativa
) {
    public static CategoriaResponseDTO from(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .areaConhecimento(categoria.getAreaConhecimento())
                .ativa(categoria.getAtiva())
                .build();
    }
}
