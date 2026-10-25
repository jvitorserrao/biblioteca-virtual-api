package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.Autor;

import java.time.LocalDate;

public record AutorResponseDTO(
        Long id,
        String nome,
        String nacionalidade,
        LocalDate dataNascimento,
        String biografia
) {
    public static AutorResponseDTO from(Autor autor) {
        return new AutorResponseDTO(
                autor.getId(),
                autor.getNome(),
                autor.getNacionalidade(),
                autor.getDataNascimento(),
                autor.getBiografia()
        );
    }
}
