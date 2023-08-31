package me.dio.sacola.service.impl;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Restaurante;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.repository.ItemRepository;
import me.dio.sacola.repository.SacolaRepository;
import me.dio.sacola.repository.ProdutoRepository;
import me.dio.sacola.resource.dto.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SacolaServiceImpl  implements SacolaService {
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemRepository itemRepository;
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
        return sacolaRepository.save(sacola);
    }
}

