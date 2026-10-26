package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.Livro;
import lombok.Builder;

@Builder
public record LivroResponseDTO(
        Long id,
        String titulo,
        String isbn,
        Integer anoPublicacao,
        Integer numeroPaginas,
        String sinopse,
        String linkAcesso,
        ReferenciaResponseDTO autor,
        ReferenciaResponseDTO editora,
        ReferenciaResponseDTO categoria,
        String qrCode
) {
    public static LivroResponseDTO from(Livro livro) {
        return LivroResponseDTO.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .isbn(livro.getIsbn())
                .anoPublicacao(livro.getAnoPublicacao())
                .numeroPaginas(livro.getNumeroPaginas())
                .sinopse(livro.getSinopse())
                .linkAcesso(livro.getLinkAcesso())
                .autor(ReferenciaResponseDTO.builder()
                        .id(livro.getAutor().getId())
                        .nome(livro.getAutor().getNome())
                        .build())
                .editora(ReferenciaResponseDTO.builder()
                        .id(livro.getEditora().getId())
                        .nome(livro.getEditora().getNome())
                        .build())
                .categoria(ReferenciaResponseDTO.builder()
                        .id(livro.getCategoria().getId())
                        .nome(livro.getCategoria().getNome())
                        .build())
                .qrCode(livro.getQrCode() == null ? null : "data:image/png;base64," + livro.getQrCode())
                .build();
    }
}
