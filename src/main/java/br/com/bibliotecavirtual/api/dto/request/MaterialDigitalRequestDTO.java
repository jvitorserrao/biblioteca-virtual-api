package br.com.bibliotecavirtual.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record MaterialDigitalRequestDTO(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
        String titulo,

        @NotBlank(message = "O tipo é obrigatório")
        @Pattern(regexp = "PDF|VIDEO|LINK|APOSTILA", message = "O tipo deve ser PDF, VIDEO, LINK ou APOSTILA")
        String tipo,

        @NotBlank(message = "A URL é obrigatória")
        @URL(message = "A URL deve ser válida e começar com http:// ou https://")
        @Size(max = 500, message = "A URL deve ter no máximo 500 caracteres")
        String url,

        String descricao,

        @NotNull(message = "A série é obrigatória")
        @Min(value = 1, message = "A série deve ser 1, 2 ou 3")
        @Max(value = 3, message = "A série deve ser 1, 2 ou 3")
        Integer serie,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId,

        Long livroId
) {
}
