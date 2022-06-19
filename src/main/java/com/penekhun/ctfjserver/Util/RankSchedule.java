package com.penekhun.ctfjserver.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.User.Repository.RankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class RankSchedule {
    private final RankRepository rankRepository;
    private List<RankDto.AccountSolveProbList> accountSolveProbLists = new ArrayList<>();
    private List<RankDto.ProbWithDynamicScore> probSolveCntList = new ArrayList<>();

    @Scheduled(fixedDelay = 5000, initialDelay = 2000)
    public void problemAndSolverPollingTask() {
        long bef = System.currentTimeMillis();
        probSolveCntList = rankRepository.findProbSolver();

        for (RankDto.ProbWithDynamicScore problem : probSolveCntList) {
            //점수 계산
            problem.setCalculatedScore();
        }

        probSolveCntList.sort(Comparator.comparingInt(RankDto.ProbWithDynamicScore::getId));
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

    public List<RankDto.ProbWithDynamicScore> getProbSolveCntList() {
        return probSolveCntList;
    }
}

