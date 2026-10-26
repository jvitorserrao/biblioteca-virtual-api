package br.com.bibliotecavirtual.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    private String descricao;

    @Column(name = "area_conhecimento", nullable = false, length = 100)
    private String areaConhecimento;

    @Column(nullable = false)
    private Boolean ativa;
}
