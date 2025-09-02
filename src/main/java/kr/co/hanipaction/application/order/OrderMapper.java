package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.order.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    int save(OrderPostDto req);
    List<OrderGetRes> findByOrderIdAndUserId(long userId);
    List<OrderGetReq> findById(long orderId);
    int updateStatus(OrderStatusPatchReq req);
    int hideByOrderId(long orderId);
    List<OrderGetDetailRes> findOrderByStoreId(long storeId);
    List<OrderGetDetailRes> findByStoreIdAndDate(OrderDateGetReq req);
}
