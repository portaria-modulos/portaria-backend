package com.portariacd.modulos.Moduloportaria.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Configuration
public class ConfigurationBens {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Configuração padrão (ex: 10 minutos)
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10));

        // Configuração específica para os armários (ex: atualiza a cada 5 minutos)
        Map<String, RedisCacheConfiguration> cacheConfigurations = Map.of(
                "armarios", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5))
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 segundos para conectar
        factory.setReadTimeout(10000);    // 10 segundos para esperar a IA responder
        return new RestTemplate(factory);
    }

//    @Bean
//  public UsuarioAdpter usuarioFacture(UsuarioRepository repository,
//                                       PerfilRepository perfilRepository,
//                                       AuthenticationManager authenticationManager,
//                                       TokenConfigure tokenConfigure
//    ){
//      return new UsuarioAdpter(repository,perfilRepository,authenticationManager,tokenConfigure);
//  }

//  @Bean
//  public RegistroPortariaRepositoryAdapter registroPortariaFacture(RegistroVisitanteRepository repository
//          , VisitanteRepository visitante
//          , UsuarioRepository usuarioRepository,
//                                                                   ValidaStatusPortaria validaStatusPortaria,
//                                                                   HistoryRepository history
//  ){
//        return new RegistroPortariaRepositoryAdapter(repository,visitante,usuarioRepository,validaStatusPortaria,history);
//  }
}
