package com.penekhun.ctfjserver.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RankDto {

    @Builder
    @Setter @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProbWithDynamicScore {
        private Integer id;
        private String title;
        private String description;
        private String writer;

        private Integer maxScore;
        private Integer minScore;
        private Integer solveThreshold;
        private Long solverCount;
        private String type;
        private List<String> solverList;
        private boolean isPublic;

        private Integer calculatedScore;

        public void setCalculatedScore() {
            double value = (((minScore - maxScore) / Math.pow(solveThreshold, 2)) * Math.pow(solverCount, 2)) + maxScore;
            calculatedScore = (int) Math.ceil(value);
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class AccountSolveProbList {
        @Schema(description = "계정 인덱스")
        private Integer accountId;
        @Schema(description = "유저 닉네임")
        private String nickname;
        @Schema(description = "푼 문제 리스트", example = "{int, int, int, }")
        private List<Integer> probIdList;
        @Schema(description = "마지막으로 문제를 맞춘 시간", example = "timestamp")
        private Timestamp lastAuthTime;
        @Schema(description = "점수", example = "timestamp")
        private int score = 0;

        public void addScore(Integer score) {
            this.score += score;
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class EveryHourScore{
        @Schema(description = "시간 정보")
        private List<AccountSolveProbList> nowRank;
        private List<AccountSolveProbListWithTimestamp> rankListWithTimestamp;

        public EveryHourScore() {
            this.rankListWithTimestamp = new ArrayList<>();
        }

        public void addRankList(AccountSolveProbListWithTimestamp rank) {
            rankListWithTimestamp.add(rank);
        }

        public void setNowRank(List<AccountSolveProbList> now) {
            this.nowRank = now;
        }

        public void removeOldOne(){
            this.rankListWithTimestamp.remove(0);
        }
    }

    @Builder
    @Getter @Setter
    @AllArgsConstructor
    public static class AccountSolveProbListWithTimestamp {
        private String timestamp;
        private List<AccountSolveProbList> rank;
    }





}
