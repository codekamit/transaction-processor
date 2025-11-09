package com.orm.learn_orm.repo;

import com.orm.learn_orm.model.NetEarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface INetEarningRepo extends JpaRepository<NetEarning, String> {

    @Query("""
            SELECT DISTINCT ne
            FROM NetEarning ne
            WHERE ne.id = :id
            """)
    Optional<NetEarning> findNetEarningWithChild(
            @Param("id") String id
    );
}
