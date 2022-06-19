package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.Util.RankSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankSchedule rankSchedule;

    public RankDto.EveryHourScore getRank(int top){
        RankDto.EveryHourScore rank = rankSchedule.getRank(top);
        if (rank != null)
            for (RankDto.AccountSolveProbListWithTimestamp rankWithTime : rank.getRankListWithTimestamp()) {
                rankWithTime.setRank(
                        rankWithTime.getRank().stream().limit(top).collect(Collectors.toList())
                );
            }
        return rank;
    }
}
