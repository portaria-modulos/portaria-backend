package com.portariacd.modulos.Moduloportaria.infrastructure.validation.exeption;

import java.time.OffsetDateTime;

public record ErroApiUsuarioDTO(OffsetDateTime timestamp, int status, String error, String message,Boolean isAtivo, String path) {


}
