package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.dto.response.DashboardResponse;
import com.les.erp_alquimia_do_malte.dto.response.LoteInsumoResponse;
import com.les.erp_alquimia_do_malte.dto.response.LoteProdutoResponse;
import com.les.erp_alquimia_do_malte.dto.response.TipoInsumoResponse;
import com.les.erp_alquimia_do_malte.repository.LoteInsumoRepository;
import com.les.erp_alquimia_do_malte.repository.LoteProdutoRepository;
import com.les.erp_alquimia_do_malte.repository.TipoInsumoRepository;
import com.les.erp_alquimia_do_malte.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private static final int DIAS_ALERTA = 30;

    private final LoteInsumoRepository loteInsumoRepository;
    private final LoteProdutoRepository loteProdutoRepository;
    private final TipoInsumoRepository tipoInsumoRepository;
    private final VendaRepository vendaRepository;

    public DashboardService(LoteInsumoRepository loteInsumoRepository,
                            LoteProdutoRepository loteProdutoRepository,
                            TipoInsumoRepository tipoInsumoRepository,
                            VendaRepository vendaRepository) {
        this.loteInsumoRepository = loteInsumoRepository;
        this.loteProdutoRepository = loteProdutoRepository;
        this.tipoInsumoRepository = tipoInsumoRepository;
        this.vendaRepository = vendaRepository;
    }

    public DashboardResponse gerar() {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(DIAS_ALERTA);

        List<LoteInsumoResponse> alertasInsumo = loteInsumoRepository
                .findLotesProximosAoVencimento(hoje, limite).stream()
                .map(LoteInsumoResponse::from).toList();

        List<LoteProdutoResponse> alertasProduto = loteProdutoRepository
                .findLotesProximosAoVencimento(hoje, limite).stream()
                .map(LoteProdutoResponse::from).toList();

        List<TipoInsumoResponse> alertasEstoque = tipoInsumoRepository
                .findInsumosAbaixoDoEstoqueMinimo().stream()
                .map(TipoInsumoResponse::from).toList();

        YearMonth mesAtual = YearMonth.now();
        LocalDateTime inicioMes = mesAtual.atDay(1).atStartOfDay();
        LocalDateTime fimMes = mesAtual.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal totalVendasMes = vendaRepository
                .findByDataVendaBetweenAndExcludedAtIsNullOrderByDataVendaDesc(inicioMes, fimMes)
                .stream()
                .map(v -> v.getValorTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardResponse(
                loteInsumoRepository.findAllByExcludedAtIsNull().size(),
                loteProdutoRepository.findAllByExcludedAtIsNull().size(),
                alertasEstoque.size(),
                alertasInsumo.size(),
                alertasProduto.size(),
                totalVendasMes,
                alertasInsumo,
                alertasProduto,
                alertasEstoque
        );
    }
}
