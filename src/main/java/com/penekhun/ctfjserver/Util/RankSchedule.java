package com.penekhun.ctfjserver.Util;

import com.penekhun.ctfjserver.User.Dto.ProbListForDynamicScore;
import com.penekhun.ctfjserver.User.Repository.RankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RankSchedule {

    private final RankRepository rankRepository;

    private List<ProbListForDynamicScore> problemList = new ArrayList<>();

    @Scheduled(fixedDelay = 2000, initialDelay = 2000)
    public void scheduleFixedRateWithInitialDelayTask() {
        long bef = System.currentTimeMillis();
        problemList = rankRepository.findAllProbSolver();

        for (ProbListForDynamicScore problem : problemList) {
            problem.setCalculatedScore(dynamicScore(problem));
        }

        for (ProbListForDynamicScore solver : problemList) {
            log.info("문제 : {}, 솔버 : {}, 점수 : {}", solver.getId(), solver.getSolverCount(), solver.getCalculatedScore());
        }


        log.info("스케쥴링 실행시간 {}",
                (System.currentTimeMillis() - bef));
    }

    public Integer dynamicScore(ProbListForDynamicScore problem){
        Integer max = problem.getMaxScore();
        Integer min = problem.getMinScore();
        Integer threshold = problem.getSolveThreshold();
        Long solveCount = problem.getSolverCount();

        double value = (((min - max) / Math.pow(threshold, 2)) * Math.pow(solveCount, 2)) + max;
        int calculatedScore = (int) Math.ceil(value);

        return calculatedScore;

    }

    public List<ProbListForDynamicScore> getProblemListForDynamicScore() {
        return problemList;
    }
}

