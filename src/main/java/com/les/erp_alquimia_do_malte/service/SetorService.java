package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.Setor;
import com.les.erp_alquimia_do_malte.dto.request.SetorRequest;
import com.les.erp_alquimia_do_malte.dto.response.SetorResponse;
import com.les.erp_alquimia_do_malte.exception.BusinessException;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.SetorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SetorService {

    private final SetorRepository setorRepository;
    private final AuditoriaService auditoriaService;

    public SetorService(SetorRepository setorRepository, AuditoriaService auditoriaService) {
        this.setorRepository = setorRepository;
        this.auditoriaService = auditoriaService;
    }

    public SetorResponse criar(SetorRequest request, UUID usuarioId) {
        if (setorRepository.existsByNomeIgnoreCaseAndExcludedAtIsNull(request.nome())) {
            throw new BusinessException("Já existe um setor com este nome");
        }
        Setor setor = new Setor();
        setor.setNome(request.nome());
        setorRepository.save(setor);
        auditoriaService.registrar(usuarioId, "setores", "CRIAR", "Setor criado: " + setor.getNome());
        return SetorResponse.from(setor);
    }

    public SetorResponse atualizar(UUID id, SetorRequest request, UUID usuarioId) {
        Setor setor = buscar(id);
        setor.setNome(request.nome());
        auditoriaService.registrar(usuarioId, "setores", "ATUALIZAR", "Setor atualizado: " + id);
        return SetorResponse.from(setor);
    }

    public void excluir(UUID id, UUID usuarioId) {
        Setor setor = buscar(id);
        setor.setExcludedAt(LocalDateTime.now());
        auditoriaService.registrar(usuarioId, "setores", "EXCLUIR", "Setor excluído: " + setor.getNome());
    }

    @Transactional(readOnly = true)
    public List<SetorResponse> listar() {
        return setorRepository.findAllByExcludedAtIsNull().stream()
                .map(SetorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SetorResponse buscarPorId(UUID id) {
        return SetorResponse.from(buscar(id));
    }

    private Setor buscar(UUID id) {
        return setorRepository.findById(id)
                .filter(s -> s.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado: " + id));
    }
}
