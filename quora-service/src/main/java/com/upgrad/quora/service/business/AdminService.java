package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.upgrad.quora.service.util.MessageKeys.ADMIN;

@Service
public class AdminService {

  @Autowired private UserDao userDao;

  /**
   * This method validates if role of user is admin
   *
   * @param userAuthToken userAuthTokenObject
   */
  public void validateIfRoleIsAdmin(UserAuthToken userAuthToken)
      throws AuthorizationFailedException {
    if (!userAuthToken.getUser().getRole().equals(ADMIN)) {
      throw new AuthorizationFailedException(
          "ATHR-003", "Unauthorized Access, Entered user is not an admin");
    }
  }

  /**
   * This method remove the user from db and returns its uuid
   *
   * @param user UserObject
   * @return uuid of the user
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public String deleteUser(User user) {
    return userDao.removeUser(user).getUuid();
  }
}
