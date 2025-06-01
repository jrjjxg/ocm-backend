package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.UserToken;

public interface UserTokenService extends BaseService<UserToken> {

    /**
     * 根据token获取UserToken，带缓存的
     *
     * @param token token
     * @return UserToken
     */
    UserToken getToken(String token);

    /**
     * 插入用户Token
     *
     * @param user user
     * @return UserToken
     */
    UserToken insertUserToken(User user);
}
