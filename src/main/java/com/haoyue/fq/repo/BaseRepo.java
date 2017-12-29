package com.haoyue.fq.repo;

/**
 * Created by LiJia on 2017/12/25.
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public abstract interface BaseRepo<T, K extends Serializable> extends JpaRepository<T, K>, QueryDslPredicateExecutor<T>
{
}
