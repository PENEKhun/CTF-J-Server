package com.penekhun.ctfjserver.User.Repository;

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

    public List<Problem> findAllProblem(boolean includePrivate){
        if (includePrivate)
            return (em.createQuery("select m from Problem m", Problem.class).getResultList());
        else
            return (em.createQuery("select m from Problem m where m.isPublic = :isPublic", Problem.class)
                    .setParameter("isPublic", true).getResultList());
    }

    public Problem findById(Integer id){
        if (id != null)
            return em.find(Problem.class, id);
        else return null;
    }

}
