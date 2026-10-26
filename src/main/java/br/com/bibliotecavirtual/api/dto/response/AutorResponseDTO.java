package br.com.bibliotecavirtual.api.dto.response;

import br.com.bibliotecavirtual.model.entity.Autor;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AutorResponseDTO(
        Long id,
        String nome,
        String nacionalidade,
        LocalDate dataNascimento,
        String biografia
) {
    public static AutorResponseDTO from(Autor autor) {
        return AutorResponseDTO.builder()
                .id(autor.getId())
                .nome(autor.getNome())
                .nacionalidade(autor.getNacionalidade())
                .dataNascimento(autor.getDataNascimento())
                .biografia(autor.getBiografia())
                .build();
    }
}
