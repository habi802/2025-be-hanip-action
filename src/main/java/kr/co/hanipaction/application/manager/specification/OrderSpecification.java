package kr.co.hanipaction.application.manager.specification;

import kr.co.hanipaction.configuration.enumcode.model.OrdersType;
import kr.co.hanipaction.configuration.enumcode.model.PaymentType;
import kr.co.hanipaction.configuration.enumcode.model.StatusType;
import kr.co.hanipaction.entity.Orders;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSpecification {
    // 주문 등록일(시작)
    public static Specification<Orders> hasStartDate(String startDate) {
        return (root, query, cb) -> {
            if (startDate == null || startDate.isEmpty()) {
                return null;
            }

            LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
            return cb.greaterThanOrEqualTo(root.get("createdAt"), startDateTime);
        };
    }

    // 주문 등록일(종료)
    public static Specification<Orders> hasEndDate(String endDate) {
        return (root, query, cb) -> {
            if (endDate == null || endDate.isEmpty()) {
                return null;
            }

            LocalDateTime endDateTime = LocalDate.parse(endDate).atStartOfDay();
            return cb.greaterThanOrEqualTo(root.get("createdAt"), endDateTime);
        };
    }

    // 주문자명
    public static Specification<Orders> hasUserIds(List<Long> userIds) {
        return (root, query, cb) -> {
            if (userIds == null || userIds.isEmpty()) {
                return null;
            }

            return root.get("userId").in(userIds);
        };
    }

    // 상호명
    public static Specification<Orders> hasStoreIds(List<Long> storeIds) {
        return (root, query, cb) -> {
            if (storeIds == null || storeIds.isEmpty()) {
                return null;
            }

            return root.get("storeId").in(storeIds);
        };
    }

    // 배달 주소
    public static Specification<Orders> hasAddress(String address) {
        return (root, query, cb) -> {
            if (address == null || address.isEmpty()) {
                return null;
            }

            return cb.or(
                    cb.like(root.get("postcode"), "%" + address + "%"),
                    cb.like(root.get("address"), "%" + address + "%"),
                    cb.like(root.get("addressDetail"), "%" + address + "%")
            );
        };
    }

    // 결제 방식
    public static Specification<Orders> hasPayment(String payment) {
        return (root, query, cb) -> {
            if (payment == null || payment.isEmpty()) {
                return null;
            }

            OrdersType ordersType = OrdersType.valueOfCode(payment);
            return cb.equal(root.get("payment"), ordersType);
        };
    }

    // 삭제 여부
    public static Specification<Orders> hasIsDeleted(Integer isDeleted) {
        return (root, query, cb) -> {
            if (isDeleted == null) {
                return null;
            }

            return cb.equal(root.get("isDeleted"), isDeleted);
        };
    }

    // 주문 상태
    public static Specification<Orders> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) {
                return null;
            }

            StatusType statusType = StatusType.valueOfCode(status);
            return cb.equal(root.get("status"), statusType);
        };
    }

    // 결제 상태
}
