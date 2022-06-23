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
        private String type;
        private boolean isPublic;

        private Integer maxScore;
        private Integer minScore;
        private Integer solveThreshold;
        private Long solve;
        private List<String> solveList;

        private Integer calculatedScore;

        public void setCalculatedScore() {
            if (isPublic) {
                // 공개된 문제일 경우만 점수 계산
                double value = (((minScore - maxScore) / Math.pow(solveThreshold, 2)) * Math.pow(solve, 2)) + maxScore;
                calculatedScore = (int) Math.ceil(value);
            } else {
                calculatedScore = 0;
            }
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
        private List<Integer> solved;
        @Schema(description = "마지막으로 문제를 맞춘 시간", example = "timestamp")
        private Timestamp lastAuthTime;
        @Schema(description = "점수", example = "timestamp")
        private int score = 0;

        public void addScore(Integer score) {
            this.score += score;
        }

        public boolean existInRank(List<RankDto.AccountSolveProbList> rank){
            return rank.stream().anyMatch(account -> (account.getAccountId().equals(this.accountId)));
        }


    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class EveryHourScore{
        @Schema(description = "시간 정보")
        private List<AccountSolveProbList> nowRank;
        private List<RankWithTimestamp> rankListWithTimestamp;

        public EveryHourScore() {
            this.rankListWithTimestamp = new ArrayList<>();
        }

        public void addRankList(RankWithTimestamp rank) {
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
    public static class RankWithTimestamp {
        private String timestamp;
        private List<AccountSolveProbList> rank;

        public void addRank(AccountSolveProbList rank) {
            this.rank.add(rank);
        }

    }





}
