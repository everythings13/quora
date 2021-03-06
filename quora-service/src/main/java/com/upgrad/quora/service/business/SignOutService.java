package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthTokenDao;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static com.upgrad.quora.service.util.MessageKeys.SGR_001;
import static com.upgrad.quora.service.util.MessageKeys.USER_NOT_SIGNED_IN;

@Service
public class SignOutService {
  @Autowired private UserAuthTokenDao userAuthTokenDao;

  /**
   * This method gets userUuid if userAuthTokenObject exists for given access token
   *
   * @param accessToken UserObject
   * @return uuid of the user
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public String getUserUuidIfPresentFromUserAuth(String accessToken)
      throws SignOutRestrictedException {
    UserAuthToken userAuthToken = userAuthTokenDao.getUserAuthEntityByAccessToken(accessToken);
    if (userAuthToken == null) {
      throw new SignOutRestrictedException(SGR_001, USER_NOT_SIGNED_IN);
    }
    userAuthToken.setLogoutAt(ZonedDateTime.now());
    userAuthTokenDao.persistUserAuthTokenEntity(userAuthToken);
    return userAuthToken.getUser().getUuid();
  }
}
