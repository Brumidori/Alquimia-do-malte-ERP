package com.les.erp_alquimia_do_malte.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        long totalInsumosAtivos,
        long totalProdutosAtivos,
        long insumosAbaixoEstoqueMinimo,
        long lotesInsumoProximosVencimento,
        long lotesProdutoProximosVencimento,
        BigDecimal totalVendasMes,
        List<LoteInsumoResponse> alertasInsumosVencendo,
        List<LoteProdutoResponse> alertasProdutosVencendo,
        List<TipoInsumoResponse> alertasEstoqueMinimo
) {}
