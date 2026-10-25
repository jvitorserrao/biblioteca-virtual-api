package br.com.bibliotecavirtual.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AutorRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "A nacionalidade é obrigatória")
        @Size(max = 80, message = "A nacionalidade deve ter no máximo 80 caracteres")
        String nacionalidade,

        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        String biografia
) {
}
