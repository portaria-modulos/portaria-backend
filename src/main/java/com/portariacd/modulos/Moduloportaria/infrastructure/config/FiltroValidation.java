package com.portariacd.modulos.Moduloportaria.infrastructure.config;

import com.auth0.jwt.interfaces.Claim;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioEntity;
import com.portariacd.modulos.Moduloportaria.infrastructure.persistence.UsuarioRepository;
import com.portariacd.modulos.Moduloportaria.infrastructure.validation.ValidationUsuarioHandle;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class
FiltroValidation extends OncePerRequestFilter {
    private UsuarioRepository repository;
    private TokenConfigure tokenConfigure;
    public  FiltroValidation(UsuarioRepository usuarioRepository,TokenConfigure tokenConfigure){
        this.repository = usuarioRepository;
        this.tokenConfigure = tokenConfigure;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods",
                "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");

        // Se for uma preflight request, responde direto e não continua o filtro
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        String token = ValidaToken(request);
        if (token != null) {
            Map<String, Claim> claims = tokenConfigure.validaTokenAuth(token);
            String sessionIdToken = claims.get("sessionId").asString();

            var authservice = tokenConfigure.validaToken(token);
            UsuarioEntity usuario = repository.findOneByEmail(authservice).orElseThrow(
                    ()-> new ValidationUsuarioHandle("Usuario não encontrado")
            );
            if(usuario.getSessionExpiresAt().isBefore(LocalDateTime.now())){
                enviarErroJson(response, HttpServletResponse.SC_FORBIDDEN,
                        "Sessão expirada. Faça login novamente.");
                return; // IMPORTANTE
            }

            if (!usuario.getCurrentSession().equals(sessionIdToken)) {
                enviarErroJson(response, HttpServletResponse.SC_FORBIDDEN,
                        "Detectamos um acesso em outro dispositivo. Sua sessão atual foi encerrada por segurança.");
                return; // IMPORTANTE
            }
            if(!usuario.getAtivo() || !usuario.isAccountNonLocked()){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    throw new ValidationUsuarioHandle("Usuario bloqueado");
            }
                var auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

        }
        filterChain.doFilter(request,response);
    }
    private String ValidaToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if(auth!=null){
            return auth.replace("Bearer","").trim();
        }
        return null;
    }

    private void enviarErroJson(HttpServletResponse response, int status, String msg) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + msg + "\"}");
        response.getWriter().flush();
    }

}
