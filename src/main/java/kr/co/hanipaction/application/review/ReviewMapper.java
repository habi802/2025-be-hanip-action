package kr.co.hanipaction.application.review;

import kr.co.hanipaction.application.review.model.*;
import kr.co.hanipaction.application.review.model.newModal.ReviewGetByStroeIdOwner;
import kr.co.hanipaction.application.review.model.newModal.ReviewGetDto;
import kr.co.hanipaction.application.review.model.newModal.ReviewGetLengthDto;
import kr.co.hanipaction.application.review.model.newModal.ReviewGetLengthRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    int save(ReviewPostReq req);
    List<ReviewGetRes> findAllByStoreIdOrderByIdDesc(long storeId);
    List<ReviewGetRatingRes> findByStoreIdAllReview(long storeId);
    List<ReviewGetRes> findAllByUserIdOrderByIdDesc(long userId);
    ReviewGetRes findByorderId(long orderId);
    Integer findByReviewIdAndStoreId(ReviewPatchDto dto);
    int updateOwnerComment(ReviewPatchReq req);
    int delete(ReviewDeleteDto req);
    int modify(ReviewPutReq req);
    ReviewGetPics findByOrderIdPics(long orderId);

    // 신규 맵퍼
    List<ReviewGetByStroeIdOwner> ownerComment(long storeId);
    List<ReviewGetRes> reviewGetByStoreId(ReviewGetDto dto);
    List<ReviewGetLengthRes> reviewLength(long storeId);

}
