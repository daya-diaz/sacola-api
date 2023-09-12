package me.dio.sacola.model; // Pacote onde a classe está localizada

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;

import javax.persistence.*; 
import java.util.List;

@AllArgsConstructor 
@Builder
@Data 
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@Entity 
@NoArgsConstructor 

public class Sacola {
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) 
    private Cliente cliente; 

    @OneToMany(cascade = CascadeType.ALL) 
    private List<Item> itens; 

    private Double valorTotal; 

    @Enumerated 
    private FormaPagamento FormaPagamento; 

    private boolean fechada;
}

//getters and setters