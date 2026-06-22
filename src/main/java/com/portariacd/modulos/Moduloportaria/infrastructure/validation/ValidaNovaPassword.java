package com.portariacd.modulos.Moduloportaria.infrastructure.validation;

import com.portariacd.modulos.Moduloportaria.domain.models.auth.AlteraSenhaDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ValidaNovaPassword {
    private PasswordEncoder passwordEncoder;
    public ValidaNovaPassword(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;

    }
    public void valid(AlteraSenhaDTO dto,String senhaBanco){
        validaSenhaAntiga(dto.senhaAntiga(),senhaBanco);
        validnova(dto);
    }
    private void validaSenhaAntiga(String senhaaDigitada,String senhaBanco){
        if(!passwordEncoder.matches(senhaaDigitada,senhaBanco)){
            throw new RuntimeException("Senha invalida");
        }
    }
    private void validnova(AlteraSenhaDTO dto){
        if(!dto.novaSenha().equals(dto.reperteSenha())){
            throw new RuntimeException("Senhas não conferem");
        }
    }
}
