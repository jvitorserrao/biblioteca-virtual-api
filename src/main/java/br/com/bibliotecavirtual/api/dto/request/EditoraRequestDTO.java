package br.com.bibliotecavirtual.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.br.CNPJ;

public record EditoraRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter 14 dígitos, sem pontos ou traços")
        @CNPJ(message = "CNPJ inválido")
        String cnpj,

        @NotBlank(message = "A cidade é obrigatória")
        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
        String cidade,

        @URL(message = "O site deve ser uma URL válida começando com http:// ou https:// (ex.: https://www.editora.com.br)")
        @Size(max = 255, message = "O site deve ter no máximo 255 caracteres")
        String site
) {
}
