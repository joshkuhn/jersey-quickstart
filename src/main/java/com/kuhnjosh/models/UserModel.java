/* Copyright 2019, Joshua M. Kuhn.  All Rights Reserved. */

package com.kuhnjosh.models;

import com.kuhnjosh.exceptions.MalformedPasswdLineException;

public class UserModel {

    private String userName;
    private String password;
    private String userId;
    private String groupId;
    private String gecos;
    private String homeDir;
    private String shell;

    private static String SEPARATOR = ":";

    // Empty constructor is required for jax-rs deserialization.
    public UserModel() {}

    public UserModel(final String userName,
                          final String password,
                          final String userId,
                          final String groupId,
                          final String gecos,
                          final String homeDir,
                          final String shell) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.groupId = groupId;
        this.gecos = gecos;
        this.homeDir = homeDir;
        this.shell = shell;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * The password setter has a special case behavior, where the user's password has is written elsewhere.
     * TODO: Add a feature that hashes the user's password and writes it to /etc/shadow.
     * @param password
     */
    public void setPassword(final String password) {
        this.password = "x";
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    /**
     * GECOS is an old abbreviation that stands for General Electric Comprehensive Operating Supervisor.
     * According to wikipedia, some early Unix systems at Bell Labs used GECOS machines for print spooling
     * and various other services, so this field was added to carry information on a user's GECOS identity.
     * TODO: Per the requirements, switch to using this rather than listing the whole line for a user.
     * @return
     */
    public void setGecos(final String gecos) {
        this.gecos = gecos;
    }

    public String getGecos() {
        return this.gecos;
    }

    public void setHomeDir(final String homeDir) {
        this.homeDir = homeDir;
    }

    public void setShell(final String shell) {
        this.shell = shell;
    }

    public static UserModel fromString(final String userString) throws MalformedPasswdLineException {
        String[] s = userString.split(":");
        if (s.length == 7) {
            return new UserModel(s[0], s[1], s[2], s[3], s[4], s[5], s[6]);
        } else {
            throw new MalformedPasswdLineException();
        }
    }

    @Override
    public String toString() {
        StringBuilder passwd = new StringBuilder();
        passwd.append(userName + SEPARATOR);
        passwd.append(password + SEPARATOR);
        passwd.append(userId + SEPARATOR);
        passwd.append(groupId + SEPARATOR);
        passwd.append(gecos + SEPARATOR);
        passwd.append(homeDir + SEPARATOR);
        passwd.append(shell);
        return passwd.toString();
    }
}
