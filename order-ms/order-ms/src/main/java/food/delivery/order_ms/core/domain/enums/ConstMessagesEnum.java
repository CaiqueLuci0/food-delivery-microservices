package food.delivery.order_ms.core.domain.enums;

public enum ConstMessagesEnum {
    NOT_FOUND("Entidade não encontrada"),
    VALIDATION_FAILED("Dados inválidos"),
    INVALID_REQUEST("Requisição inválida"),
    DATA_INTEGRITY_VIOLATION("Violação de integridade dos dados"),
    INTERNAL_ERROR("Erro interno do servidor"),
    INVALID_CREDENTIALS("Credenciais inválidas"),
    ACCESS_DENIED("Acesso negado"),
    USER_NOT_FOUND("Usuário não encontrado"),
    INVALID_ORDER_STATUS("Status do pedido inválido para esta operação"),
    ORDER_NOT_PAYABLE("Pagamento do pedido ainda não foi aprovado"),
    CATALOG_RESOLVE_FAILED("Falha ao resolver produtos no catálogo");

    private final String messageBase;

    ConstMessagesEnum(String messageBase) {
        this.messageBase = messageBase;
    }

    public String getMessage() {
        return this.messageBase;
    }
}
