/* Copyright 2019, Joshua M. Kuhn.  All Rights Reserved. */

package com.kuhnjosh.models;

import com.kuhnjosh.exceptions.MalformedPasswdLineException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUserModel {

    private UserModel testUser;

    private static final String USERNAME = "mock_user_name";
    private static final String PASSWORD = "x";
    private static final String UID = "1001";
    private static final String GID = "1002";
    private static final String GECOS = "Mock,1234 9th Ave,800.828.1337";
    private static final String HOMEDIR = "/users/mock_user_name";
    private static final String SHELL = "/bin/bash";

    private static final String EXPECTED_STRING_REPR =
            "mock_user_name:x:1001:1002:Mock,1234 9th Ave,800.828.1337:/users/mock_user_name:/bin/bash";

    @BeforeEach
    public void setup() {
        testUser = new UserModel(USERNAME, PASSWORD, UID, GID, GECOS, HOMEDIR, SHELL);
    }

    @Test
    public void testSetUserName() {
        final String name = "new_user_name";
        testUser.setUserName(name);
        assertEquals(testUser.getUserName(), name);
    }

    @Test
    public void testSetGecos() {
        final String gecos = "new_gecos";
        testUser.setGecos(gecos);
        assertEquals(testUser.getGecos(), gecos);
    }

    @Test
    public void testToString() {
        assertEquals(testUser.toString(), EXPECTED_STRING_REPR);
    }

    @Test
    public void testFromString() throws MalformedPasswdLineException {
        UserModel builtFromString = UserModel.fromString(EXPECTED_STRING_REPR);
        assertEquals(testUser.toString(), builtFromString.toString());
    }
}
