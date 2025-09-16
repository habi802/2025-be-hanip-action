package kr.co.hanipaction.application.manager;

import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.manager.model.*;
import kr.co.hanipaction.application.manager.specification.OrderSpecification;
import kr.co.hanipaction.application.manager.specification.ReviewSpecification;
import kr.co.hanipaction.application.order.OrderMapper;
import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.review.ReviewRepository;
import kr.co.hanipaction.application.review.ReviewService;
import kr.co.hanipaction.configuration.enumcode.model.StatusType;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Review;
import kr.co.hanipaction.entity.ReviewImage;
import kr.co.hanipaction.openfeign.store.StoreClient;
import kr.co.hanipaction.openfeign.store.model.StoreInManagerRes;
import kr.co.hanipaction.openfeign.store.model.StoreListReq;
import kr.co.hanipaction.openfeign.store.model.StoreListRes;
import kr.co.hanipaction.openfeign.user.UserClient;
import kr.co.hanipaction.openfeign.user.model.UserListReq;
import kr.co.hanipaction.openfeign.user.model.UserListRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserClient userClient;
    private final StoreClient storeClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    // 주문 전체 조회
    public PageResponse<OrderListRes> getOrderList(OrderListReq req) {
        // 주문자명, 상호명 같이 외부 api를 호출해야 하는 경우 입력한 주문자명 혹은 상호명을 가진 id 리스트를 검색 조건으로 사용
        ResponseEntity<ResultResponse<Page<UserListRes>>> userListRes = userClient.getUserIdsInManager(UserListReq.builder().name(req.getUserName())
                                                                                                                            .pageNumber(0)
                                                                                                                            .pageSize(-1)
                                                                                                                            .build());
        List<Long> userIds = userListRes.getBody().getResultData().getContent().stream().map(UserListRes::getUserId).toList();

        ResponseEntity<ResultResponse<Page<StoreListRes>>> storeListRes = storeClient.getStoreIdsInManager(StoreListReq.builder().name(req.getStoreName())
                                                                                                                                 .pageNumber(0)
                                                                                                                                 .pageSize(-1)
                                                                                                                                 .build());
        List<Long> storeIds = storeListRes.getBody().getResultData().getContent().stream().map(StoreListRes::getStoreId).toList();

        // 검색 조건 적용
        Specification<Orders> spec = OrderSpecification.hasStartDate(req.getStartDate())
                                                       .and(OrderSpecification.hasEndDate(req.getEndDate()))
                                                       .and(OrderSpecification.hasUserIds(userIds))
                                                       .and(OrderSpecification.hasStoreIds(storeIds))
                                                       .and(OrderSpecification.hasAddress(req.getAddress()))
                                                       .and(OrderSpecification.hasPayment(req.getPayment()))
                                                       .and(OrderSpecification.hasIsDeleted(req.getIsDeleted()))
                                                       .and(OrderSpecification.hasStatus(req.getStatus()));

        // 페이징 및 페이지 사이즈 적용
        Pageable pageable = PageRequest.of(req.getPageNumber(), req.getPageSize());

        Page<Orders> page = orderRepository.findAll(spec, pageable);
        List<OrderListRes> result = page.stream().map(order -> {
            // 작성자명, 상호명 가져오기 위해 외부 api 호출
            ResponseEntity<ResultResponse<String>> userRes = userClient.getUserNameInManager(order.getUserId());
            ResponseEntity<ResultResponse<StoreInManagerRes>> storeRes = storeClient.getStoreNameInManager(order.getStoreId());

            return OrderListRes.builder()
                               .createdAt(order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                               .orderId(order.getId())
                               .userName(userRes.getBody().getResultData())
                               .storeName(storeRes.getBody().getResultData().getName())
                               .address(String.format("%s, %s, %s", order.getPostcode(), order.getAddress(), order.getAddressDetail()))
                               .payment(order.getPayment().getValue())
                               .status(order.getStatus().getValue())
                               .isDeleted(order.getIsDeleted())
                               .build();
        }).toList();

        return new PageResponse<>(result);
    }

    // 주문 상세 조회
    public OrderInManagerRes getOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElse(null);

        // 작성자명, 상호명 가져오기 위해 외부 api 호출
        ResponseEntity<ResultResponse<String>> userRes = userClient.getUserNameInManager(order.getUserId());
        ResponseEntity<ResultResponse<StoreInManagerRes>> storeRes = storeClient.getStoreNameInManager(order.getStoreId());

        return OrderInManagerRes.builder()
                                .orderId(orderId)
                                .userName(userRes.getBody().getResultData())
                                .storeName(storeRes.getBody().getResultData().getName())
                                .address(String.format("%s, %s, %s", order.getPostcode(), order.getAddress(), order.getAddressDetail()))
                                .amount(order.getAmount())
                                .payment(order.getPayment().getValue())
                                .status(order.getStatus().getValue())
                                .isDeleted(order.getIsDeleted())
                                .build();
    }

    // 주문 취소
    @Transactional
    public void patchStatusInOrder(List<Long> ids) {
        List<Orders> orders = orderRepository.findAllById(ids);

        for (Orders order : orders) {
            // 카카오페이 주문 취소에 대한 코드 필요(tid, 결제금액이 필요함)

            order.setStatus(StatusType.CANCELED);
        }
    }

    // 리뷰 전체 조회
    public PageResponse<ReviewListRes> getReviewList(ReviewListReq req) {
        ResponseEntity<ResultResponse<Page<UserListRes>>> userListRes = userClient.getUserIdsInManager(UserListReq.builder().name(req.getUserName())
                                                                                                                            .pageNumber(0)
                                                                                                                            .pageSize(-1)
                                                                                                                            .build());
        List<Long> userIds = userListRes.getBody().getResultData().getContent().stream().map(UserListRes::getUserId).toList();

        ResponseEntity<ResultResponse<Page<StoreListRes>>> storeListRes = storeClient.getStoreIdsInManager(StoreListReq.builder().name(req.getStoreName())
                                                                                                                                 .pageNumber(0)
                                                                                                                                 .pageSize(-1)
                                                                                                                                 .build());
        List<Long> storeIds = storeListRes.getBody().getResultData().getContent().stream().map(StoreListRes::getStoreId).toList();
        List<Long> orderIds = orderRepository.findAllByStoreIdIn(storeIds).stream().map(Orders::getId).toList();

        // 검색 조건 적용
        Specification<Review> spec = ReviewSpecification.hasStartDate(req.getStartDate())
                                                        .and(ReviewSpecification.hasEndDate(req.getEndDate()))
                                                        .and(ReviewSpecification.hasUserIds(userIds))
                                                        .and(ReviewSpecification.hasOrderIds(storeIds, orderIds))
                                                        .and(ReviewSpecification.hasComment(req.getComment()))
                                                        .and(ReviewSpecification.hasOwnerComment(req.getOwnerComment()))
                                                        .and(ReviewSpecification.hasIsHide(req.getIsHide()));

        // 페이징 및 페이지 사이즈 적용
        Pageable pageable = PageRequest.of(req.getPageNumber(), req.getPageSize());

        Page<Review> page = reviewRepository.findAll(spec, pageable);
        List<ReviewListRes> result = page.stream().map(review -> {
            ResponseEntity<ResultResponse<String>> userRes = userClient.getUserNameInManager(review.getUserId());
            ResponseEntity<ResultResponse<StoreInManagerRes>> storeRes = storeClient.getStoreNameInManager(review.getOrderId().getStoreId());

            return ReviewListRes.builder()
                                .createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                .reviewId(review.getId())
                                .userName(userRes.getBody().getResultData())
                                .storeName(storeRes.getBody().getResultData().getName())
                                .comment(review.getComment())
                                .ownerComment(review.getOwnerComment() != null && review.getOwnerComment() != "" ? 1 : 0)
                                .isHide(review.getIsHide())
                                .build();

        }).toList();

        return new PageResponse<>(result);
    }

    // 리뷰 상세 조회
    public ReviewInManagerRes getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        // 리뷰 이미지 가져오기
        List<ReviewImage> reviewImages = review.getReviewPicList();
        List<String> images = new ArrayList<>();
        for (ReviewImage image : reviewImages) {
            images.add(image.getReviewImageIds().getPic());
        }

        ResponseEntity<ResultResponse<String>> userRes = userClient.getUserNameInManager(review.getUserId());
        ResponseEntity<ResultResponse<StoreInManagerRes>> storeRes = storeClient.getStoreNameInManager(review.getOrderId().getStoreId());

        return ReviewInManagerRes.builder()
                                 .reviewId(reviewId)
                                 .userName(userRes.getBody().getResultData())
                                 .storeName(storeRes.getBody().getResultData().getName())
                                 .images(images)
                                 .comment(review.getComment())
                                 .ownerComment(review.getOwnerComment())
                                 .isHide(review.getIsHide())
                                 .build();
    }

    // 리뷰 숨기기 상태 변경
    @Transactional
    public void patchIsHideInReview(List<Long> ids, int isHide) {
        List<Review> reviews = reviewRepository.findAllById(ids);

        for (Review review : reviews) {
            review.setIsHide(isHide);

            // 평균 별점 계산
            reviewService.patchAverageRating(review.getId());
        }
    }

    // 3개의 기간을 리스트에 넣는 메소드
    public List<String> addPeriodList(String type, String date) {
        List<String> periods = new ArrayList<>(3);
        switch (type) {
            case "YEAR":
                int year = Integer.parseInt(date);
                for (int i = 2; i >= 0; i--) {
                    periods.add(String.valueOf(year - i));
                }
                break;
            case "MONTH":
                LocalDate month = LocalDate.parse(date + "-01");
                for (int i = 2; i >= 0; i--) {
                    periods.add(month.minusMonths(i).toString());
                }
                break;
            case "WEEK":
                LocalDate week = LocalDate.parse(date);
                for (int i = 2; i >= 0; i--) {
                    periods.add(week.minusWeeks(i).toString());
                }
                break;
            case "DAY":
                LocalDate day = LocalDate.parse(date);
                for (int i = 2; i >= 0; i--) {
                    periods.add(day.minusDays(i).toString());
                }
                break;
            default:
                throw new IllegalArgumentException("잘못된 날짜 타입을 입력하였습니다.");
        }

        return periods;
    }

    // 주문 건 수(총 주문 건 수, 취소된 건 수) 통계
    public List<OrderStatsRes> getOrderStats(OrderStatsReq req) {
        String type = req.getType().toUpperCase();

        // 3개의 기간이 들어가는 리스트를 만들고, 그 리스트에 선택한 날짜의 이전 2개 항목, 마지막으로 선택한 날짜를 넣는 과정(예를 들어, ?type=year&date=2025 이면 2023, 2024, 2025)
        List<String> periods = addPeriodList(type, req.getDate());

        OrderStatsDto dto = OrderStatsDto.builder()
                                         .type(type)
                                         .periods(periods)
                                         .build();

        return orderMapper.findStatsByDate(dto);
    }

    // 매출액 통계
    public List<OrderAmountStatsRes> getAmountStats(OrderAmountStatsReq req) {
        String type = req.getType().toUpperCase();

        // 3개의 기간이 들어가는 리스트를 만들고, 그 리스트에 선택한 날짜의 이전 2개 항목, 마지막으로 선택한 날짜를 넣는 과정(예를 들어, ?type=year&date=2025 이면 2023, 2024, 2025)
        List<String> periods = addPeriodList(type, req.getDate());

        OrderAmountStatsDto dto = OrderAmountStatsDto.builder()
                                                    .type(type)
                                                    .periods(periods)
                                                    .build();

        return orderMapper.findAmountStatsByDate(dto);
    }
}
