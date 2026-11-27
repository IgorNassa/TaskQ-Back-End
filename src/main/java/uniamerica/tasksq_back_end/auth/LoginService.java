package uniamerica.tasksq_back_end.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import uniamerica.tasksq_back_end.config.JwtServiceGenerator;
import uniamerica.tasksq_back_end.dto.response.LoginResponse;
import uniamerica.tasksq_back_end.dto.response.UsuarioLogadoResponse;
import uniamerica.tasksq_back_end.entity.User;

@Service
public class LoginService {

    @Autowired
    private LoginRepository repository;

    @Autowired
    private JwtServiceGenerator jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public LoginResponse logar(Login login) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getSenha()));
        User usuario = buscarPorEmail(login.getEmail());
        String token = jwtService.generateToken(usuario);
        return new LoginResponse(token, toUsuarioLogado(usuario));
    }

    public UsuarioLogadoResponse usuarioLogado(String email) {
        return toUsuarioLogado(buscarPorEmail(email));
    }

    private User buscarPorEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
    }

    private UsuarioLogadoResponse toUsuarioLogado(User usuario) {
        return new UsuarioLogadoResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCargo().getNome(),
                usuario.getPerfil());
    }
}
