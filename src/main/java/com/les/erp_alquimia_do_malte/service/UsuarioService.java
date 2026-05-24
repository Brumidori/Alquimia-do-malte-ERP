package com.les.erp_alquimia_do_malte.service;

import com.les.erp_alquimia_do_malte.domain.entity.Setor;
import com.les.erp_alquimia_do_malte.domain.entity.Usuario;
import com.les.erp_alquimia_do_malte.dto.request.UsuarioRequest;
import com.les.erp_alquimia_do_malte.dto.response.UsuarioResponse;
import com.les.erp_alquimia_do_malte.exception.BusinessException;
import com.les.erp_alquimia_do_malte.exception.ResourceNotFoundException;
import com.les.erp_alquimia_do_malte.repository.SetorRepository;
import com.les.erp_alquimia_do_malte.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final SetorRepository setorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public UsuarioService(UsuarioRepository usuarioRepository, SetorRepository setorRepository,
                          PasswordEncoder passwordEncoder, AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.setorRepository = setorRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    public UsuarioResponse criar(UsuarioRequest request, UUID usuarioId) {
        if (usuarioRepository.existsByEmailAndExcludedAtIsNull(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (usuarioRepository.existsByCpfAndExcludedAtIsNull(request.cpf())) {
            throw new BusinessException("CPF já cadastrado");
        }
        Usuario usuario = new Usuario();
        preencherCampos(usuario, request);
        usuarioRepository.save(usuario);
        auditoriaService.registrar(usuarioId, "usuarios", "CRIAR", "Usuário criado: " + usuario.getEmail());
        return UsuarioResponse.from(usuario);
    }

    public UsuarioResponse atualizar(UUID id, UsuarioRequest request, UUID usuarioId) {
        Usuario usuario = buscar(id);
        if (!usuario.getEmail().equals(request.email()) &&
                usuarioRepository.existsByEmailAndExcludedAtIsNull(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        preencherCampos(usuario, request);
        auditoriaService.registrar(usuarioId, "usuarios", "ATUALIZAR", "Usuário atualizado: " + id);
        return UsuarioResponse.from(usuario);
    }

    public void excluir(UUID id, UUID usuarioId) {
        Usuario usuario = buscar(id);
        usuario.setExcludedAt(LocalDateTime.now());
        usuario.setAtivo(false);
        auditoriaService.registrar(usuarioId, "usuarios", "EXCLUIR", "Usuário excluído: " + usuario.getEmail());
    }

    public UsuarioResponse alternarAtivo(UUID id, UUID usuarioId) {
        Usuario usuario = buscar(id);
        usuario.setAtivo(!Boolean.TRUE.equals(usuario.getAtivo()));
        String acao = Boolean.TRUE.equals(usuario.getAtivo()) ? "ATIVAR" : "DESATIVAR";
        auditoriaService.registrar(usuarioId, "usuarios", acao, "Usuário " + acao.toLowerCase() + "do: " + id);
        return UsuarioResponse.from(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAllByExcludedAtIsNull().stream()
                .map(UsuarioResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(UUID id) {
        return UsuarioResponse.from(buscar(id));
    }

    private void preencherCampos(Usuario usuario, UsuarioRequest request) {
        usuario.setNome(request.nome());
        usuario.setCpf(request.cpf());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setTipo(request.tipo());
        if (request.setorId() != null) {
            Setor setor = setorRepository.findById(request.setorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));
            usuario.setSetor(setor);
        } else {
            usuario.setSetor(null);
        }
    }

    private Usuario buscar(UUID id) {
        return usuarioRepository.findById(id)
                .filter(u -> u.getExcludedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }
}
