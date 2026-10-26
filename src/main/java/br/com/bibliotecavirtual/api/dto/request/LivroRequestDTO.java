package br.com.bibliotecavirtual.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

public record LivroRequestDTO(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
        String titulo,

        @NotBlank(message = "O ISBN é obrigatório")
        @Pattern(regexp = "\\d{13}", message = "O ISBN deve conter 13 dígitos, sem traços")
        @ISBN(message = "ISBN inválido")
        String isbn,

        @NotNull(message = "O ano de publicação é obrigatório")
        @Min(value = 1450, message = "O ano de publicação deve ser a partir de 1450")
        Integer anoPublicacao,

        @Positive(message = "O número de páginas deve ser maior que zero")
        Integer numeroPaginas,

        String sinopse,

        @URL(message = "O link de acesso deve ser uma URL válida começando com http:// ou https://")
        @Size(max = 500, message = "O link de acesso deve ter no máximo 500 caracteres")
        String linkAcesso,

        @NotNull(message = "O autor é obrigatório")
        Long autorId,

        @NotNull(message = "A editora é obrigatória")
        Long editoraId,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId
) {
}
