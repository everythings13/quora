package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.upgrad.quora.service.util.MessageKeys.*;

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
            SGR_001, TRY_ANY_OTHER_USERNAME);
      }
      if (email.equals(user.getEmail())) {
        throw new SignUpRestrictedException(
            SGR_002, USER_ALREADY_REGISTERED);
      }
    }
  }
}
