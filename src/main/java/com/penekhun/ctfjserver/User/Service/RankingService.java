package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.Util.RankSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankSchedule rankSchedule;

    public List<RankDto.RankWithTimestamp> getRankHistory(int top){
        List<RankDto.RankWithTimestamp> rankHistory = RankSchedule.everyHourScoreRank.getRankListWithTimestamp();
        List<RankDto.RankWithTimestamp> resultList = new ArrayList<>();

        List<RankDto.AccountSolveProbList> nowRank = RankSchedule.accSolveList.stream().limit(top).collect(Collectors.toList());
        for (RankDto.RankWithTimestamp rankWithTimestamp : rankHistory) {
            RankDto.RankWithTimestamp resultItem = RankDto.RankWithTimestamp.builder()
                    .timestamp(rankWithTimestamp.getTimestamp())
                    .rank(new ArrayList<>()).build();
            List<RankDto.AccountSolveProbList> i = rankWithTimestamp.getRank();
            for (RankDto.AccountSolveProbList account : i) {
                if (account.existInRank(nowRank)){
                    resultItem.addRank(account);
                }
            }
            resultList.add(resultItem);

        }


        return resultList;
    }


    public List<RankDto.AccountSolveProbList> getRank(int top){
        return rankSchedule.getRank(top);
    }
}
