package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.Produto;
import com.les.erp_alquimia_do_malte.domain.entity.Receita;
import com.les.erp_alquimia_do_malte.domain.entity.ReceitaInsumo;
import com.les.erp_alquimia_do_malte.domain.entity.TipoInsumo;
import com.les.erp_alquimia_do_malte.dto.request.ReceitaInsumoRequest;
import com.les.erp_alquimia_do_malte.dto.request.ReceitaRequest;
import com.les.erp_alquimia_do_malte.dto.response.ReceitaResponse;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.ProdutoRepository;
import com.les.erp_alquimia_do_malte.repository.ReceitaInsumoRepository;
import com.les.erp_alquimia_do_malte.repository.ReceitaRepository;
import com.les.erp_alquimia_do_malte.repository.TipoInsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ReceitaInsumoRepository receitaInsumoRepository;
    private final ProdutoRepository produtoRepository;
    private final TipoInsumoRepository tipoInsumoRepository;
    private final AuditoriaService auditoriaService;

    public ReceitaService(ReceitaRepository receitaRepository,
                          ReceitaInsumoRepository receitaInsumoRepository,
                          ProdutoRepository produtoRepository,
                          TipoInsumoRepository tipoInsumoRepository,
                          AuditoriaService auditoriaService) {
        this.receitaRepository = receitaRepository;
        this.receitaInsumoRepository = receitaInsumoRepository;
        this.produtoRepository = produtoRepository;
        this.tipoInsumoRepository = tipoInsumoRepository;
        this.auditoriaService = auditoriaService;
    }

    public ReceitaResponse criar(ReceitaRequest request, UUID usuarioId) {
        Produto produto = buscarProduto(request.produtoId());
        Receita receita = new Receita();
        receita.setNome(request.nome());
        receita.setProduto(produto);
        receitaRepository.save(receita);
        salvarInsumos(receita, request.insumos());
        auditoriaService.registrar(usuarioId, "receitas", "CRIAR", "Receita criada: " + receita.getNome());
        return ReceitaResponse.from(receita);
    }

    public ReceitaResponse atualizar(UUID id, ReceitaRequest request, UUID usuarioId) {
        Receita receita = buscar(id);
        receita.setNome(request.nome());
        receita.setProduto(buscarProduto(request.produtoId()));
        receitaInsumoRepository.deleteByReceita(receita);
        receita.getInsumos().clear();
        salvarInsumos(receita, request.insumos());
        auditoriaService.registrar(usuarioId, "receitas", "ATUALIZAR", "Receita atualizada: " + id);
        return ReceitaResponse.from(receita);
    }

    public void excluir(UUID id, UUID usuarioId) {
        Receita receita = buscar(id);
        receita.setExcludedAt(LocalDateTime.now());
        auditoriaService.registrar(usuarioId, "receitas", "EXCLUIR", "Receita excluída: " + receita.getNome());
    }

    @Transactional(readOnly = true)
    public List<ReceitaResponse> listar() {
        return receitaRepository.findAllByExcludedAtIsNull().stream()
                .map(ReceitaResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ReceitaResponse buscarPorId(UUID id) {
        return ReceitaResponse.from(buscar(id));
    }

    public Receita buscarEntidade(UUID id) {
        return buscar(id);
    }

    private void salvarInsumos(Receita receita, List<ReceitaInsumoRequest> insumosReq) {
        for (ReceitaInsumoRequest req : insumosReq) {
            TipoInsumo tipo = tipoInsumoRepository.findById(req.tipoInsumoId())
                    .filter(t -> t.getExcludedAt() == null)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de insumo não encontrado: " + req.tipoInsumoId()));
            ReceitaInsumo ri = new ReceitaInsumo();
            ri.setReceita(receita);
            ri.setTipoInsumo(tipo);
            ri.setQuantidade(req.quantidade());
            receitaInsumoRepository.save(ri);
            receita.getInsumos().add(ri);
        }
    }

    private Produto buscarProduto(UUID id) {
        return produtoRepository.findById(id)
                .filter(p -> p.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));
    }

    private Receita buscar(UUID id) {
        return receitaRepository.findById(id)
                .filter(r -> r.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada: " + id));
    }
}
