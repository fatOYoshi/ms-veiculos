package com.locadoracarros.veiculos.service;

import com.locadoracarros.veiculos.model.Veiculo;
import java.util.List;

public interface VeiculoService {

    Veiculo cadastrar(Veiculo v);
    Veiculo atualizar(Long id, Veiculo v);
    List<Veiculo> listar();
    Veiculo consultar(Long id);
    void excluir(Long id); // ✅ ADICIONAR ESTE MÉTODO
}