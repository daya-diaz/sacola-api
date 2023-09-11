package me.dio.sacola.events;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.model.Item;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoFechadoEvent {
    private Long id;
    private Double valorTotal;
    private List<Item> itens;
    private FormaPagamento FormaPagamento;
}
