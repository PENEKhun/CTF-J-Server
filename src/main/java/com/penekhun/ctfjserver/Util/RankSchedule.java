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
    public static RankDto.EveryHourScore everyHourScoreRank = new RankDto.EveryHourScore();

    public static final String RANK_HISTORY_FILENAME = "rankHistory.json";


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
        long mills = System.currentTimeMillis() - bef;
        if (mills > 2000){
            log.warn("Slow Query.... takes {} ms", mills);
        }
    }

    @Scheduled(cron = "0 0 0/1 * * *") // 매 시간마다
    public void everyHourScoreCachingTask() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH:mm:00");
        String nowTime = date.format(new Date());
        if (accountSolveProbLists != null
                && everyHourScoreRank.getRankListWithTimestamp().stream().noneMatch(rank -> rank.getTimestamp().equals(nowTime))){
            // accountSolveProbLists is not null && nowTime과 중복된 데이터 값이 없을때
            RankDto.AccountSolveProbListWithTimestamp accountSolveProbListWithTimestamp = RankDto.AccountSolveProbListWithTimestamp.builder()
                    .timestamp(nowTime)
                    .rank(accountSolveProbLists).build();
            everyHourScoreRank.addRankList(accountSolveProbListWithTimestamp);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path path = Paths.get(RANK_HISTORY_FILENAME);
        try {
            Files.write(path,gson.toJson(everyHourScoreRank).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Rank History File save Error!!!!");
        }

        if (everyHourScoreRank.getRankListWithTimestamp().size() > 15){
            // rankHistory가 많이 싸이면 맨 마지막 history를 지워줍니다.
            everyHourScoreRank.removeOldOne();
        }
    }

    public RankDto.EveryHourScore getRank(int top) {
        everyHourScoreRank.setNowRank(accountSolveProbLists.stream().limit(top).collect(Collectors.toList()));
        return everyHourScoreRank;
    }



    public List<RankDto.AccountSolveProbList> getAccountSolveProbLists() {
        return accountSolveProbLists;
    }

    public List<RankDto.ProbWithDynamicScore> getProbSolveCntList() {
        return probSolveCntList;
    }
}

