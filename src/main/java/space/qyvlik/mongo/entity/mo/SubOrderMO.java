package space.qyvlik.mongo.entity.mo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.math.BigDecimal;

public class SubOrderMO implements Serializable {
    @Id
    private long id;
    @Indexed(unique = false)
    private Long orderId;
    private BigDecimal amount;

    public SubOrderMO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
