package com.orm.learn_orm.repo;

import com.orm.learn_orm.model.NetEarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface INetEarningRepo extends JpaRepository<NetEarning, UUID> {

    @Query("""
            SELECT DISTINCT ne
            FROM NetEarning ne
            LEFT JOIN FETCH ne.earnings
            LEFT JOIN FETCH ne.settlementUpload
            WHERE ne.id = :id
            """)
    Optional<NetEarning> findNetEarningWithChild(
            @Param("id") UUID id
    );
}
