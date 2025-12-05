package com.locadoracarros.veiculos.service;

import com.locadoracarros.veiculos.model.Veiculo;
import com.locadoracarros.veiculos.repository.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoServiceImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Veiculo cadastrar(Veiculo v) {
        // Validação da Placa (existente)
        if (v.getPlaca() == null || v.getPlaca().trim().isEmpty()) {
            throw new RuntimeException("Placa é obrigatória");
        }
        
        // Validação de Preço (Novo)
        if (v.getPreco() == null || v.getPreco().signum() <= 0) {
            throw new RuntimeException("Preço deve ser maior que zero");
        }

        // Se status não for enviado, define como "disponivel"
        if (v.getStatus() == null || v.getStatus().trim().isEmpty()) {
            v.setStatus("disponivel");
        }
        
        return repository.save(v);
    }

   @Override
    @Transactional
    public Veiculo atualizar(Long id, Veiculo v) {
        // Busca o veículo existente
        Veiculo existente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Veículo não encontrado com id: " + id));
        
        // Atualiza os campos se foram fornecidos
        if (v.getPlaca() != null) {
            existente.setPlaca(v.getPlaca());
        }
        if (v.getModelo() != null) {
            existente.setModelo(v.getModelo());
        }
        if (v.getMarca() != null) {
            existente.setMarca(v.getMarca());
        }
        if (v.getAno() != 0) { // 0 é valor default de int
            existente.setAno(v.getAno());
        }
        if (v.getStatus() != null) {
            existente.setStatus(v.getStatus());
        }
        
        // Lógica para atualizar o Preço (Novo)
        if (v.getPreco() != null) { 
             // Validação básica para o preço na atualização
             if (v.getPreco().signum() <= 0) {
                throw new RuntimeException("Preço na atualização deve ser maior que zero");
             }
            existente.setPreco(v.getPreco());
        }
        
        return repository.save(existente);
    }

    @Override
    public List<Veiculo> listar() {
        return repository.findAll();
    }

    @Override
    public Veiculo consultar(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Veículo não encontrado com id: " + id));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        // Verifica se existe antes de deletar
        if (!repository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}