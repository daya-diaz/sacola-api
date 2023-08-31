package me.dio.sacola.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private double valorUnitario;
    @Builder.Default // Usado para deixar o atributo 'disponivel' como true por padrão.
    private Boolean disponivel = true;
    @ManyToOne //Definindo a relação: Um Restaurante tem vários Produtos
    @JsonIgnore
    private Restaurante restaurante; //Atributo que associa o produto a um restaurante
}
