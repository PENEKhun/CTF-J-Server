package com.penekhun.ctfjserver.Util;

import com.penekhun.ctfjserver.User.Dto.ProbListForDynamicScore;
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

    private List<ProbListForDynamicScore> probSolveCntList = new ArrayList<>();
 //   private List<ProbListForDynamicScore> memberSolveList = new ArrayList<>();

    @Scheduled(fixedDelay = 2000, initialDelay = 2000)
    public void scheduleFixedRateWithInitialDelayTask() {
        long bef = System.currentTimeMillis();
        probSolveCntList = rankRepository.findProbSolver();

        for (ProbListForDynamicScore problem : probSolveCntList) {
            problem.setCalculatedScore(dynamicScore(problem));
        }

        probSolveCntList.sort(Comparator.comparingInt(ProbListForDynamicScore::getId));


        log.info("스케쥴링 실행시간 {}",
                (System.currentTimeMillis() - bef));
    }

    public Integer dynamicScore(ProbListForDynamicScore problem){
        Integer max = problem.getMaxScore();
        Integer min = problem.getMinScore();
        Integer threshold = problem.getSolveThreshold();
        Long solveCount = problem.getSolverCount();

        double value = (((min - max) / Math.pow(threshold, 2)) * Math.pow(solveCount, 2)) + max;

        return (int) Math.ceil(value);
    }

    public List<ProbListForDynamicScore> getProblemListForDynamicScore() {
        return probSolveCntList;
    }
}

