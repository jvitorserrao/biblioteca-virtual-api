package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.MaterialDigital;

public record MaterialDigitalResponseDTO(
        Long id,
        String titulo,
        String tipo,
        String url,
        String descricao,
        Integer serie,
        ReferenciaResponseDTO categoria,
        ReferenciaResponseDTO livro
) {
    public static MaterialDigitalResponseDTO from(MaterialDigital material) {
        ReferenciaResponseDTO livro = material.getLivro() == null
                ? null
                : new ReferenciaResponseDTO(material.getLivro().getId(), material.getLivro().getTitulo());

        return new MaterialDigitalResponseDTO(
                material.getId(),
                material.getTitulo(),
                material.getTipo(),
                material.getUrl(),
                material.getDescricao(),
                material.getSerie(),
                new ReferenciaResponseDTO(material.getCategoria().getId(), material.getCategoria().getNome()),
                livro
        );
    }
}
