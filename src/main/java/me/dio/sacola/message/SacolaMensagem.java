package me.dio.sacola.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SacolaMensagem {
    private Long sacolaId;
    private int formaPagamento;
}
