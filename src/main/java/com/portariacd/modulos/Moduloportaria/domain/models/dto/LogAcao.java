package com.portariacd.modulos.Moduloportaria.domain.models.dto;


public enum LogAcao {

    // Usuário
    USUARIO_CRIADO("Criação de usuário"),
    USUARIO_EDITADO("Edição de usuário"),
    USUARIO_DELETADO("Exclusão de usuário"),
    USUARIO_ATIVADO("Usuário ativado"),
    USUARIO_DESATIVADO("Usuário desativado"),
    USUARIO_LOGIN("Login realizado"),
    USUARIO_LOGOUT("Logout realizado"),
    USUARIO_TROCOU_SENHA("Troca de senha"),
    USUARIO_RESETOU_SENHA("Reset de senha"),
    USUARIO_ERRO_LOGIN("Erro de login"),

    // Perfil
    PERFIL_ADICIONADO("Perfil adicionado ao usuário"),
    PERFIL_REMOVIDO("Perfil removido do usuário"),
    PERFIL_EDITADO("Perfil editado"),

    // Permissões
    PERMISSAO_ADICIONADA("Permissão adicionada"),
    PERMISSAO_REMOVIDA("Permissão removida"),
    PERMISSAO_EDITADA("Permissão editada"),

    // Sistema
    ACESSO_NEGADO("Acesso negado"),
    SESSAO_INVALIDA("Sessão inválida"),
    TOKEN_EXPIRADO("Token expirado"),

    // Registros
    REGISTRO_CRIADO("Registro criado"),
    REGISTRO_EDITADO("Registro editado"),
    REGISTRO_DELETADO("Registro deletado"),

    // Outros
    ACAO_DESCONHECIDA("Ação desconhecida");

    private final String descricao;

    LogAcao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
