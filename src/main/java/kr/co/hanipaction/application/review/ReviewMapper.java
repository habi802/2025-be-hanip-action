package kr.co.hanipaction.application.review;

import kr.co.hanipaction.application.review.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    int save(ReviewPostReq req);
    List<ReviewGetRes> findAllByStoreIdOrderByIdDesc(long storeId);
    List<ReviewGetRes> findAllByUserIdOrderByIdDesc(long userId);
    ReviewGetRes findByorderId(long orderId);
    Integer findByReviewIdAndStoreId(ReviewPatchDto dto);
    int updateOwnerComment(ReviewPatchReq req);
    int delete(ReviewDeleteDto req);
    int modify(ReviewPutReq req);
    ReviewGetPics findByOrderIdPics(long orderId);

}
