package me.dio.sacola.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@Entity //A classe Restaurante vai ser convertida para uma tabela la no banco de dados relacional;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;

    @OneToMany(cascade = CascadeType.ALL) //um Restaurante vai ter varios (Produto)s
    private List<Produto> cardapio;

    @Embedded //A anotação @Embedded é uma anotação JPA (Java Persistence API) usada para
              // indicar que uma entidade deve ser incorporada em outra entidade.
              //@Embedded no JPA: Atributos de uma classe são considerados parte da entidade que a inclui.
    private Endereco endereco;

}
