package space.qyvlik.mongo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable {
    private Long id;
    private BigDecimal fillAmount;
    private BigDecimal totalAmount;

    public Order() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFillAmount() {
        return fillAmount;
    }

    public void setFillAmount(BigDecimal fillAmount) {
        this.fillAmount = fillAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", fillAmount=" + fillAmount +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
