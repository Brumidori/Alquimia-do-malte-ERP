package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.Cliente;
import com.les.erp_alquimia_do_malte.domain.entity.LoteProduto;
import com.les.erp_alquimia_do_malte.domain.entity.Usuario;
import com.les.erp_alquimia_do_malte.domain.entity.Venda;
import com.les.erp_alquimia_do_malte.domain.entity.VendaItem;
import com.les.erp_alquimia_do_malte.dto.request.VendaItemRequest;
import com.les.erp_alquimia_do_malte.dto.request.VendaRequest;
import com.les.erp_alquimia_do_malte.dto.response.VendaResponse;
import com.les.erp_alquimia_do_malte.exception.BusinessException;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.ClienteRepository;
import com.les.erp_alquimia_do_malte.repository.LoteProdutoRepository;
import com.les.erp_alquimia_do_malte.repository.UsuarioRepository;
import com.les.erp_alquimia_do_malte.repository.VendaItemRepository;
import com.les.erp_alquimia_do_malte.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VendaService {

    private final VendaRepository vendaRepository;
    private final VendaItemRepository vendaItemRepository;
    private final ClienteRepository clienteRepository;
    private final LoteProdutoRepository loteProdutoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;

    public VendaService(VendaRepository vendaRepository,
                        VendaItemRepository vendaItemRepository,
                        ClienteRepository clienteRepository,
                        LoteProdutoRepository loteProdutoRepository,
                        UsuarioRepository usuarioRepository,
                        AuditoriaService auditoriaService) {
        this.vendaRepository = vendaRepository;
        this.vendaItemRepository = vendaItemRepository;
        this.clienteRepository = clienteRepository;
        this.loteProdutoRepository = loteProdutoRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    public VendaResponse criar(VendaRequest request, UUID usuarioId) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .filter(c -> c.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        validarItens(request.itens());

        BigDecimal total = request.itens().stream()
                .map(i -> i.valorUnitario().multiply(i.quantidade()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setDataVenda(request.dataVenda());
        venda.setValorTotal(total);
        venda.setUsuario(usuario);
        vendaRepository.save(venda);

        for (VendaItemRequest itemReq : request.itens()) {
            LoteProduto lote = loteProdutoRepository.findById(itemReq.loteProdutoId())
                    .filter(l -> l.getExcludedAt() == null)
                    .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado: " + itemReq.loteProdutoId()));

            lote.setQuantidade(lote.getQuantidade().subtract(itemReq.quantidade()));

            VendaItem item = new VendaItem();
            item.setVenda(venda);
            item.setLoteProduto(lote);
            item.setQuantidade(itemReq.quantidade());
            item.setValorUnitario(itemReq.valorUnitario());
            vendaItemRepository.save(item);
            venda.getItens().add(item);
        }

        auditoriaService.registrar(usuarioId, "vendas", "CRIAR",
                "Venda criada para " + cliente.getNome() + " - Total: R$ " + total);
        return VendaResponse.from(venda);
    }

    public void cancelar(UUID id, UUID usuarioId) {
        Venda venda = buscar(id);
        for (VendaItem item : venda.getItens()) {
            LoteProduto lote = item.getLoteProduto();
            lote.setQuantidade(lote.getQuantidade().add(item.getQuantidade()));
        }
        venda.setExcludedAt(LocalDateTime.now());
        auditoriaService.registrar(usuarioId, "vendas", "CANCELAR", "Venda cancelada: " + id);
    }

    @Transactional(readOnly = true)
    public List<VendaResponse> listar() {
        return vendaRepository.findAllByExcludedAtIsNullOrderByDataVendaDesc().stream()
                .map(VendaResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public VendaResponse buscarPorId(UUID id) {
        return VendaResponse.from(buscar(id));
    }

    private void validarItens(List<VendaItemRequest> itens) {
        for (VendaItemRequest item : itens) {
            LoteProduto lote = loteProdutoRepository.findById(item.loteProdutoId())
                    .filter(l -> l.getExcludedAt() == null)
                    .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado: " + item.loteProdutoId()));

            if (lote.getQuantidade().compareTo(item.quantidade()) < 0) {
                throw new BusinessException(
                        "Estoque insuficiente do lote '" + lote.getCodigoLote() +
                        "'. Disponível: " + lote.getQuantidade() +
                        ", Solicitado: " + item.quantidade());
            }
        }
    }

    private Venda buscar(UUID id) {
        return vendaRepository.findById(id)
                .filter(v -> v.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada: " + id));
    }
}
