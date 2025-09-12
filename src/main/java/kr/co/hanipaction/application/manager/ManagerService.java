package kr.co.hanipaction.application.manager;

import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.manager.model.*;
import kr.co.hanipaction.application.manager.specification.OrderSpecification;
import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.review.ReviewRepository;
import kr.co.hanipaction.application.review.ReviewService;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserClient userClient;
    private final StoreClient storeClient;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    // 주문 전체 조회
    public Page<OrderListRes> getOrderList(OrderListReq req) {
        // 주문자명, 상호명 같이 외부 api를 호출해야 하는 경우 입력한 주문자명 혹은 상호명을 가진 id 리스트를 검색 조건으로 사용
        ResponseEntity<ResultResponse<Page<UserListRes>>> userListRes = userClient.getUserIdsInManager(UserListReq.builder().name(req.getUserName()).build());
        List<Long> userIds = userListRes.getBody().getResultData().stream().map(UserListRes::getUserId).toList();

        ResponseEntity<ResultResponse<Page<StoreListRes>>> storeListRes = storeClient.getStoreIdsInManager(StoreListReq.builder().name(req.getStoreName()).build());
        List<Long> storeIds = storeListRes.getBody().getResultData().stream().map(StoreListRes::getStoreId).toList();

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
        Page<OrderListRes> result = page.map(order -> {
            // 작성자명, 상호명 가져오기 위해 외부 api 호출
            ResponseEntity<ResultResponse<String>> userRes = userClient.getUserNameInManager(order.getUserId());
            ResponseEntity<ResultResponse<StoreInManagerRes>> storeRes = storeClient.getStoreNameInManager(order.getStoreId());

            return OrderListRes.builder()
                               //.createdAt()
                               .orderId(order.getId())
                               .userName(userRes.getBody().getResultData())
                               .storeName(storeRes.getBody().getResultData().getName())
                               .address(String.format("%s, %s, %s", order.getPostcode(), order.getAddress(), order.getAddressDetail()))
                               .payment(order.getPayment().getValue())
                               .status(order.getStatus().getValue())
                               .isDeleted(order.getIsDeleted())
                               .build();
        });

        return result;
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
    public void cancelOrder() {

    }

    // 리뷰 전체 조회
    public Page<ReviewListRes> getReviewList() {
        return null;
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

        return ReviewInManagerRes.builder()
                                 .reviewId(reviewId)
                                 //.storeName()
                                 //.userName()
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
}
