package com.mindskip.xzs.configuration.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


/**
 * @version 3.5.0
 * @description: The type System config.
 * Copyright (C), 2020-2025, 武汉思维跳跃科技有限公司
 * @date 2021/12/25 9:45
 */
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    private PasswordKeyConfig pwdKey;
    private List<String> securityIgnoreUrls;
    private QnConfig qn;

    /**
     * Gets pwd key.
     *
     * @return the pwd key
     */
    public PasswordKeyConfig getPwdKey() {
        return pwdKey;
    }

    /**
     * Sets pwd key.
     *
     * @param pwdKey the pwd key
     */
    public void setPwdKey(PasswordKeyConfig pwdKey) {
        this.pwdKey = pwdKey;
    }

    /**
     * Gets security ignore urls.
     *
     * @return the security ignore urls
     */
    public List<String> getSecurityIgnoreUrls() {
        return securityIgnoreUrls;
    }

    /**
     * Sets security ignore urls.
     *
     * @param securityIgnoreUrls the security ignore urls
     */
    public void setSecurityIgnoreUrls(List<String> securityIgnoreUrls) {
        this.securityIgnoreUrls = securityIgnoreUrls;
    }

    /**
     * Gets qn.
     *
     * @return the qn
     */
    public QnConfig getQn() {
        return qn;
    }

    /**
     * Sets qn.
     *
     * @param qn the qn
     */
    public void setQn(QnConfig qn) {
        this.qn = qn;
    }

}
