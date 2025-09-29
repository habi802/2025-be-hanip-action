package kr.co.hanipaction.application.manager.specification;

import kr.co.hanipaction.entity.Review;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewSpecification {
    // 리뷰 등록일(시작)
    public static Specification<Review> hasStartDate(String startDate) {
        return (root, query, cb) -> {
            if (startDate == null || startDate.isEmpty()) {
                return null;
            }

            LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
            return cb.greaterThanOrEqualTo(root.get("createdAt"), startDateTime);
        };
    }

    // 리뷰 등록일(종료)
    public static Specification<Review> hasEndDate(String endDate) {
        return (root, query, cb) -> {
            if (endDate == null || endDate.isEmpty()) {
                return null;
            }

            LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(23, 59, 59);
            return cb.lessThanOrEqualTo(root.get("createdAt"), endDateTime);
        };
    }

    // 작성자명
    public static Specification<Review> hasUserIds(List<Long> userIds) {
        return (root, query, cb) -> {
            if (userIds == null) {
                return null;
            }

            return root.get("userId").in(userIds);
        };
    }

    // 상호명
    public static Specification<Review> hasOrderIds(List<Long> orderIds) {
        return (root, query, cb) -> {
            if (orderIds == null) {
                return null;
            }

            return root.get("orderId").get("id").in(orderIds);
        };
    }

    // 내용
    public static Specification<Review> hasComment(String comment) {
        return (root, query, cb) -> {
            if (comment == null || comment.isEmpty()) {
                return null;
            }

            return cb.like(root.get("comment"), "%" + comment + "%");
        };
    }

    // 사장 답변 등록 여부
    public static Specification<Review> hasOwnerComment(String ownerComment) {
        return (root, query, cb) -> {
            if (ownerComment == null) {
                return null;
            }

            if (Integer.valueOf(ownerComment) == 0) {
                return cb.or(cb.isNull(root.get("ownerComment")), cb.equal(root.get("ownerComment"), ""));
            } else {
                return cb.and(cb.isNotNull(root.get("ownerComment")), cb.notEqual(root.get("ownerComment"), ""));
            }
        };
    }

    // 숨김 상태
    public static Specification<Review> hasIsHide(String isHide) {
        return (root, query, cb) -> {
            if (isHide == null || isHide.isEmpty()) {
                return null;
            }

            return cb.equal(root.get("isHide"), Integer.valueOf(isHide));
        };
    }
}
