package com.example.api.Repository;

import com.example.api.Entity.BoothCollaboratorList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothCollaboratorListRepository extends JpaRepository<BoothCollaboratorList, Integer> {

    // 這邊要用entity中存在的欄位名命名，參數也是
    // JPA根據方法名產生SQL查詢的特殊用法
    boolean existsByIdAndCollaborators_UserAccount(Integer id, String userAccount);
}
