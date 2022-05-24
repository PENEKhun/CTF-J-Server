package com.penekhun.ctfjserver.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class RankDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ProbListForDynamicScore {
        private Integer problemId;
        private Integer maxScore;
        private Integer minScore;
        private Integer solveThreshold;
        private Long solverCount;

        private Integer calculatedScore;

        public void setCalculatedScore(Integer calculatedScore) {
            this.calculatedScore = calculatedScore;
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class AccountSolveProbList {
        private Integer accountId;
        private List<Integer> probIdList;
        private int score = 0;

        public void addScore(Integer score) {
            this.score += score;
        }
    }


}
