package com.portariacd.modulos.Moduloportaria.infrastructure.validation.exeption;

public class UsuarioConsumerValidation extends RuntimeException{
    private final boolean critico;
    public UsuarioConsumerValidation(String ms,boolean clitico){
        super(ms);
        this.critico = clitico;
    }
    public boolean isCritico() {
        return critico;
    }
}
