package me.dio.sacola.service.impl;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.message.SacolaMensagem;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Restaurante;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.repository.ItemRepository;
import me.dio.sacola.repository.SacolaRepository;
import me.dio.sacola.repository.ProdutoRepository;
import me.dio.sacola.resource.dto.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SacolaServiceImpl implements SacolaService {
    private final RabbitTemplate rabbitTemplate;
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;

    @Autowired
    public SacolaServiceImpl(SacolaRepository sacolaRepository, RabbitTemplate rabbitTemplate, ProdutoRepository produtoRepository){
        this.sacolaRepository = sacolaRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.produtoRepository = produtoRepository;
    }
    @Override
    public Item incluirItemNaSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getSacolaId());

        //verificando se a sacola esta aberta ou fechada;
        if(sacola.isFechada()) {
            throw new RuntimeException("A sacola já foi fechada.");
        }

        //criando o item;
        Item itemParaInserir = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                    () -> {
                        throw new RuntimeException("Produto não encontrado.");
                }))
        .build();

        //verificando se a sacola esta vazia para adicionar o item, e verificando caso não esteja vazia
        //se o restaurante do item que esta na sacola é o mesmo restaurante do item que esta sendo adicionado
        List<Item> itensDaSacola = sacola.getItens();

        if(itensDaSacola.isEmpty()) {
            itensDaSacola.add(itemParaInserir);
        }else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteDoItemParaInserir = itemParaInserir.getProduto().getRestaurante();

            if(restauranteAtual.getNome().equals(restauranteDoItemParaInserir.getNome())){
                itensDaSacola.add(itemParaInserir);
            }else{
                throw new RuntimeException("Não é possível adicionar itens de restaurantes diferentes no mesmo carrinho. ");
            }
        }

        //calcular valorTotalSacola
        List<Double> valorDosItens = new ArrayList<>();
        for(Item itemDaSacola: itensDaSacola) {
            double valorTotalItens = itemDaSacola.getProduto().getValorUnitario() * itemDaSacola.getQuantidade();
            valorDosItens.add(valorTotalItens);
        }
        double valorTotalSacola = 0.0;
        for(Double valorItem : valorDosItens) {
            valorTotalSacola += valorItem;
        }

        sacola.setValorTotal(valorTotalSacola);
        //sacolaRepository é o que vai ter contato direto com o banco de dados; (?)
        sacolaRepository.save(sacola);
        return itemParaInserir;
    }

    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe.");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int numeroFormaPagamento) {
        Sacola sacola = verSacola(id);
        if (sacola.getItens().isEmpty()){
           throw new RuntimeException("Inclua items na sacola.");
        }

        FormaPagamento formaPagamento =
                numeroFormaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINETA;

        sacola.setFormaPagamento(formaPagamento);
        sacola.setFechada(true);

        enviarMensagemParaFila(sacola, numeroFormaPagamento);

        return sacolaRepository.save(sacola);
    }
    
    @Override
    public void excluirItemDaSacola(Long sacolaId, Long itemId) {
        Sacola sacola = sacolaRepository.findById(sacolaId).orElseThrow(() -> new RuntimeException("Sacola não encontrada."));

        if (sacola.isFechada()) {
            throw new RuntimeException("A sacola já foi fechada.");
        }

        Item itemParaRemover = null;

        // Encontre o item na sacola pelo ID
        for (Item item : sacola.getItens()) {
            if (item.getId() == (itemId)) { // Use equals para comparar objetos Long
                itemParaRemover = item;
                break;
            }
        }

        if (itemParaRemover != null) {
            // Remova o item da lista de itens
            if(itemParaRemover.getQuantidade() > 1){
                itemParaRemover.setQuantidade(itemParaRemover.getQuantidade() - 1);
            }else{
                sacola.getItens().remove(itemParaRemover);
            }

            // Atualize o valor total da sacola subtraindo o valor do item
            double valorItem = itemParaRemover.getProduto().getValorUnitario();
            double novoValorTotal = sacola.getValorTotal() - valorItem;
            sacola.setValorTotal(novoValorTotal);
            sacolaRepository.save(sacola);
        } else {
            throw new RuntimeException("Item não encontrado na sacola.");
        }
    }
    @Override
    public void enviarMensagemParaFila(Sacola sacola, int formaPagamento) {
        // criar uma instância da mensagem que você deseja enviar para a fila
        SacolaMensagem mensagem = new SacolaMensagem(sacola.getId(), formaPagamento);

        // usar rabbitTemplate para enviar a mensagem para a fila
        rabbitTemplate.convertAndSend("fila-de-pedidos-clientes", mensagem);
    }
    }

