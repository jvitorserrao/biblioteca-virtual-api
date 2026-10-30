package br.com.bibliotecavirtual.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class QrCodeClient {

    private final WebClient qrCodeWebClient;

    public Optional<byte[]> gerar(String conteudo) {
        try {
            byte[] imagem = qrCodeWebClient.get()
                    .uri(uri -> uri.path("/create-qr-code/")
                            .queryParam("size", "300x300")
                            .queryParam("format", "png")
                            .queryParam("data", "{data}")
                            .build(conteudo))
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

            return Optional.ofNullable(imagem);
        } catch (Exception ex) {
            log.warn("Não foi possível gerar o QR Code na API goqr.me: {}", ex.getMessage());
            return Optional.empty();
        }
    }
}
