package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthTokenDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBusinessService {
  @Autowired private UserDao userDao;

  @Autowired private UserAuthTokenDao userAuthTokenDao;

  public User getUserAuthByAccessToken(String accessToken, String userUuid)
          throws AuthenticationFailedException, UserNotFoundException {
    UserAuthToken userAuthToken = userAuthTokenDao.getUserAuthEntityByAccessToken(accessToken);
    if (null == userAuthToken) {
      throw new AuthenticationFailedException("ATHR-001", "User has not signed in.");
    }
    if (null == userAuthToken.getLogoutAt()) {
      throw new AuthenticationFailedException(
          "ATHR-002", "User is signed out.Sign in first to get user details");
    }
    User user = userDao.getUserByUuid(userUuid);
    if(null == user){
        throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
    }
    return user;
  }
}
