package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.Util.RankSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankSchedule rankSchedule;

    public List<RankDto.AccountSolveProbList> getRank(int top){
        List<RankDto.AccountSolveProbList> accountSolveProbLists = rankSchedule.accountSolveProbLists;
        return accountSolveProbLists.stream().limit(top).collect(Collectors.toList());
    }
}
