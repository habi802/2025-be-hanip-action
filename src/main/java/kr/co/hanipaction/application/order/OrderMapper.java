package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.manager.model.OrderStatsDto;
import kr.co.hanipaction.application.manager.model.OrderStatsRes;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.newmodel.DrOrderGetRes;
import kr.co.hanipaction.application.order.newmodel.OrderDetailGetRes;
import kr.co.hanipaction.application.order.newmodel.OrderGetDto;
import kr.co.hanipaction.application.order.newmodel.OrderGetRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

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
    List<OrderDetailGetRes> findOrdered(long storeId);


    List<OrderDetailGetRes> findPreParing(long storeId);
    List<OrderDetailGetRes> findDelivered(long storeId);

    List<DrOrderGetRes> findDrOrderList(long userId);

    List<OrderStatsRes> findStatsByDate(OrderStatsDto dto); // 주문 건 수(총 주문 건 수, 취소된 건 수) 통계
}
