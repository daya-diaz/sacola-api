package me.dio.sacola.model; // Pacote onde a classe está localizada

import com.fasterxml.jackson.annotation.JsonIgnore; // Importação de classes de outros pacotes
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;

import javax.persistence.*; // Importações relacionadas à persistência de dados
import java.util.List;

import javax.persistence.*;

@AllArgsConstructor // cria um constructor com todas os atributos;
@Builder //designpatters que ajuda na hora de criar os objetos da Classe.
@Data //trás todos os getters e setters dos atributos que tinha feito na mão;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //ignora erros quando hibernate
                                                                // ta trabalhando c atributos Lazy
@Entity // Declaração da classe como uma entidade JPA (é uma classe Java que
        // está mapeada para uma tabela em um banco de dados relacional.)
@NoArgsConstructor // cria um constructor sem argumentos/atributos (o hibernate precisa);

public class Sacola {
    @Id // Marca o campo 'id' como chave primária (conceito importante em bancos de dados relacionais
        // que serve para identificar de forma única cada registro (linha) em uma tabela. )
    @GeneratedValue(strategy = GenerationType.AUTO) // Define a geração automática do valor 'id' / AUTO -> autoincrement
    private Long id; // Campo que representa a chave primária
    @ManyToOne(fetch = FetchType.LAZY, optional = false) //  representa um relacionamento
    // muitos-para-um entre a entidade Sacola e a entidade Cliente.
    //Uma sacola pertence a um único cliente.
    //Um cliente pode ter muitas sacolas.

    // (fetch = FetchType.LAZY) Define como os dados relacionados são carregados. Nesse caso, LAZY indica que o
    // carregamento será tardio, os dados do cliente não serão carregados automaticamente
    // a menos que você os acesse explicitamente.
    //  optional = false -> Isso significa que o campo cliente não pode ser nulo. (é obrigatório)

    @JsonIgnore // Não incluirá 'cliente' na serialização JSON
    private Cliente cliente; //  Aqui é onde o campo cliente é declarado como uma propriedade na classe Sacola.
    @OneToMany(cascade = CascadeType.ALL) // Relacionamento um-para-muitos
    private List<Item> itens; // Campo que representa o relacionamento com 'Item'
    private Double valorTotal; // Campo para armazenar o valor total da sacola
    @Enumerated // Indica que 'FormaPagamento' é uma enumeração
                // Usar uma enumeração garante que apenas essas opções possam ser usadas (ex: credito, pix)
    private FormaPagamento FormaPagamento; // Campo para armazenar a forma de pagamento
    private boolean fechada; // Campo booleano para indicar se a sacola está fechada ou não
}

//getters and setters