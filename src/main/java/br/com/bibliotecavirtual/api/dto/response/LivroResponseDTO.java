package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.Livro;

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
        ReferenciaResponseDTO categoria
) {
    public static LivroResponseDTO from(Livro livro) {
        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getNumeroPaginas(),
                livro.getSinopse(),
                livro.getLinkAcesso(),
                new ReferenciaResponseDTO(livro.getAutor().getId(), livro.getAutor().getNome()),
                new ReferenciaResponseDTO(livro.getEditora().getId(), livro.getEditora().getNome()),
                new ReferenciaResponseDTO(livro.getCategoria().getId(), livro.getCategoria().getNome())
        );
    }
}
