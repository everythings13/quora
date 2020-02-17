package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {

  @Autowired private UserDao userDao;

  @Autowired private PasswordCryptographyProvider cryptographyProvider;

  /**
   * This method returns userObject after encrypting its password
   *
   * @param user user
   * @return UserObject
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public User signUp(User user) {
    String[] encryptedText = cryptographyProvider.encrypt(user.getPassword());
    user.setSalt(encryptedText[0]);
    user.setPassword(encryptedText[1]);
    return userDao.createUser(user);
  }

  public void validateUserByUsernameAndEmail(String userName, String email)
      throws SignUpRestrictedException {
    User userByEmail = userDao.getUserByEmail(email);
    validateUser(userName, email, userByEmail);
    User userByUsername = userDao.getUserByUsername(userName);
    validateUser(userName, email, userByUsername);
  }

  private void validateUser(String userName, String email, User user)
      throws SignUpRestrictedException {
    if (user != null) {
      if (userName.equals(user.getUserName())) {
        throw new SignUpRestrictedException(
            "SGR-001", "Try any other Username, this Username has already been taken");
      }
      if (email.equals(user.getEmail())) {
        throw new SignUpRestrictedException(
            "SGR-002", "This user has already been registered, try with any other emailId");
      }
    }
  }
}
