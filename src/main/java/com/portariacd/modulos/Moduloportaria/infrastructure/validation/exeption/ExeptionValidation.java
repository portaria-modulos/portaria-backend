package com.portariacd.modulos.Moduloportaria.infrastructure.validation.exeption;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.portariacd.modulos.Moduloportaria.infrastructure.validation.ValidationUsuarioHandle;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExeptionValidation {
//
//    @ExceptionHandler(TokenExpiredException.class)
//    public ResponseEntity<ErroApiDTO> tokenValid(TokenExpiredException ex, HttpServletRequest request) {
//        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request, "Sessão expirada");
//    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<?> handleJwtExpired(JWTVerificationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Sessão expirada",
                        "message", ex.getMessage()
                ));
    }
    @ExceptionHandler(ValidationUsuarioHandle.class)
    public ResponseEntity<ErroApiDTO> handleUsuarioBloqueado(ValidationUsuarioHandle ex,HttpServletRequest resquest) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, resquest, ex.getMessage());

    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroApiDTO> handleBadCredentials(BadCredentialsException ex, HttpServletRequest resquest) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, resquest, "Email ou Senha invalidos");
    }
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErroApiDTO> handleSecurityException(SecurityException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Acesso negado");
        body.put("message", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request, ex.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErroApiDTO> validation(RuntimeException exception, HttpServletRequest request){
        return buildErrorResponse(exception, HttpStatus.UNAUTHORIZED, request,exception.getMessage());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(errors);
    }
    private ResponseEntity<ErroApiDTO> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request, String msg) {
        ErroApiDTO errorDTO = new ErroApiDTO(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                msg,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(errorDTO);
    }
}
