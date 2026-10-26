package br.com.bibliotecavirtual.api.controller;

import br.com.bibliotecavirtual.api.dto.request.EditoraRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.EditoraResponseDTO;
import br.com.bibliotecavirtual.model.service.EditoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/editoras")
@RequiredArgsConstructor
@Tag(name = "Editoras", description = "Cadastro de editoras dos livros")
public class EditoraController {

    private final EditoraService editoraService;

    @GetMapping
    @Operation(summary = "Lista todas as editoras")
    public List<EditoraResponseDTO> listar() {
        return editoraService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma editora pelo ID")
    public EditoraResponseDTO buscarPorId(@PathVariable Long id) {
        return editoraService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma nova editora")
    public EditoraResponseDTO criar(@RequestBody @Valid EditoraRequestDTO dto) {
        return editoraService.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma editora")
    public EditoraResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid EditoraRequestDTO dto) {
        return editoraService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui uma editora")
    public void excluir(@PathVariable Long id) {
        editoraService.excluir(id);
    }
}
