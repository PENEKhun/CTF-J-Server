package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.User.Entity.Problem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public List<ProblemDto.Res.correctProblemList> findCorrectProblemList(){

        String sql = "select a " +
                "from Account m, AuthLog a " +
                "where a.accountIdx = m.id and a.isSuccess = true";
        List correctList = em.createQuery(sql).getResultList();

        List<ProblemDto.Res.correctProblemList> correctProblemLists = new ArrayList<>();
        correctList.forEach(s -> correctProblemLists.add(modelMapper.map(s, ProblemDto.Res.correctProblemList.class)));
        return correctProblemLists;
    }

    public List<RankDto.ProbWithDynamicScore> findProbSolver(){

        List<Object[]> resultList = em.createQuery("select a.problem, count(a.problem)  from AuthLog a where a.isSuccess = true group by a.problem").getResultList();
//        List<Object[]> resultList = em.createQuery("select a.problem.id, a.problem.maxScore, a.problem.minScore, a.problem.solveThreshold, count(a.problem), a.problem.type, a.problem.isPublic  from AuthLog a where a.isSuccess = true group by a.problem").getResultList();
        //SELECT problem_idx, count(problem_idx) FROM ctf.AuthLog where is_success=true group By problem_idx
        List<Problem> resultList2 = em.createQuery("select p from Problem p where p.id NOT IN (select a.problem from AuthLog a where a.isSuccess = true)", Problem.class).getResultList();
        //SELECT idx, max_score, min_score, solve_threshold FROM ctf.Problem where idx NOT IN (SELECT problem_idx FROM ctf.AuthLog WHERE is_success != true)
        List<RankDto.ProbWithDynamicScore> outputList = new ArrayList<>();

        if (resultList != null)
            for (Object[] row : resultList) {
                Problem problem = (Problem) row[0];
                Long countSolver = (Long) row[1];
                RankDto.ProbWithDynamicScore item = modelMapper.map(problem, RankDto.ProbWithDynamicScore.class);
                item.setSolverCount(countSolver);
                outputList.add(item);
            }

        if (resultList2 != null)
            for (Problem problem : resultList2) {
                RankDto.ProbWithDynamicScore item = modelMapper.map(problem, RankDto.ProbWithDynamicScore.class);
                item.setSolverCount(0L);
                outputList.add(item);
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
                    .probIdList(probIdList).build();
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
