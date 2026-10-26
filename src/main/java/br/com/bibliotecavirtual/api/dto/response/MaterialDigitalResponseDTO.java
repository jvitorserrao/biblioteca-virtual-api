package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.MaterialDigital;
import lombok.Builder;

@Builder
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
                : ReferenciaResponseDTO.builder()
                        .id(material.getLivro().getId())
                        .nome(material.getLivro().getTitulo())
                        .build();

        return MaterialDigitalResponseDTO.builder()
                .id(material.getId())
                .titulo(material.getTitulo())
                .tipo(material.getTipo())
                .url(material.getUrl())
                .descricao(material.getDescricao())
                .serie(material.getSerie())
                .categoria(ReferenciaResponseDTO.builder()
                        .id(material.getCategoria().getId())
                        .nome(material.getCategoria().getNome())
                        .build())
                .livro(livro)
                .build();
    }
}
