package br.com.baozistore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClienteEntrada(
        @NotBlank @Size(max = 120) String nome,
        @NotNull @PastOrPresent LocalDate clienteDesde
) {
}
