package br.com.bibliotecavirtual.api.controller;

import br.com.bibliotecavirtual.api.dto.request.LivroRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.LivroResponseDTO;
import br.com.bibliotecavirtual.model.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "Cadastro de livros do acervo")
public class LivroController {

    private final LivroService livroService;

    @GetMapping
    @Operation(summary = "Lista todos os livros")
    public List<LivroResponseDTO> listar() {
        return livroService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um livro pelo ID")
    public LivroResponseDTO buscarPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um novo livro")
    public LivroResponseDTO criar(@RequestBody @Valid LivroRequestDTO dto) {
        return livroService.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um livro")
    public LivroResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid LivroRequestDTO dto) {
        return livroService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um livro")
    public void excluir(@PathVariable Long id) {
        livroService.excluir(id);
    }

    @GetMapping("/{id}/qrcode")
    @Operation(summary = "Retorna a imagem PNG do QR Code com as informações do livro")
    @ApiResponse(responseCode = "200", description = "Imagem do QR Code",
            content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE))
    public ResponseEntity<byte[]> buscarQrCode(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(livroService.buscarQrCode(id));
    }
}
