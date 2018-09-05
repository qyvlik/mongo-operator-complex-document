package space.qyvlik.mongo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class SubOrder implements Serializable {
    private Long id;
    private Long orderId;
    private BigDecimal amount;

    public SubOrder() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
