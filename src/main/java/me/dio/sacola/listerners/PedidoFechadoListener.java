package me.dio.sacola.listerners;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import me.dio.sacola.events.PedidoFechadoEvent;

@Component
public class PedidoFechadoListener {
    
    @RabbitListener(queues = "sacola.v1.pedidos-clientes")
    public void quandoPedidoFechar(PedidoFechadoEvent event) {
        System.out.println("Id do produto: " + event.getId());
        System.out.println("Valor final da compra: " + event.getValorTotal());

    }
}
