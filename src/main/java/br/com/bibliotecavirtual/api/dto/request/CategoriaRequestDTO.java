package br.com.bibliotecavirtual.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String descricao,

        @NotBlank(message = "A área do conhecimento é obrigatória")
        @Size(max = 100, message = "A área do conhecimento deve ter no máximo 100 caracteres")
        String areaConhecimento,

        Boolean ativa
) {
}
