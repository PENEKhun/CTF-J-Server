package com.penekhun.ctfjserver.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.User.Repository.RankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(readOnly=true)
public class RankSchedule {
    private final RankRepository rankRepository;
    public static List<RankDto.AccountSolveProbList> accSolveList = new ArrayList<>();
    private List<RankDto.ProbWithDynamicScore> prbSolveList = new ArrayList<>();
    public static RankDto.EveryHourScore everyHourScoreRank = new RankDto.EveryHourScore();

    public static final String RANK_HISTORY_FILENAME = "rankHistory.json";

    @Scheduled(fixedDelay = 5000, initialDelay = 2000)
    public synchronized void dynamicScorePollingTask() {
        /*
            설정한 인터벌 마다,
            동적으로 문제들의 점수를 계산해 주는 스케쥴러
         */
        long bef = System.currentTimeMillis();
        prbSolveList = rankRepository.findPrbSolve();

        //점수 계산
        prbSolveList.forEach(RankDto.ProbWithDynamicScore::setCalculatedScore);
        prbSolveList.sort(Comparator.comparingLong(RankDto.ProbWithDynamicScore::getId));
        accSolveList = rankRepository.findWhoSolveProb();

        for (RankDto.AccountSolveProbList accountSolveProbList : accSolveList) {
            List<ProblemDto.Res.CorrectProblem> solveList = accountSolveProbList.getSolved();

            solveList.stream()
                    .map(ProblemDto.Res.CorrectProblem::getId)
                    .forEach(solvedProbId ->
                            prbSolveList.stream()
                            .filter(RankDto.ProbWithDynamicScore::getIsPublic)
                            .filter(prob -> prob.getId().equals(solvedProbId))
                            .findFirst()
                            .ifPresent(
                                prob -> {
                                    // 문제정보 변수에 푼 유저 정보 기입
                                    prob.addSolver(accountSolveProbList.getNickname());
                                    // 회원정보 변수에 푼 문제 리스트 계산
                                    accountSolveProbList.addScore(prob.getCalculatedScore());
                                    accountSolveProbList.fillSolvedProblemData(prob.getId(), prob.getTitle(), prob.getType());
                                })
                        );
        }
        accSolveList.sort(Comparator.comparing(RankDto.AccountSolveProbList::getScore, Comparator.reverseOrder())
                .thenComparing(RankDto.AccountSolveProbList::getLastAuthTime, Comparator.naturalOrder()));
        long mills = System.currentTimeMillis() - bef;
        if (mills > 2000){
            log.warn("Slow Query.... takes {} ms", mills);
        }
    }

    //@Scheduled(fixedDelay = 6000, initialDelay = 100000)
//    @Scheduled(cron = "0 */5 * * * *") //임시로 5분
    @Scheduled(cron = "0 0 0/1 * * *") // 매 시간마다
    public void everyHourScoreCachingTask() {
        /*
        매 시간 마다 랭크를 기록해 두는 스케쥴러
         */
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH:mm:00");
        String nowTime = date.format(new Date());
        if (accSolveList != null
                && everyHourScoreRank.getRankListWithTimestamp().stream().noneMatch(rank -> rank.getTimestamp().equals(nowTime))){
            // accountSolveProbLists is not null && nowTime과 중복된 데이터 값이 없을때
            RankDto.RankWithTimestamp accountSolveProbListWithTimestamp = RankDto.RankWithTimestamp.builder()
                    .timestamp(nowTime)
                    .rank(accSolveList).build();
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

    public List<RankDto.AccountSolveProbList> getRank(int top) {
        return accSolveList.stream().limit(top).collect(Collectors.toList());
    }


    public List<RankDto.ProbWithDynamicScore> getPrbSolveList() {
        return prbSolveList;
    }
}

