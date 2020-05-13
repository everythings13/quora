package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthTokenDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class SignInAuthenticationService {

  @Autowired private UserDao userDao;

  @Autowired private UserAuthTokenDao userAuthTokenDao;

  @Autowired private PasswordCryptographyProvider cryptographyProvider;

  /**
   * This method returns userAuthTokenObject if username exists and if password entered in correct
   *
   * @param username username
   * @param password password
   * @return userAuthTokenObject
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public UserAuthToken getUserAuthTokenByAuthenticating(
      final String username, final String password) throws AuthenticationFailedException {
    User user = userDao.getUserByUsername(username);
    if (user == null) {
      throw new AuthenticationFailedException("ATH-001", "This username does not exist");
    }
    final String encryptedPassword = cryptographyProvider.encrypt(password, user.getSalt());
    if (encryptedPassword.equals(user.getPassword())) {
      UserAuthToken userAuthToken = getUserAuthToken(user, encryptedPassword);
      return userAuthTokenDao.persistUserAuthTokenEntity(userAuthToken);
    } else {
      throw new AuthenticationFailedException("ATH-002", "Password Failed");
    }
  }

  private UserAuthToken getUserAuthToken(User user, String encryptedPassword) {
    UserAuthToken userAuthToken = new UserAuthToken();
    userAuthToken.setUser(user);
    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
    final ZonedDateTime now = ZonedDateTime.now();
    final ZonedDateTime expiresAt = now.plusHours(8);
    userAuthToken.setAccessToken(jwtTokenProvider.generateToken(user.getUuid(), now, expiresAt));
    userAuthToken.setUuid(UUID.randomUUID().toString());
    userAuthToken.setLoginAt(now);
    userAuthToken.setExpiresAt(expiresAt);
    userAuthToken.setLogoutAt(null);
    return userAuthToken;
  }
}

