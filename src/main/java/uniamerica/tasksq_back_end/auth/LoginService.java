package uniamerica.tasksq_back_end.auth;

import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import uniamerica.tasksq_back_end.config.JwtServiceGenerator;
import uniamerica.tasksq_back_end.dto.response.LoginResponse;
import uniamerica.tasksq_back_end.dto.response.UsuarioLogadoResponse;
import uniamerica.tasksq_back_end.entity.User;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtServiceGenerator jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse logar(Login login) {
        if (login.getSenha().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getSenha()));
        User usuario = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(usuario);
        return new LoginResponse(token, toUsuarioLogado(usuario));
    }

    public UsuarioLogadoResponse toUsuarioLogado(User usuario) {
        return new UsuarioLogadoResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCargo().getNome(),
                usuario.getPerfil());
    }
}
