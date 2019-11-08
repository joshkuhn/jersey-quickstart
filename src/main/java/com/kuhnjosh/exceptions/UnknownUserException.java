/* Copyright 2019, Joshua M. Kuhn.  All Rights Reserved. */

package com.kuhnjosh.exceptions;

public class UnknownUserException extends Exception {

    public String userName;

    public UnknownUserException(String userName) {
        this.userName = userName;
    }
}
