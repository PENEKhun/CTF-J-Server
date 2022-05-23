package com.penekhun.ctfjserver.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ProbListForDynamicScore {
    private Integer id;
    private Integer maxScore;
    private Integer minScore;
    private Integer solveThreshold;
    private Long solverCount;

    private Integer calculatedScore;

    public void setCalculatedScore(Integer calculatedScore) {
        this.calculatedScore = calculatedScore;
    }
}
