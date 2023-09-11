package me.dio.sacola.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoFechadoEvent {
    private Long id;
    private Double valorTotal;
}
