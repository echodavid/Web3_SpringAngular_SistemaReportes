package com.reportesciudadanos.config;

import com.reportesciudadanos.entity.Usuario;
import com.reportesciudadanos.enums.RoleEnum;
import com.reportesciudadanos.repository.RolRepository;
import com.reportesciudadanos.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Solo laboratorio: no habilitar el perfil demo fuera del entorno local. */
@Configuration
@Profile("demo")
public class DemoDataConfig {
  @Bean
  CommandLineRunner demoUsers(UsuarioRepository users, RolRepository roles,
      PasswordEncoder encoder, @Value("${DEMO_PASSWORD}") String password) {
    return args -> {
      if (password.length() < 12 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72)
        throw new IllegalArgumentException("DEMO_PASSWORD debe tener de 12 a 72 bytes");
      for (RoleEnum role : RoleEnum.values()) {
        String email = role.name().toLowerCase(java.util.Locale.ROOT) + "@example.test";
        if (!users.findByCorreoElectronico(email).isPresent()) {
          Usuario user = new Usuario();
          user.setNombre("Cuenta sintética " + role.name());
          user.setCorreoElectronico(email);
          user.setPasswordHash(encoder.encode(password));
          user.setRol(roles.findByNombre(role).orElseThrow());
          users.save(user);
        }
      }
    };
  }
}
