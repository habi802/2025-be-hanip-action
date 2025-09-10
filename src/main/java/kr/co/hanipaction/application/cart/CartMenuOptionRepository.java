package kr.co.hanipaction.application.cart;

import kr.co.hanipaction.entity.CartMenuOption;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartMenuOptionRepository extends JpaRepository<CartMenuOption, Long> {
    @Query("select distinct o from CartMenuOption o " +
            "left join fetch o.children " +
            "where o.id in :optionIds")
    List<CartMenuOption> findAllWithChildren(@Param("optionIds") List<Long> optionIds);
}
