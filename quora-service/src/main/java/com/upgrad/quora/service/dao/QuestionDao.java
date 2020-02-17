package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/** Author : Harika Etamukkala This class will contain Question related database calls */
@Repository
public class QuestionDao {

  @PersistenceContext private EntityManager entityManager;

  /**
   * @param questionEntity
   * @return
   */
  @Transactional
  public QuestionEntity save(QuestionEntity questionEntity) {
    entityManager.persist(questionEntity);
    return questionEntity;
  }

  /** @return list of questionentity */
  @Transactional
  public List<QuestionEntity> getAllQuestions() {
    try {
      return entityManager
          .createNamedQuery("getAllQuestions", QuestionEntity.class)
          .getResultList();
    } catch (NoResultException ex) {
      return null;
    }
  }

  @Transactional
  public QuestionEntity getQuestionById(String uuid) {
    try {
      return entityManager
          .createNamedQuery("getQuestionById", QuestionEntity.class)
          .setParameter("uuid", uuid)
          .getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }

  @Transactional
  public String deleteQuestion(String uuid) {
    try {
      QuestionEntity entity = getQuestionById(uuid);
      entityManager.remove(entity);
      return entity.getUuid();
    } catch (NoResultException ex) {
      return null;
    }
  }

  @Transactional
  public List<QuestionEntity> getAllQuestionsByUser(User user) {
    try {

      return (List<QuestionEntity>)
          entityManager
              .createNamedQuery("getQuestionByUserId", QuestionEntity.class)
              .setParameter("userId", user)
              .getResultList();
    } catch (NoResultException ex) {
      return null;
    }
  }

  @Transactional
  public UserAuthToken getAuthToken(String accessToken) {
    try {
      return entityManager
          .createNamedQuery("userAuthByAccessToken", UserAuthToken.class)
          .setParameter("accessToken", accessToken)
          .getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }

  @Transactional
  public User getUser(String userUuid) throws UserNotFoundException {
    try {
      return entityManager
          .createNamedQuery("userByUuid", User.class)
          .setParameter("uuid", userUuid)
          .getSingleResult();
    } catch (NoResultException ex) {
      throw new UserNotFoundException("USR-001", "User not found");
    }
  }
}
