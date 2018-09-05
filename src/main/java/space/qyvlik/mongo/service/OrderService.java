package space.qyvlik.mongo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import space.qyvlik.mongo.entity.Order;
import space.qyvlik.mongo.entity.SubOrder;
import space.qyvlik.mongo.entity.mo.OrderMO;
import space.qyvlik.mongo.entity.mo.SubOrderMO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    public static final String ORDER = "qyvlik.space.Order";
    public static final String SUB_ORDER = "qyvlik.space.SubOrder";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateOrderWhenSubOrderCreate(List<SubOrder> subOrderList) {
        if (subOrderList == null || subOrderList.isEmpty()) {
            return;
        }

        StopWatch stopWatch = new StopWatch("updateOrderWhenSubOrderCreate");

        stopWatch.start("removeRepeat");

        List<Long> idList = Lists.newLinkedList();
        for (SubOrder subOrder : subOrderList) {
            idList.add(subOrder.getId());
        }
        Query query = Query.query(Criteria.where("_id").in(idList));
        List<SubOrderMO> subOrderMOList =
                mongoTemplate.find(query, SubOrderMO.class, SUB_ORDER);
        Map<Long, SubOrderMO> subOrderMOMap = Maps.newHashMap();
        for (SubOrderMO subOrderMO : subOrderMOList) {
            subOrderMOMap.put(subOrderMO.getId(), subOrderMO);
        }

        Map<Long, Update> orderUpdateMap = Maps.newHashMap();
        List<SubOrderMO> needSaveSubOrderMOList = Lists.newLinkedList();
        for (SubOrder subOrder : subOrderList) {

            if (subOrderMOMap.get(subOrder.getId()) != null) {
                continue;
            }

            SubOrderMO subOrderMO = adaptor(subOrder);
            needSaveSubOrderMOList.add(subOrderMO);

            Update update = orderUpdateMap.get(subOrder.getOrderId());
            if (update == null) {
                update = new Update();
            }
            update.set("fillAmountMap." + subOrder.getId(), subOrder.getAmount());
            orderUpdateMap.put(subOrder.getOrderId(), update);
        }

        stopWatch.stop();


        stopWatch.start("updateOrder");
        //! note must update the order first!
        for (Map.Entry<Long, Update> entry : orderUpdateMap.entrySet()) {
            Query orderQuery = Query.query(
                    Criteria.where("_id").is(entry.getKey())
            );
            Update update = entry.getValue();

            mongoTemplate.updateFirst(orderQuery, update, ORDER);
        }
        stopWatch.stop();


        stopWatch.start("saveSubOrderList");
        mongoTemplate.insert(needSaveSubOrderMOList, SUB_ORDER);
        stopWatch.stop();

        logger.info("updateOrderWhenSubOrderCreate:{}", stopWatch.prettyPrint());
    }

    public void saveOrder(Order order) {
        OrderMO orderMO = new OrderMO();
        orderMO.setId(order.getId());
        orderMO.setTotalAmount(order.getTotalAmount());
        mongoTemplate.save(orderMO, ORDER);
    }

    public Order getOrder(Long id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        OrderMO orderMO = mongoTemplate.findOne(query, OrderMO.class, ORDER);
        return adaptor(orderMO);
    }

    private Order adaptor(OrderMO orderMO) {
        Order order = new Order();

        order.setId(orderMO.getId());
        order.setTotalAmount(orderMO.getTotalAmount());

        if (orderMO.getFillAmountMap() == null || orderMO.getFillAmountMap().isEmpty()) {
            order.setFillAmount(BigDecimal.ZERO);
        } else {
            BigDecimal sum = BigDecimal.ZERO;
            for (BigDecimal fillAmount : orderMO.getFillAmountMap().values()) {
                sum = sum.add(fillAmount);
            }
            order.setFillAmount(sum);
        }

        return order;
    }

    private SubOrderMO adaptor(SubOrder subOrder) {
        SubOrderMO subOrderMO = new SubOrderMO();
        subOrderMO.setId(subOrder.getId());
        subOrderMO.setOrderId(subOrder.getOrderId());
        subOrderMO.setAmount(subOrder.getAmount());
        return subOrderMO;
    }

}
