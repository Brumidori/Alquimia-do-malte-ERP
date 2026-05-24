package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.TipoInsumo;
import com.les.erp_alquimia_do_malte.dto.request.TipoInsumoRequest;
import com.les.erp_alquimia_do_malte.dto.response.TipoInsumoResponse;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.TipoInsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TipoInsumoService {

    private final TipoInsumoRepository tipoInsumoRepository;
    private final AuditoriaService auditoriaService;

    public TipoInsumoService(TipoInsumoRepository tipoInsumoRepository, AuditoriaService auditoriaService) {
        this.tipoInsumoRepository = tipoInsumoRepository;
        this.auditoriaService = auditoriaService;
    }

    public TipoInsumoResponse criar(TipoInsumoRequest request, UUID usuarioId) {
        TipoInsumo t = new TipoInsumo();
        preencher(t, request);
        tipoInsumoRepository.save(t);
        auditoriaService.registrar(usuarioId, "tipos_insumo", "CRIAR", "Tipo insumo criado: " + t.getNome());
        return TipoInsumoResponse.from(t);
    }

    public TipoInsumoResponse atualizar(UUID id, TipoInsumoRequest request, UUID usuarioId) {
        TipoInsumo t = buscar(id);
        preencher(t, request);
        auditoriaService.registrar(usuarioId, "tipos_insumo", "ATUALIZAR", "Tipo insumo atualizado: " + id);
        return TipoInsumoResponse.from(t);
    }

    public void excluir(UUID id, UUID usuarioId) {
        TipoInsumo t = buscar(id);
        t.setExcludedAt(LocalDateTime.now());
        auditoriaService.registrar(usuarioId, "tipos_insumo", "EXCLUIR", "Tipo insumo excluído: " + t.getNome());
    }

    @Transactional(readOnly = true)
    public List<TipoInsumoResponse> listar() {
        return tipoInsumoRepository.findAllByExcludedAtIsNull().stream()
                .map(TipoInsumoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TipoInsumoResponse buscarPorId(UUID id) {
        return TipoInsumoResponse.from(buscar(id));
    }

    public TipoInsumo buscarEntidade(UUID id) {
        return buscar(id);
    }

    private void preencher(TipoInsumo t, TipoInsumoRequest r) {
        t.setNome(r.nome());
        t.setUnidadeMedida(r.unidadeMedida());
        t.setEstoqueMinimo(r.estoqueMinimo());
        t.setValidadeDias(r.validadeDias());
    }

    private TipoInsumo buscar(UUID id) {
        return tipoInsumoRepository.findById(id)
                .filter(t -> t.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de insumo não encontrado: " + id));
    }
}
