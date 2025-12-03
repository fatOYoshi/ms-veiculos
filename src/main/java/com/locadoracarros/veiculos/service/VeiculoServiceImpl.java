package com.locadoracarros.veiculos.service;

import com.locadoracarros.veiculos.model.Veiculo;
import com.locadoracarros.veiculos.repository.VeiculoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoServiceImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Veiculo cadastrar(Veiculo v) {
        return repository.save(v);
    }

    @Override
    public Veiculo atualizar(Long id, Veiculo v) {
        Veiculo existente = repository.findById(id).orElseThrow();
        existente.setModelo(v.getModelo());
        existente.setMarca(v.getMarca());
        existente.setAno(v.getAno());
        existente.setPlaca(v.getPlaca());
        existente.setStatus(v.getStatus());
        return repository.save(existente);
    }

    @Override
    public List<Veiculo> listar() {
        return repository.findAll();
    }

    @Override
    public Veiculo consultar(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
