package com.penekhun.ctfjserver.User.Dto;

import lombok.*;

import java.sql.Timestamp;
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
