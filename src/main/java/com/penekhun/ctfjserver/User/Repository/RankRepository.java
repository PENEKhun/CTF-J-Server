package com.penekhun.ctfjserver.User.Repository;

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
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class RankRepository{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    ModelMapper modelMapper;

    public List<RankDto.ProbWithDynamicScore> findPrbSolve(){

        Query nativeQuery = em.createNativeQuery(
                "SELECT "
                +    "    *,"
                +    "    IFNULL(solve_tmp, 0) AS solverCount"
                +    "      FROM"
                +    "    Problem"
                +    "        LEFT OUTER JOIN"
                +    "    (SELECT "
                +    "         problem_idx, COUNT(DISTINCT account_idx) AS solve_tmp"
                +    "    FROM"
                +    "        AuthLog"
                +    "    WHERE"
                +    "        is_success = TRUE"
                +    "    GROUP BY problem_idx) Auth ON Problem.idx = problem_idx"
                +    "  "
                +    "  UNION"
                +    "  "
                +    "      SELECT"
                +    "        *,"
                +    "        null, null, 0 AS solveCount"
                +    "      FROM"
                +    "          Problem"
                +    "      WHERE"
                +    "          idx NOT IN (SELECT "
                +    "                problem_idx"
                +    "          FROM"
                +    "                AuthLog a"
                +    "          );"
        );

        List<Object[]> resultList = nativeQuery.getResultList();

        List<RankDto.ProbWithDynamicScore> outputList = new ArrayList<>();
        if (resultList != null)
            for (Object[] row : resultList) {
                RankDto.ProbWithDynamicScore outputItem = RankDto.ProbWithDynamicScore.builder()
                        .id((Integer) row[0])
                        .title((String) row[2])
                        .description((String) row[3])
                        .type((String) row[5])
                        .isPublic(((Byte) row[6]) != 0 )
                        .maxScore((Integer) row[7])
                        .minScore((Integer) row[8])
                        .solveThreshold((Integer) row[9])
                        .solve(((BigInteger) row[14]).longValue()).build();
                outputList.add(outputItem);
            }
        //문제마다 solveThreshold과 같은 칼럼을 가져오고 solverCount을 계산해줌. -> 아직 calculatedScore는 계산 안됨
        return outputList;
    }

    public List<RankDto.AccountSolveProbList> findWhoSolveProb(){

        List<RankDto.AccountSolveProbList> accountSolveProbLists = new ArrayList<>();
        List<Object[]> resultList = em.createQuery("SELECT a.accountIdx, a.solver.nickname, group_concat(a.problem.id ), a.solver.lastAuthTime FROM AuthLog a LEFT JOIN a.solver where a.isSuccess=true  GROUP By a.accountIdx").getResultList();
//        SELECT account_idx, GROUP_CONCAT(problem_idx) AS problem, Account.last_auth_time FROM AuthLog
//        LEFT JOIN Account ON Account.idx = account_idx
//        WHERE is_success = true GROUP By account_idx

        for (Object[] row : resultList) {
            List<String> tempProbIdList = List.of(row[2].toString().split(","));
            List<Integer> probIdList = convertStringListToIntList(tempProbIdList, Integer::parseInt);

            RankDto.AccountSolveProbList item = RankDto.AccountSolveProbList.builder().accountId( (Integer) row[0])
                    .nickname((String) row[1])
                    .lastAuthTime((Timestamp) row[3])
                    .solved(probIdList).build();
            accountSolveProbLists.add(item);
        }
        return accountSolveProbLists;
    }

    public static <T, U> List<U>
        convertStringListToIntList(List<T> listOfString,
                               Function<T, U> function) {
        return listOfString.stream()
                .map(function)
                .collect(Collectors.toList());
    }

}
