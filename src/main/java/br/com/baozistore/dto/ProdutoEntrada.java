package br.com.baozistore.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoEntrada(
        @NotBlank @Size(max = 120) String nome,
        @NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal preco,
        @NotNull Boolean estoque
) {
}
