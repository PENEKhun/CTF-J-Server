package com.penekhun.ctfjserver.Util;

import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.User.Repository.RankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RankSchedule {

    private final RankRepository rankRepository;

    private List<RankDto.AccountSolveProbList> accountSolveProbLists = new ArrayList<>();

    @Scheduled(fixedDelay = 5000, initialDelay = 2000)
    public void scheduleFixedRateWithInitialDelayTask() {
        long bef = System.currentTimeMillis();
        List<RankDto.ProbListForDynamicScore> probSolveCntList = rankRepository.findProbSolver();

        for (RankDto.ProbListForDynamicScore problem : probSolveCntList) {
            //점수 계산
            problem.setCalculatedScore(problem);
        }

        probSolveCntList.sort(Comparator.comparingInt(RankDto.ProbListForDynamicScore::getProblemId));

        accountSolveProbLists = rankRepository.findWhoSolveProb();

        for (RankDto.AccountSolveProbList accountSolveProbList : accountSolveProbLists) {
            List<Integer> solveList = accountSolveProbList.getProbIdList();

            for (Integer probId : solveList) {
                Integer score = probSolveCntList.get(probId-1).getCalculatedScore();
                accountSolveProbList.addScore(score);
            }
        }

        accountSolveProbLists.sort(Comparator.comparing(RankDto.AccountSolveProbList::getScore, Comparator.reverseOrder())
                .thenComparing(RankDto.AccountSolveProbList::getLastAuthTime, Comparator.naturalOrder()));

        log.info("스케쥴링 실행시간 {}",
                (System.currentTimeMillis() - bef));
    }

    public List<RankDto.AccountSolveProbList> getAccountSolveProbLists() {
        return accountSolveProbLists;
    }
}

