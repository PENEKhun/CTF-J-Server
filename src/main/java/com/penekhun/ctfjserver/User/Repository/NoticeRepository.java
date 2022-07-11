package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

}
