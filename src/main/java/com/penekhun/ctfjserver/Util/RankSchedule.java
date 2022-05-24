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

    public List<RankDto.AccountSolveProbList> accountSolveProbLists = new ArrayList<>();

    @Scheduled(fixedDelay = 5000, initialDelay = 2000)
    public void scheduleFixedRateWithInitialDelayTask() {
        long bef = System.currentTimeMillis();
        List<RankDto.ProbListForDynamicScore> probSolveCntList = rankRepository.findProbSolver();

        for (RankDto.ProbListForDynamicScore problem : probSolveCntList) {
            problem.setCalculatedScore(dynamicScore(problem));
        }

        probSolveCntList.sort(Comparator.comparingInt(RankDto.ProbListForDynamicScore::getProblemId));

        List<RankDto.AccountSolveProbList> accountSolveProbLists = rankRepository.findWhoSolveProb();

        for (RankDto.AccountSolveProbList accountSolveProbList : accountSolveProbLists) {
            //Integer id = accountSolveProbList.getAccountId();
            List<Integer> solveList = accountSolveProbList.getProbIdList();

            for (Integer probId : solveList) {
                Integer score = probSolveCntList.get(probId-1).getCalculatedScore();
                accountSolveProbList.addScore(score);
            }

        }

        accountSolveProbLists.sort(Comparator.comparing(RankDto.AccountSolveProbList::getScore, Comparator.reverseOrder()));

        log.info("스케쥴링 실행시간 {}",
                (System.currentTimeMillis() - bef));
    }

    public Integer dynamicScore(RankDto.ProbListForDynamicScore problem){
        Integer max = problem.getMaxScore();
        Integer min = problem.getMinScore();
        Integer threshold = problem.getSolveThreshold();
        Long solveCount = problem.getSolverCount();

        double value = (((min - max) / Math.pow(threshold, 2)) * Math.pow(solveCount, 2)) + max;

        return (int) Math.ceil(value);
    }
}

