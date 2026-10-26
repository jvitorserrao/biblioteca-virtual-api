package br.com.bibliotecavirtual.api.dto.response;

import lombok.Builder;

@Builder
public record ReferenciaResponseDTO(
        Long id,
        String nome
) {
}
