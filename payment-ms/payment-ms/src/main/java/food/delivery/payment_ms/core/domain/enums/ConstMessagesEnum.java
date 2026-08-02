package food.delivery.payment_ms.core.domain.enums;

public enum ConstMessagesEnum {
    NOT_FOUND("Entidade não encontrada"),
    VALIDATION_FAILED("Dados inválidos"),
    INVALID_REQUEST("Requisição inválida"),
    INTERNAL_ERROR("Erro interno do servidor"),
    INVALID_CREDENTIALS("Credenciais inválidas"),
    ACCESS_DENIED("Acesso negado"),
    INVALID_PAYMENT_STATUS("Status do pagamento inválido para esta operação");

    private final String messageBase;

    ConstMessagesEnum(String messageBase) {
        this.messageBase = messageBase;
    }

    public String getMessage() {
        return this.messageBase;
    }
}
