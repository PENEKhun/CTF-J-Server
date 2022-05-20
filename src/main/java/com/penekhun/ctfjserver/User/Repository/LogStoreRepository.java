package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.LogStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogStoreRepository extends JpaRepository<LogStore, Long> {
}
