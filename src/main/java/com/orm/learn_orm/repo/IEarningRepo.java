package com.orm.learn_orm.repo;

import com.orm.learn_orm.model.Earning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEarningRepo extends JpaRepository<Earning, Long> {
}
