package food.delivery.restaurant_ms.core.domain.enums;

public enum ConstMessagesEnum {
    NOT_FOUND("Entidade não encontrada"),
    VALIDATION_FAILED("Dados inválidos"),
    INVALID_REQUEST("Requisição inválida"),
    DATA_INTEGRITY_VIOLATION("Violação de integridade dos dados"),
    INTERNAL_ERROR("Erro interno do servidor"),
    INVALID_CREDENTIALS("Credenciais inválidas"),
    ACCESS_DENIED("Acesso negado"),
    INVALID_CEP("CEP inválido ou não encontrado"),
    INVALID_LOCATION("Localização não encontrada para o endereço"),
    OWNER_NOT_FOUND("Usuário dono não encontrado"),
    RESTAURANT_ALREADY_EXISTS("Usuário já possui um restaurante");

    private final String messageBase;

    ConstMessagesEnum(String messageBase) {
        this.messageBase = messageBase;
    }

    public String getMessage() {
        return this.messageBase;
    }
}
