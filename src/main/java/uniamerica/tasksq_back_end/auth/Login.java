package uniamerica.tasksq_back_end.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Login {

    @Email
    @NotBlank
    @Size(max = 250)
    private String email;

    @NotBlank
    @Size(max = 72)
    private String senha;
}
