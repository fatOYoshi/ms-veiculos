package com.locadoracarros.veiculos.controller;

import com.locadoracarros.veiculos.model.Veiculo;
import com.locadoracarros.veiculos.service.VeiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
@CrossOrigin(origins = "*", 
           allowedHeaders = "*", 
           methods = {RequestMethod.GET, RequestMethod.POST, 
                      RequestMethod.PUT, RequestMethod.DELETE, 
                      RequestMethod.OPTIONS})
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Veiculo v) {
        try {
            // Validação básica
            if (v.getPlaca() == null || v.getPlaca().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Placa é obrigatória");
            }
            if (v.getAno() < 1900 || v.getAno() > 2100) {
                return ResponseEntity.badRequest().body("Ano inválido");
            }
            
            Veiculo salvo = service.cadastrar(v);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar veículo: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Veiculo v) {
        try {
            Veiculo atualizado = service.atualizar(id, v);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar veículo: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Veiculo>> listar() {
        try {
            List<Veiculo> veiculos = service.listar();
            return ResponseEntity.ok(veiculos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultar(@PathVariable Long id) {
        try {
            Veiculo veiculo = service.consultar(id);
            return ResponseEntity.ok(veiculo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao consultar veículo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            service.excluir(id);
            // Retorna 200 OK com mensagem de sucesso
            return ResponseEntity.ok().body("{\"message\": \"Veículo excluído com sucesso\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Erro ao excluir veículo\"}");
        }
    }
}