package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.newmodel.OrderGetDto;
import kr.co.hanipaction.application.order.newmodel.OrderGetRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface OrderMapper {
    int save(OrderPostDto req);
    List<OrderGetRes> findByOrderIdAndUserId(long userId);
    List<OrderGetReq> findById(long orderId);
    int updateStatus(OrderStatusPatchReq req);
    int hideByOrderId(long orderId);
    List<OrderGetDetailRes> findOrderByStoreId(long storeId);
    List<OrderGetDetailRes> findByStoreIdAndDate(OrderDateGetReq req);


//    신규 맵퍼용
    List<OrderGetRes> findOrders(OrderGetDto dto);
}
