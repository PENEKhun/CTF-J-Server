package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Dto.RankDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RankRepository{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    ModelMapper modelMapper;

    public List<RankDto.ProbWithDynamicScore> findPrbSolve(){

        Query nativeQuery = em.createNativeQuery(
            "    SELECT"
            +"            *,"
            +"        IFNULL(solve_tmp, 0) AS solverCount"
            +"    FROM"
            +"    Problem"
                    // 정답자 수 계산 및 조인
            +"    LEFT OUTER JOIN ("
            +"            SELECT"
            +"            problem_idx,"
            +"            COUNT(DISTINCT account_idx) AS solve_tmp"
            +"            FROM"
            +"            AuthLog"
            +"            WHERE"
            +"            is_success = TRUE"
            +"            GROUP BY"
            +"            problem_idx"
            +"    ) Auth ON Problem.idx = problem_idx"
                    // 문제 작성자 정보 조인
            +"    LEFT OUTER JOIN("
            +"            SELECT"
            +"            idx as accIdx,"
            +"            nickname"
            +"            FROM"
            +"            Account"
            +"    ) Acc On Problem.author_id = Acc.accIdx"
                    // 문제 파일 조인
            +"    LEFT OUTER JOIN("
            +"            SELECT"
            +"            idx as fileIdx,"
            +"            problem_idx as problemIdx,"
            +"            file_name_for_display"
            +"            FROM"
            +"            ProblemFile"
            +"    ) PrblmFile On Problem.idx = problemIdx");

        List<Object[]> resultList = nativeQuery.getResultList();

        List<RankDto.ProbWithDynamicScore> outputList = new ArrayList<>();
        if (resultList != null)
            for (Object[] row : resultList) {
                RankDto.ProbWithDynamicScore outputItem = RankDto.ProbWithDynamicScore.builder()
                        .id(((BigInteger) row[0]).longValue())
                        .authorId(((BigInteger) row[1]).longValue())
                        .title((String) row[2])
                        .description((String) row[3])
                        .flag((String) row[4])
                        .type((String) row[5])
                        .isPublic((Boolean) row[6])
                        .maxScore((Integer) row[7])
                        .minScore((Integer) row[8])
                        .solveThreshold((Integer) row[9])
                        .modifyTime((Timestamp) row[11])
                        .author((String) row[15])
                        .fileIdx((((BigInteger) row[16]) == null) ? null: ((BigInteger) row[16]).longValue())
                        .fileName((String) row[18])
                        .solve(((BigInteger) row[19]).longValue())
                        .build();
                outputList.add(outputItem);
            }
        //문제마다 solveThreshold과 같은 칼럼을 가져오고 solverCount을 계산해줌. -> 아직 calculatedScore는 계산 안됨
        return outputList;
    }

    public List<RankDto.AccountSolveProbList> findWhoSolveProb(){

        List<RankDto.AccountSolveProbList> accountSolveProbLists = new ArrayList<>();
        List<Object[]> resultList = em.createQuery("SELECT a.accountIdx, a.solver.nickname, group_concat(a.problem.id ), a.solver.lastAuthTime FROM AuthLog a LEFT JOIN a.solver where a.isSuccess=true  GROUP By a.accountIdx").getResultList();

        for (Object[] row : resultList) {
            List<String> tempProbIdList = List.of(row[2].toString().split(","));
            List<ProblemDto.Res.CorrectProblem> correctProblemList = new ArrayList<>();
            tempProbIdList.forEach(probId ->
                    correctProblemList.add(
                            ProblemDto.Res.CorrectProblem.builder().id(Long.valueOf(probId)).build()
                            ));

            RankDto.AccountSolveProbList item = RankDto.AccountSolveProbList.builder().accountId( (Long) row[0])
                    .nickname((String) row[1])
                    .lastAuthTime((Timestamp) row[3])
                    .solved(correctProblemList)
                    .build();
            accountSolveProbLists.add(item);
        }
        return accountSolveProbLists;
    }

}
