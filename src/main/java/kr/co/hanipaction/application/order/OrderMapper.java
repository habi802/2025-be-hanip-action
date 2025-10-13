package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.manager.model.OrderAmountStatsDto;
import kr.co.hanipaction.application.manager.model.OrderAmountStatsRes;
import kr.co.hanipaction.application.manager.model.OrderStatsDto;
import kr.co.hanipaction.application.manager.model.OrderStatsRes;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.model.OrderPostDto;
import kr.co.hanipaction.application.order.newmodel.*;
import kr.co.hanipaction.application.pay.naverpay.model.NaverPayOrderItemReq;
import kr.co.hanipaction.application.pay.naverpay.model.NaverProductItem;
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
    List<OrderGetByStoreIdRes> getOrderByStoreId(OrderGetByStoreIdDto dto);


    List<OrderDetailGetRes> findPreParing(long storeId);
    List<OrderDetailGetRes> findDelivered(long storeId);
    List<OrderDetailGetRes> findCompleted(long storeId);
    List<OrderDetailGetRes> findCanceled(long storeId);

    List<OrderDetailGetRes> findOrderSearchByDate(OrderStatusDto dto);

    List<DrOrderGetRes> findDrOrderList(long userId);

    Integer findCountToday(); // 금일 주문 건 수 통계
    Integer findSumToday(); // 금일 매출액 통계
    List<OrderStatsRes> findStatsByDate(OrderStatsDto dto); // 주문 건 수(총 주문 건 수, 취소된 건 수) 통계
    List<OrderAmountStatsRes> findAmountStatsByDate(OrderAmountStatsDto dto); // 매출액 통계

    // 네이버 페이 아이템 리스트용
    List<NaverPayOrderItemReq> naverPay(long userId,long orderId);
}
