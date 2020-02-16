package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

  @PersistenceContext private EntityManager entityManager;

  public User createUser(User user) {
    entityManager.persist(user);
    return user;
  }

  public User getUserByEmail(final String email) {
    try {
      return entityManager
          .createNamedQuery("userByEmail", User.class)
          .setParameter("email", email)
          .getSingleResult();
    } catch (NoResultException noResultException) {
      return null;
    }
  }

  public User getUserByUsername(final String username) {
    try {
      return entityManager
          .createNamedQuery("userByUsername", User.class)
          .setParameter("username", username)
          .getSingleResult();
    } catch (NoResultException noResultException) {
      return null;
    }
  }

  public User getUserByUuid(String userUuid) {
    try {
      return entityManager
          .createNamedQuery("userByUuid", User.class)
          .setParameter("uuid", userUuid)
          .getSingleResult();
    } catch (NoResultException noResultException) {
      return null;
    }
  }

  public User removeUser(User user) {
    entityManager.remove(user);
    return user;
  }
}
