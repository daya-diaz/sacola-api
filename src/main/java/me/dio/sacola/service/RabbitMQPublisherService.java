package me.dio.sacola.service;


import me.dio.sacola.message.SacolaMensagem;
import me.dio.sacola.model.Sacola;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisherService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void enviarMensagemParaFila(Sacola sacola, int numeroFormaPagamento) {
        SacolaMensagem mensagem = new SacolaMensagem(sacola.getId(), numeroFormaPagamento);

        rabbitTemplate.convertAndSend("fila-de-pedidos-clientes", mensagem);
    }
}
