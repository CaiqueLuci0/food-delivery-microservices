package food.delivery.user_ms.infra.adapters.outbound.security;

import food.delivery.user_ms.core.application.ports.out.PasswordEncoderOutputPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderOutputPortAdapter implements PasswordEncoderOutputPort {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderOutputPortAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
