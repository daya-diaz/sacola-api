package me.dio.sacola.listerners;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import me.dio.sacola.events.PedidoFechadoEvent;
import me.dio.sacola.model.Item;

@Component
public class PedidoFechadoListener {
    
    @RabbitListener(queues = "sacola.v1.pedidos-clientes")
    public void quandoPedidoFechar(PedidoFechadoEvent event) {
        System.out.println("Id da sacola: " + event.getId());

        for(Item item: event.getItens()){
            String nomeDoProduto = item.getProduto().getNome();
            Double valorUnitario = item.getProduto().getValorUnitario();
            int quantidade = item.getQuantidade();

            System.out.println("Produto: " + nomeDoProduto + " ----- Valor: " + valorUnitario + " ----- Quantidade: " + quantidade);
        }
        System.out.println("Forma de pagamento escolhida: " + event.getFormaPagamento());
        System.out.println("Valor final da compra: " + event.getValorTotal());

    }
}
