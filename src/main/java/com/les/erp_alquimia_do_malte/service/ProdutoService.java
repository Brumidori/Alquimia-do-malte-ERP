package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.Produto;
import com.les.erp_alquimia_do_malte.dto.request.ProdutoRequest;
import com.les.erp_alquimia_do_malte.dto.response.ProdutoResponse;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final AuditoriaService auditoriaService;

    public ProdutoService(ProdutoRepository produtoRepository, AuditoriaService auditoriaService) {
        this.produtoRepository = produtoRepository;
        this.auditoriaService = auditoriaService;
    }

    public ProdutoResponse criar(ProdutoRequest request, UUID usuarioId) {
        Produto p = new Produto();
        preencher(p, request);
        produtoRepository.save(p);
        auditoriaService.registrar(usuarioId, "produtos", "CRIAR", "Produto criado: " + p.getNome());
        return ProdutoResponse.from(p);
    }

    public ProdutoResponse atualizar(UUID id, ProdutoRequest request, UUID usuarioId) {
        Produto p = buscar(id);
        preencher(p, request);
        auditoriaService.registrar(usuarioId, "produtos", "ATUALIZAR", "Produto atualizado: " + id);
        return ProdutoResponse.from(p);
    }

    public void excluir(UUID id, UUID usuarioId) {
        Produto p = buscar(id);
        p.setExcludedAt(LocalDateTime.now());
        auditoriaService.registrar(usuarioId, "produtos", "EXCLUIR", "Produto excluído: " + p.getNome());
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar() {
        return produtoRepository.findAllByExcludedAtIsNull().stream()
                .map(ProdutoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(UUID id) {
        return ProdutoResponse.from(buscar(id));
    }

    public Produto buscarEntidade(UUID id) {
        return buscar(id);
    }

    private void preencher(Produto p, ProdutoRequest r) {
        p.setNome(r.nome());
        p.setUnidadeMedida(r.unidadeMedida());
        p.setValidadeDias(r.validadeDias());
    }

    private Produto buscar(UUID id) {
        return produtoRepository.findById(id)
                .filter(p -> p.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));
    }
}
