package com.portariacd.modulos.Moduloportaria.infrastructure.validation;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements Validator{
    @Override
    public void valid(String password) {
        if(insegurePassword(password)){
            throw new RuntimeException("A senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais");

        }
    }

    private static boolean insegurePassword(@NotNull  String password) {
        return password.length() < 8
                || !containNumber(password)
                || !containsLetter(password)
                || !containsUppercase(password)
                || !containCaracter(password);

    }

    private static boolean containsLetter(String password) {
        return password.matches(".*[a-zA-Z].*");
    }

    private static boolean containCaracter(String password) {
        return password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");  // Verifica se tem pelo menos um caractere especial

    }

    private static boolean containsUppercase(String password) {
        return password.matches(".*[A-Z].*");
    }

    private static boolean containNumber(String password) {
        return password.matches(".*\\d.*");
    }
}
