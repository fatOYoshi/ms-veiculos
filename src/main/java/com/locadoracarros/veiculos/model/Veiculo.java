package com.locadoracarros.veiculos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal; // Importação necessária

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;
    private String marca;
    private int ano;
    private String placa;

    // Novo campo para o preço do carro (valor de venda ou aluguel)
    @Column(precision = 10, scale = 2) // Opcional, mas recomendado para BigDecimal no JPA
    private BigDecimal preco; 

    // disponível, alugado, manutenção
    private String status;
}