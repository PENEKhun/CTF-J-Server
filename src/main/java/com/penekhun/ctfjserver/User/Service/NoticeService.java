package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.User.Entity.Notice;
import com.penekhun.ctfjserver.User.Repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public List<Notice> getNoticeAll(){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return noticeRepository.findAll(sort);
    }

}
