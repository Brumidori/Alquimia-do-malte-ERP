package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.Cliente;
import com.les.erp_alquimia_do_malte.dto.request.ClienteRequest;
import com.les.erp_alquimia_do_malte.dto.response.ClienteResponse;
import com.les.erp_alquimia_do_malte.exception.BusinessException;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final AuditoriaService auditoriaService;

    public ClienteService(ClienteRepository clienteRepository, AuditoriaService auditoriaService) {
        this.clienteRepository = clienteRepository;
        this.auditoriaService = auditoriaService;
    }

    public ClienteResponse criar(ClienteRequest request, UUID usuarioId) {
        if (request.cnpj() != null && clienteRepository.existsByCnpjAndExcludedAtIsNull(request.cnpj())) {
            throw new BusinessException("CNPJ já cadastrado");
        }
        Cliente cliente = new Cliente();
        preencher(cliente, request);
        clienteRepository.save(cliente);
        auditoriaService.registrar(usuarioId, "clientes", "CRIAR", "Cliente criado: " + cliente.getNome());
        return ClienteResponse.from(cliente);
    }

    public ClienteResponse atualizar(UUID id, ClienteRequest request, UUID usuarioId) {
        Cliente cliente = buscar(id);
        preencher(cliente, request);
        auditoriaService.registrar(usuarioId, "clientes", "ATUALIZAR", "Cliente atualizado: " + id);
        return ClienteResponse.from(cliente);
    }

    public void excluir(UUID id, UUID usuarioId) {
        Cliente cliente = buscar(id);
        cliente.setExcludedAt(LocalDateTime.now());
        auditoriaService.registrar(usuarioId, "clientes", "EXCLUIR", "Cliente excluído: " + cliente.getNome());
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAllByExcludedAtIsNull().stream()
                .map(ClienteResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(UUID id) {
        return ClienteResponse.from(buscar(id));
    }

    private void preencher(Cliente c, ClienteRequest r) {
        c.setNome(r.nome());
        c.setCnpj(r.cnpj());
        c.setTelefone(r.telefone());
        c.setEmail(r.email());
    }

    private Cliente buscar(UUID id) {
        return clienteRepository.findById(id)
                .filter(c -> c.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
    }
}
