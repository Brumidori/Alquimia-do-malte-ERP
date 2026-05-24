package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.LoteInsumo;
import com.les.erp_alquimia_do_malte.domain.entity.LoteProduto;
import com.les.erp_alquimia_do_malte.domain.entity.Producao;
import com.les.erp_alquimia_do_malte.domain.entity.Receita;
import com.les.erp_alquimia_do_malte.domain.entity.ReceitaInsumo;
import com.les.erp_alquimia_do_malte.domain.entity.Usuario;
import com.les.erp_alquimia_do_malte.dto.request.ProducaoRequest;
import com.les.erp_alquimia_do_malte.dto.response.ProducaoResponse;
import com.les.erp_alquimia_do_malte.exception.BusinessException;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.LoteInsumoRepository;
import com.les.erp_alquimia_do_malte.repository.LoteProdutoRepository;
import com.les.erp_alquimia_do_malte.repository.ProducaoRepository;
import com.les.erp_alquimia_do_malte.repository.ReceitaRepository;
import com.les.erp_alquimia_do_malte.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProducaoService {

    private final ProducaoRepository producaoRepository;
    private final ReceitaRepository receitaRepository;
    private final LoteInsumoRepository loteInsumoRepository;
    private final LoteProdutoRepository loteProdutoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;

    public ProducaoService(ProducaoRepository producaoRepository,
                           ReceitaRepository receitaRepository,
                           LoteInsumoRepository loteInsumoRepository,
                           LoteProdutoRepository loteProdutoRepository,
                           UsuarioRepository usuarioRepository,
                           AuditoriaService auditoriaService) {
        this.producaoRepository = producaoRepository;
        this.receitaRepository = receitaRepository;
        this.loteInsumoRepository = loteInsumoRepository;
        this.loteProdutoRepository = loteProdutoRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    public ProducaoResponse registrar(ProducaoRequest request, UUID usuarioId) {
        Receita receita = receitaRepository.findById(request.receitaId())
                .filter(r -> r.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        validarEstoque(receita, request.quantidadeProduzida());
        consumirInsumos(receita, request.quantidadeProduzida());

        Producao producao = new Producao();
        producao.setReceita(receita);
        producao.setQuantidadeProduzida(request.quantidadeProduzida());
        producao.setDataProducao(request.dataProducao());
        producao.setUsuario(usuario);
        producaoRepository.save(producao);

        LoteProduto lote = criarLoteProduto(producao, receita, request);
        loteProdutoRepository.save(lote);

        auditoriaService.registrar(usuarioId, "producoes", "CRIAR",
                "Produção registrada: receita=" + receita.getNome() +
                ", quantidade=" + request.quantidadeProduzida() +
                ", lote=" + lote.getCodigoLote());

        return ProducaoResponse.from(producao);
    }

    @Transactional(readOnly = true)
    public List<ProducaoResponse> listar() {
        return producaoRepository.findAllByOrderByDataProducaoDesc().stream()
                .map(ProducaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ProducaoResponse buscarPorId(UUID id) {
        return ProducaoResponse.from(
                producaoRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Produção não encontrada: " + id))
        );
    }

    private void validarEstoque(Receita receita, BigDecimal fator) {
        for (ReceitaInsumo ri : receita.getInsumos()) {
            BigDecimal necessario = ri.getQuantidade().multiply(fator);
            BigDecimal disponivel = loteInsumoRepository.sumQuantidadeByTipoInsumo(ri.getTipoInsumo().getId());
            if (disponivel.compareTo(necessario) < 0) {
                throw new BusinessException(
                        "Estoque insuficiente de '" + ri.getTipoInsumo().getNome() +
                        "'. Necessário: " + necessario + ", Disponível: " + disponivel);
            }
        }
    }

    // Consome insumos em ordem FIFO (data_validade ASC)
    private void consumirInsumos(Receita receita, BigDecimal fator) {
        for (ReceitaInsumo ri : receita.getInsumos()) {
            BigDecimal restante = ri.getQuantidade().multiply(fator);
            List<LoteInsumo> lotes = loteInsumoRepository
                    .findByTipoInsumoAndExcludedAtIsNullAndQuantidadeGreaterThanOrderByDataValidadeAsc(
                            ri.getTipoInsumo(), BigDecimal.ZERO);

            for (LoteInsumo lote : lotes) {
                if (restante.compareTo(BigDecimal.ZERO) <= 0) break;
                if (lote.getQuantidade().compareTo(restante) >= 0) {
                    lote.setQuantidade(lote.getQuantidade().subtract(restante));
                    restante = BigDecimal.ZERO;
                } else {
                    restante = restante.subtract(lote.getQuantidade());
                    lote.setQuantidade(BigDecimal.ZERO);
                }
            }
        }
    }

    private LoteProduto criarLoteProduto(Producao producao, Receita receita, ProducaoRequest request) {
        LoteProduto lote = new LoteProduto();
        lote.setProduto(receita.getProduto());
        lote.setProducao(producao);
        lote.setQuantidade(request.quantidadeProduzida());
        lote.setCodigoLote(gerarCodigo(request.dataProducao()));

        int validadeDias = receita.getProduto().getValidadeDias();
        lote.setDataValidade(request.dataProducao().toLocalDate().plusDays(validadeDias));

        return lote;
    }

    private String gerarCodigo(LocalDateTime data) {
        String dataStr = data.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sufixo = String.valueOf(System.currentTimeMillis()).substring(9);
        return "LP-" + dataStr + "-" + sufixo;
    }
}
