package br.com.baozistore.controller;

import br.com.baozistore.dto.PedidoEntrada;
import br.com.baozistore.dto.PedidoSaida;
import br.com.baozistore.model.Pedido;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public PedidoController(PedidoRepository pedidos, ClienteRepository clientes, ProdutoRepository produtos) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @PostMapping
    public ResponseEntity<PedidoSaida> criar(@Valid @RequestBody PedidoEntrada entrada) {
        var cliente = clientes.findById(entrada.clienteId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente " + entrada.clienteId() + " não encontrado"));
        var produto = produtos.findById(entrada.produtoId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto " + entrada.produtoId() + " não encontrado"));

        if (!produto.getEstoque()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto indisponível");
        }

        var salvo = pedidos.save(new Pedido(cliente, produto, entrada.quantidade()));
        URI local = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(local).body(PedidoSaida.de(salvo));
    }

    @GetMapping
    public List<PedidoSaida> listar() {
        return pedidos.findAll(Sort.by("id")).stream().map(PedidoSaida::de).toList();
    }

    @GetMapping("/{id}")
    public PedidoSaida consultar(@PathVariable Long id) {
        return PedidoSaida.de(buscar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pedidos.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }

    private Pedido buscar(Long id) {
        return pedidos.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido " + id + " não encontrado"));
    }
}
