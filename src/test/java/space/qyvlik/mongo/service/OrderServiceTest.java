package space.qyvlik.mongo.service;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import space.qyvlik.mongo.entity.Order;
import space.qyvlik.mongo.entity.SubOrder;
import space.qyvlik.mongo.utils.Collections3;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    @Test
    public void saveOrder() throws Exception {
        Order order = new Order();
        order.setId(System.currentTimeMillis());
        order.setTotalAmount(new BigDecimal(100));
        order.setFillAmount(BigDecimal.ZERO);

        orderService.saveOrder(order);

        Order orderInDB = orderService.getOrder(order.getId());
        logger.info("saveOrder:{}", orderInDB);
    }


    @Test
    public void updateOrderWhenSubOrderCreate() throws Exception {
        int count = 50000;

        Order order = new Order();
        order.setId(System.currentTimeMillis());
        order.setTotalAmount(new BigDecimal(count));
        order.setFillAmount(BigDecimal.ZERO);

        orderService.saveOrder(order);

        Order orderInDB = orderService.getOrder(order.getId());
        logger.info("saveOrder:{}", orderInDB);


        long orderId = orderInDB.getId();

        long from = System.currentTimeMillis() * 100000;
        long to = from + count - 1;

        List<SubOrder> subOrderList
                = genSubOrderList(orderId, from, to, BigDecimal.ONE);

        if (subOrderList.size() > 1000) {
            List<List<SubOrder>> lists = Collections3.split(subOrderList, 1000);
            for (List<SubOrder> smallSubOrderList : lists) {
                orderService.updateOrderWhenSubOrderCreate(smallSubOrderList);
            }
        } else {
            orderService.updateOrderWhenSubOrderCreate(subOrderList);
        }

        orderInDB = orderService.getOrder(order.getId());
        logger.info("updateOrderWhenSubOrderCreate:{}", orderInDB);
    }

    private List<SubOrder> genSubOrderList(long orderId, long form, long to, BigDecimal fillAmount) {
        List<SubOrder> subOrderList = Lists.newLinkedList();

        while (to > form) {
            SubOrder subOrder = new SubOrder();
            subOrder.setId(to);
            subOrder.setOrderId(orderId);
            subOrder.setAmount(fillAmount);

            subOrderList.add(subOrder);

            to -= 1;
        }

        return Lists.newArrayList(subOrderList);
    }

}