package com.penekhun.ctfjserver.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
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

        public void setCalculatedScore(ProbListForDynamicScore problem) {
            Integer max = problem.getMaxScore();
            Integer min = problem.getMinScore();
            Integer threshold = problem.getSolveThreshold();
            Long solveCount = problem.getSolverCount();

            double value = (((min - max) / Math.pow(threshold, 2)) * Math.pow(solveCount, 2)) + max;
            this.calculatedScore = (int) Math.ceil(value);
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class AccountSolveProbList {
        private Integer accountId;
        private String nickname;
        private List<Integer> probIdList;
        private Timestamp lastAuthTime;
        private int score = 0;

        public void addScore(Integer score) {
            this.score += score;
        }
    }


}
