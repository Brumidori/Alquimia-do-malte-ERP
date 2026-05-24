package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.Fornecedor;
import com.les.erp_alquimia_do_malte.domain.entity.LoteInsumo;
import com.les.erp_alquimia_do_malte.domain.entity.TipoInsumo;
import com.les.erp_alquimia_do_malte.dto.request.LoteInsumoRequest;
import com.les.erp_alquimia_do_malte.dto.response.LoteInsumoResponse;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.FornecedorRepository;
import com.les.erp_alquimia_do_malte.repository.LoteInsumoRepository;
import com.les.erp_alquimia_do_malte.repository.TipoInsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LoteInsumoService {

    private final LoteInsumoRepository loteInsumoRepository;
    private final TipoInsumoRepository tipoInsumoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final AuditoriaService auditoriaService;

    public LoteInsumoService(LoteInsumoRepository loteInsumoRepository,
                              TipoInsumoRepository tipoInsumoRepository,
                              FornecedorRepository fornecedorRepository,
                              AuditoriaService auditoriaService) {
        this.loteInsumoRepository = loteInsumoRepository;
        this.tipoInsumoRepository = tipoInsumoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.auditoriaService = auditoriaService;
    }

    public LoteInsumoResponse registrarEntrada(LoteInsumoRequest request, UUID usuarioId) {
        TipoInsumo tipo = tipoInsumoRepository.findById(request.tipoInsumoId())
                .filter(t -> t.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de insumo não encontrado"));

        LoteInsumo lote = new LoteInsumo();
        lote.setTipoInsumo(tipo);
        lote.setQuantidade(request.quantidade());
        lote.setDataValidade(request.dataValidade());
        lote.setCodigoLote(gerarCodigo("LI", tipo.getNome()));

        if (request.fornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));
            lote.setFornecedor(fornecedor);
        }

        loteInsumoRepository.save(lote);
        auditoriaService.registrar(usuarioId, "lotes_insumo", "CRIAR",
                "Entrada de lote: " + lote.getCodigoLote() + " (" + request.quantidade() + " " + tipo.getUnidadeMedida() + ")");
        return LoteInsumoResponse.from(lote);
    }

    @Transactional(readOnly = true)
    public List<LoteInsumoResponse> listar() {
        return loteInsumoRepository.findAllByExcludedAtIsNull().stream()
                .map(LoteInsumoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public LoteInsumoResponse buscarPorId(UUID id) {
        return LoteInsumoResponse.from(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<LoteInsumoResponse> listarProximosVencimento(int dias) {
        LocalDate hoje = LocalDate.now();
        return loteInsumoRepository.findLotesProximosAoVencimento(hoje, hoje.plusDays(dias)).stream()
                .map(LoteInsumoResponse::from).toList();
    }

    public LoteInsumo buscarEntidade(UUID id) {
        return buscar(id);
    }

    private LoteInsumo buscar(UUID id) {
        return loteInsumoRepository.findById(id)
                .filter(l -> l.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Lote de insumo não encontrado: " + id));
    }

    private String gerarCodigo(String prefixo, String nome) {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sufixo = String.valueOf(System.currentTimeMillis()).substring(9);
        return prefixo + "-" + data + "-" + sufixo;
    }
}
