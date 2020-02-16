package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param answerEntity
     * @return
     */
    @Transactional
    public AnswerEntity save(AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    @Transactional
    public AnswerEntity getAnswerById(String answerId) {
        try{
            return entityManager.createNamedQuery("getAnswerById",AnswerEntity.class).setParameter("uuid",answerId).getSingleResult();
        }
        catch (NoResultException ex)
        {
            return null;
        }
    }

    @Transactional
    public String deleteAnswer(String answerId) {
        try{
            AnswerEntity answerEntity = getAnswerById(answerId);
            entityManager.remove(answerEntity);
            return answerEntity.getUuid();
        }
        catch (NoResultException ex)
        {
            return null;
        }
    }

    @Transactional
    public List<AnswerEntity> getAllAnswersForQuestion(QuestionEntity questionEntity){
        try{
            return (List<AnswerEntity>) entityManager.createNamedQuery("getAllAnswersForQuestion",AnswerEntity.class).setParameter("question",questionEntity).getResultList();
        }
        catch (NoResultException ex)
        {
            return null;
        }
    }
}
