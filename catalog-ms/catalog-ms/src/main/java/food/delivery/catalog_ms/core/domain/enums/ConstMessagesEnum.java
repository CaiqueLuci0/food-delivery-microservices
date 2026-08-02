package food.delivery.catalog_ms.core.domain.enums;

public enum ConstMessagesEnum {
    NOT_FOUND("Entidade não encontrada"),
    VALIDATION_FAILED("Dados inválidos"),
    INVALID_REQUEST("Requisição inválida"),
    DATA_INTEGRITY_VIOLATION("Violação de integridade dos dados"),
    INTERNAL_ERROR("Erro interno do servidor"),
    INVALID_CREDENTIALS("Credenciais inválidas"),
    ACCESS_DENIED("Acesso negado"),
    RESTAURANT_REFERENCE_NOT_FOUND("Referência de restaurante não encontrada"),
    PRODUCTS_DIFFERENT_RESTAURANT("Produtos pertencem a restaurantes diferentes");

    private final String messageBase;

    ConstMessagesEnum(String messageBase) {
        this.messageBase = messageBase;
    }

    public String getMessage() {
        return this.messageBase;
    }
}
