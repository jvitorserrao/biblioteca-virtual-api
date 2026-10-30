package br.com.bibliotecavirtual.client;

import br.com.bibliotecavirtual.model.exception.IntegracaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArmazenamentoClient {

    private final WebClient supabaseWebClient;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    public String enviar(String nomeArquivo, byte[] conteudo, MediaType tipo) {
        if (supabaseUrl.isBlank() || supabaseKey.isBlank()) {
            throw new IntegracaoException("Armazenamento de arquivos não configurado. Defina SUPABASE_URL e SUPABASE_KEY.");
        }

        try {
            supabaseWebClient.post()
                    .uri("/object/{bucket}/{nome}", bucket, nomeArquivo)
                    .contentType(tipo)
                    .header("x-upsert", "true")
                    .header("cache-control", "max-age=60")
                    .bodyValue(conteudo)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception ex) {
            log.error("Falha ao enviar o arquivo {} para o Supabase: {}", nomeArquivo, ex.getMessage());
            throw new IntegracaoException("Não foi possível enviar o arquivo para o armazenamento. Tente novamente mais tarde.");
        }

        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + nomeArquivo;
    }

    public void remover(String urlArquivo) {
        String nomeArquivo = urlArquivo.substring(urlArquivo.lastIndexOf('/') + 1);

        try {
            supabaseWebClient.delete()
                    .uri("/object/{bucket}/{nome}", bucket, nomeArquivo)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception ex) {
            log.warn("Não foi possível remover o arquivo {} do Supabase: {}", nomeArquivo, ex.getMessage());
        }
    }
}
