package com.suplementos.lojasuplementosapi.core;

public final class ApiConstants {
    private ApiConstants() {}
    
    public static final String API_BASE_PATH = "/api/v1";
    public static final String AUTH_PATH = API_BASE_PATH + "/auth";
    public static final String USUARIOS_PATH = API_BASE_PATH + "/usuarios";
    public static final String CATEGORIAS_PATH = API_BASE_PATH + "/categorias";
    public static final String SUPLEMENTOS_PATH = API_BASE_PATH + "/suplementos";
    public static final String PEDIDOS_PATH = API_BASE_PATH + "/pedidos";
    public static final String AVALIACOES_PATH = API_BASE_PATH + "/avaliacoes";
    
    public static final String ERRO_SUPLEMENTO_NAO_ENCONTRADO = "Suplemento não encontrado com o ID: ";
    public static final String ERRO_CATEGORIA_NAO_ENCONTRADA = "Categoria não encontrada com o ID: ";
    public static final String ERRO_USUARIO_NAO_ENCONTRADO = "Usuário não encontrado com o ID: ";
    public static final String ERRO_PEDIDO_NAO_ENCONTRADO = "Pedido não encontrado com o ID: ";
    public static final String ERRO_AVALIACAO_NAO_ENCONTRADA = "Avaliação não encontrada com o ID: ";
    
    public static final String ERRO_EMAIL_JA_EXISTE = "Email já está em uso";
    public static final String ERRO_NOME_CATEGORIA_JA_EXISTE = "Já existe uma categoria com este nome";
    public static final String ERRO_CODIGO_BARRAS_JA_EXISTE = "Já existe um suplemento com este código de barras";
}