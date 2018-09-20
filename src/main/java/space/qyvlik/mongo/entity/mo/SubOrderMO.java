package space.qyvlik.mongo.entity.mo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;

// @Document 作用在类声明上，类属性才可以被 @Indexed 标记为索引，@Id 同理
// @Document(collection="collectionName") 可以指定文档的集合名

@Document
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
