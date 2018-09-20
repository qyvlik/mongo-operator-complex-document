package space.qyvlik.mongo.entity.mo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Document
public class OrderMO implements Serializable {
    @Id
    private long id;
    private BigDecimal totalAmount;
    private Map<String, BigDecimal> fillAmountMap;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Map<String, BigDecimal> getFillAmountMap() {
        return fillAmountMap;
    }

    public void setFillAmountMap(Map<String, BigDecimal> fillAmountMap) {
        this.fillAmountMap = fillAmountMap;
    }
}
