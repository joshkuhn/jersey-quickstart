/* Copyright 2019, Joshua M. Kuhn.  All Rights Reserved. */

package com.kuhnjosh.models;

import com.kuhnjosh.exceptions.MalformedPasswdLineException;
import com.kuhnjosh.exceptions.UnknownUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TestPasswdModel {

    private PasswdModel model;
    private UserModel mockUser;

    private static final String MOCK_USER_NAME = "mock_user_name";

    private static final String MOCK_USER_REPR =
            "mock_user_name:x:1001:1002:Mock,1234 9th Ave,800.828.1337:/users/mock_user_name:/bin/bash";

    @BeforeEach
    public void setup() throws MalformedPasswdLineException {
        model = new PasswdModel();
        mockUser = UserModel.fromString(MOCK_USER_REPR);
        model.addUser(mockUser);
    }

    @Test
    public void testGetUsers() {
        Set<String> users = model.getUsers();
        assert(users.contains(MOCK_USER_NAME));
    }

    @Test
    public void testGetUserModel() throws UnknownUserException {
        UserModel fetchedUser = model.getUser(MOCK_USER_NAME);
        assertEquals(fetchedUser.toString(), mockUser.toString());
    }

    @Test
    public void removeUser() throws UnknownUserException {
        model.removeUser(MOCK_USER_NAME);
        assertThrows(UnknownUserException.class, () -> {
            model.getUser(MOCK_USER_NAME);
        });
        assert(model.getUsers().size() == 0);
    }

    // TODO: Refactor FileIO operations so that they can be more easily mocked.
}
