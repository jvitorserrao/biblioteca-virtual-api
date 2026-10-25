package br.com.bibliotecavirtual.api.controller;

import br.com.bibliotecavirtual.api.dto.request.AutorRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.AutorResponseDTO;
import br.com.bibliotecavirtual.model.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Cadastro de autores dos livros")
public class AutorController {

    private final AutorService autorService;

    @GetMapping
    @Operation(summary = "Lista todos os autores")
    public List<AutorResponseDTO> listar() {
        return autorService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um autor pelo ID")
    public AutorResponseDTO buscarPorId(@PathVariable Long id) {
        return autorService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um novo autor")
    public AutorResponseDTO criar(@RequestBody @Valid AutorRequestDTO dto) {
        return autorService.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um autor")
    public AutorResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid AutorRequestDTO dto) {
        return autorService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um autor")
    public void excluir(@PathVariable Long id) {
        autorService.excluir(id);
    }
}
