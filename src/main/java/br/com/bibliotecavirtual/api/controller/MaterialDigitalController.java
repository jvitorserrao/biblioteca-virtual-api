package br.com.bibliotecavirtual.api.controller;

import br.com.bibliotecavirtual.api.dto.request.MaterialDigitalRequestDTO;
import br.com.bibliotecavirtual.api.dto.response.MaterialDigitalResponseDTO;
import br.com.bibliotecavirtual.model.service.MaterialDigitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materiais")
@RequiredArgsConstructor
@Tag(name = "Materiais digitais", description = "Apostilas, vídeos, PDFs e links de apoio aos estudantes")
public class MaterialDigitalController {

    private final MaterialDigitalService materialDigitalService;

    @GetMapping
    @Operation(summary = "Lista todos os materiais digitais")
    public List<MaterialDigitalResponseDTO> listar() {
        return materialDigitalService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um material digital pelo ID")
    public MaterialDigitalResponseDTO buscarPorId(@PathVariable Long id) {
        return materialDigitalService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um novo material digital")
    public MaterialDigitalResponseDTO criar(@RequestBody @Valid MaterialDigitalRequestDTO dto) {
        return materialDigitalService.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um material digital")
    public MaterialDigitalResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid MaterialDigitalRequestDTO dto) {
        return materialDigitalService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um material digital")
    public void excluir(@PathVariable Long id) {
        materialDigitalService.excluir(id);
    }
}
