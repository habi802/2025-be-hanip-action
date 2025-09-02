package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.order.model.OrderGetListReq;
import kr.co.hanipaction.application.order.model.OrderMenuDto;
import kr.co.hanipaction.application.order.model.OrderMenuPostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMenusMapper {
    int SaveQuantity(OrderMenuPostDto req);
    List<OrderMenuDto> findAllByOrderId(int orderId);
    List<OrderGetListReq> findAllByOrderIdFromUser(int orderId);
}
