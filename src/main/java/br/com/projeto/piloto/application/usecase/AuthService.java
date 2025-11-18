package br.com.projeto.piloto.application.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.projeto.piloto.domain.model.User;
import br.com.projeto.piloto.domain.port.inbound.AuthUseCasePort;
import br.com.projeto.piloto.domain.port.inbound.UserUseCasePort;

@Service
public class AuthService implements AuthUseCasePort {

    private final UserUseCasePort userUseCasePort;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserUseCasePort userUseCasePort, PasswordEncoder passwordEncoder) {
        this.userUseCasePort = userUseCasePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        return userUseCasePort.save(user);
    }

    @Override
    public User authenticate(String login, String senha) {

        User user = userUseCasePort.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        return user;
    }
}
