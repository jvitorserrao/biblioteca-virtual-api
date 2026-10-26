package br.com.bibliotecavirtual.api.controller;

import br.com.bibliotecavirtual.api.dto.request.CategoriaRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.CategoriaResponseDTO;
import br.com.bibliotecavirtual.model.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Categorias dos livros e materiais digitais")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Lista todas as categorias")
    public List<CategoriaResponseDTO> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma categoria pelo ID")
    public CategoriaResponseDTO buscarPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma nova categoria")
    public CategoriaResponseDTO criar(@RequestBody @Valid CategoriaRequestDTO dto) {
        return categoriaService.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma categoria")
    public CategoriaResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO dto) {
        return categoriaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui uma categoria")
    public void excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
    }
}
