package uniamerica.tasksq_back_end.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniamerica.tasksq_back_end.dto.response.LoginResponse;
import uniamerica.tasksq_back_end.dto.response.UsuarioLogadoResponse;
import uniamerica.tasksq_back_end.entity.User;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> logar(@RequestBody Login login) {
        return new ResponseEntity<>(loginService.logar(login), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioLogadoResponse> usuarioLogado(@AuthenticationPrincipal User usuario) {
        return new ResponseEntity<>(loginService.toUsuarioLogado(usuario), HttpStatus.OK);
    }
}
