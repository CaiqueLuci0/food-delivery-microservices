package food.delivery.payment_ms.infra.adapters.outbound.persistence.mongock;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@ChangeUnit(id = "001-payment-orderId-unique", order = "001", author = "payment-ms")
public class V001PaymentOrderIdUniqueIndex {

    @Execution
    public void execution(MongoTemplate mongoTemplate) {
        mongoTemplate.indexOps("payment")
                .ensureIndex(new Index().on("orderId", Sort.Direction.ASC).unique());
    }

    @RollbackExecution
    public void rollback() {
    }
}
