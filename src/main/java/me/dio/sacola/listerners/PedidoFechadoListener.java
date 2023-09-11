package me.dio.sacola.listerners;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoFechadoListener {
    
    @RabbitListener(queues = "sacola.v1.pedidos-clientes")
    public void quandoPedidoFechar(String id) {
        System.out.println("Id Recebido: " + id);
    }
}
