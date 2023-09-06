package me.dio.sacola.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue filaDePedidosClientes() {
        return new Queue("fila-de-pedidos-clientes");
    }
}