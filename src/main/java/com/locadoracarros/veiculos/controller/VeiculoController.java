package com.locadoracarros.veiculos.controller;

import com.locadoracarros.veiculos.model.Veiculo;
import com.locadoracarros.veiculos.service.VeiculoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @PostMapping
    public Veiculo cadastrar(@RequestBody Veiculo v) {
        return service.cadastrar(v);
    }

    @PutMapping("/{id}")
    public Veiculo atualizar(@PathVariable Long id, @RequestBody Veiculo v) {
        return service.atualizar(id, v);
    }

    @GetMapping
    public List<Veiculo> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Veiculo consultar(@PathVariable Long id) {
        return service.consultar(id);
    }
}
