package br.com.baozistore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoEntrada(
        @NotNull @Positive Long clienteId,
        @NotNull @Positive Long produtoId,
        @NotNull @Positive Integer quantidade
) {
}
