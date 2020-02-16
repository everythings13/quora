package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthTokenDao;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class SignOutService {
  @Autowired private UserAuthTokenDao userAuthTokenDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public String  getUserUuidIfPresent(String accessToken) throws SignOutRestrictedException {
        UserAuthToken userAuthToken = userAuthTokenDao.getUserAuthEntityByAccessToken(accessToken);
        if(userAuthToken == null){
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        userAuthToken.setLogoutAt(ZonedDateTime.now());
        return userAuthToken.getUser().getUuid();
    }
}
