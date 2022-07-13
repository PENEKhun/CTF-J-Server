package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.AuthLog;
import com.penekhun.ctfjserver.User.Entity.Problem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProblemRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Problem problem){
        em.persist(problem);
    }

    public boolean amICorrectBefore(Integer accountId, Integer problemId){
        Problem problem = findById(problemId);
        List<AuthLog> authLog =
                em.createQuery("select a from AuthLog a where a.accountIdx = :accountId and a.problem = :problem and a.isSuccess = true", AuthLog.class)
                .setParameter("problem", problem)
                .setParameter("accountId", accountId)
                .setMaxResults(1).getResultList();
        return authLog != null && !authLog.isEmpty();
    }

    public Problem findById(Long id){
        /*
            todo: change optional
         */
        if (id != null)
            return em.find(Problem.class, id);
        else return null;
    }

}
