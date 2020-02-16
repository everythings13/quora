package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthTokenDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBusinessService {

  @Autowired private UserDao userDao;
  @Autowired private UserAuthTokenDao userAuthTokenDao;

  public UserAuthToken getUserAuthByAccessToken(String accessToken, String msgWhenUserSignedOut)
      throws AuthorizationFailedException {
    UserAuthToken userAuthToken = userAuthTokenDao.getUserAuthEntityByAccessToken(accessToken);
    if (null == userAuthToken) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in.");
    }
    if (null != userAuthToken.getLogoutAt()) {
      throw new AuthorizationFailedException("ATHR-002", msgWhenUserSignedOut);
    }
    return userAuthToken;
  }

  public User getValidatedGivenUser(String userUuid, String msgWhenUserNotFound)
      throws UserNotFoundException {
    User user = userDao.getUserByUuid(userUuid);
    if (null == user) {
      throw new UserNotFoundException("USR-001", msgWhenUserNotFound);
    }
    return user;
  }
}
