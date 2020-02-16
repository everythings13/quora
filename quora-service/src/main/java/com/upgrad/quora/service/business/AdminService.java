package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

  private static String ADMIN = "admin";
  @Autowired private UserDao userDao;

  public void validateIfRoleIsAdmin(UserAuthToken userAuthToken, String uuid)
      throws AuthorizationFailedException {
    if (!userAuthToken.getUser().getRole().equals(ADMIN)) {
      throw new AuthorizationFailedException(
          "ATHR-003", "Unauthorized Access, Entered user is not an admin");
    }
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public String deleteUser(User user) {
    return userDao.removeUser(user).getUuid();
  }
}