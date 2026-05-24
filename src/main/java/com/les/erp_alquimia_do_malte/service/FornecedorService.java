package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.Fornecedor;
import com.les.erp_alquimia_do_malte.dto.request.FornecedorRequest;
import com.les.erp_alquimia_do_malte.dto.response.FornecedorResponse;
import com.les.erp_alquimia_do_malte.exception.BusinessException;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final AuditoriaService auditoriaService;

    public FornecedorService(FornecedorRepository fornecedorRepository, AuditoriaService auditoriaService) {
        this.fornecedorRepository = fornecedorRepository;
        this.auditoriaService = auditoriaService;
    }

    public FornecedorResponse criar(FornecedorRequest request, UUID usuarioId) {
        if (request.cnpj() != null && fornecedorRepository.existsByCnpjAndExcludedAtIsNull(request.cnpj())) {
            throw new BusinessException("CNPJ já cadastrado");
        }
        Fornecedor f = new Fornecedor();
        preencher(f, request);
        fornecedorRepository.save(f);
        auditoriaService.registrar(usuarioId, "fornecedores", "CRIAR", "Fornecedor criado: " + f.getNome());
        return FornecedorResponse.from(f);
    }

    public FornecedorResponse atualizar(UUID id, FornecedorRequest request, UUID usuarioId) {
        Fornecedor f = buscar(id);
        preencher(f, request);
        auditoriaService.registrar(usuarioId, "fornecedores", "ATUALIZAR", "Fornecedor atualizado: " + id);
        return FornecedorResponse.from(f);
    }

    public void excluir(UUID id, UUID usuarioId) {
        Fornecedor f = buscar(id);
        f.setExcludedAt(LocalDateTime.now());
        auditoriaService.registrar(usuarioId, "fornecedores", "EXCLUIR", "Fornecedor excluído: " + f.getNome());
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponse> listar() {
        return fornecedorRepository.findAllByExcludedAtIsNull().stream()
                .map(FornecedorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public FornecedorResponse buscarPorId(UUID id) {
        return FornecedorResponse.from(buscar(id));
    }

    private void preencher(Fornecedor f, FornecedorRequest r) {
        f.setNome(r.nome());
        f.setCnpj(r.cnpj());
        f.setTelefone(r.telefone());
        f.setEmail(r.email());
    }

    private Fornecedor buscar(UUID id) {
        return fornecedorRepository.findById(id)
                .filter(f -> f.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado: " + id));
    }
}
