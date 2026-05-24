package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.LoteProduto;
import com.les.erp_alquimia_do_malte.dto.response.LoteProdutoResponse;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.LoteProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class LoteProdutoService {

    private final LoteProdutoRepository loteProdutoRepository;

    public LoteProdutoService(LoteProdutoRepository loteProdutoRepository) {
        this.loteProdutoRepository = loteProdutoRepository;
    }

    public List<LoteProdutoResponse> listar() {
        return loteProdutoRepository.findAllByExcludedAtIsNull().stream()
                .map(LoteProdutoResponse::from).toList();
    }

    public LoteProdutoResponse buscarPorId(UUID id) {
        return LoteProdutoResponse.from(buscar(id));
    }

    public List<LoteProdutoResponse> listarProximosVencimento(int dias) {
        LocalDate hoje = LocalDate.now();
        return loteProdutoRepository.findLotesProximosAoVencimento(hoje, hoje.plusDays(dias)).stream()
                .map(LoteProdutoResponse::from).toList();
    }

    public LoteProduto buscarEntidade(UUID id) {
        return buscar(id);
    }

    private LoteProduto buscar(UUID id) {
        return loteProdutoRepository.findById(id)
                .filter(l -> l.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Lote de produto não encontrado: " + id));
    }
}
