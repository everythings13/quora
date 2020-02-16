package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuthToken;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserAuthTokenDao {
  @PersistenceContext private EntityManager entityManager;

  public UserAuthToken persistUserAuthTokenEntity(final UserAuthToken userAuthToken) {
    entityManager.persist(userAuthToken);
    return userAuthToken;
  }

  public UserAuthToken getUserAuthEnityByAccessToken(final String accessToken) {
    try {
      return entityManager
          .createNamedQuery("userAuthByAccessToken", UserAuthToken.class)
          .setParameter("accessToken", accessToken)
          .getSingleResult();
    } catch (NoResultException noResultException) {
      return null;
    }
  }

  public UserAuthToken removeUserAuthTokenEntity(final UserAuthToken userAuthToken) {
    entityManager.remove(userAuthToken);
    return userAuthToken;
  }
}
