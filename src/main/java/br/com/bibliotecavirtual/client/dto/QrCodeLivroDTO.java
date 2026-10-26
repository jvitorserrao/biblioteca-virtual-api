package br.com.bibliotecavirtual.client.dto;

import lombok.Builder;

@Builder
public record QrCodeLivroDTO(
        String titulo,
        String autor,
        String editora,
        Integer anoPublicacao,
        String isbn,
        String linkAcesso
) {
    public String conteudo() {
        String conteudo = """
                Título: %s
                Autor: %s
                Editora: %s
                Ano: %d
                ISBN: %s""".formatted(titulo, autor, editora, anoPublicacao, isbn);

        return linkAcesso == null ? conteudo : conteudo + "\nAcesse: " + linkAcesso;
    }
}
