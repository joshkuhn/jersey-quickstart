/* Copyright 2019, Joshua M. Kuhn.  All Rights Reserved. */

package com.kuhnjosh.models;

import com.kuhnjosh.exceptions.MalformedPasswdLineException;
import com.kuhnjosh.exceptions.UnknownUserException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class PasswdModel {

    // Implements FileIO for the passwd file.
    // Provide CRUD operations on the users in that file.
    // TODO: Refactor fileIO into a separate package.

    private HashMap<String, UserModel> users = new HashMap<>();

    // Package-private constructor used for unit tests.
    PasswdModel() {}

    public PasswdModel(final String filePath) throws IOException {
        this.readPasswdFromDisk(filePath);
    }

    public Set<String> getUsers() {
        return users.keySet();
    }

    public UserModel getUser(String userName) throws UnknownUserException {
        if (users.containsKey(userName)) {
            return users.get(userName);
        } else {
            throw new UnknownUserException(userName);
        }
    }

    public void addUser(UserModel user) {
        users.put(user.getUserName(), user);
    }

    public void updateUserHomeDirectory(final String userName, final String homeDirectory) throws UnknownUserException {
        if (users.containsKey(userName)) {
            UserModel user = users.get(userName);
            user.setHomeDir(homeDirectory);
            users.put(user.getUserName(), user);
        } else {
            throw new UnknownUserException(userName);
        }
    }

    public void updateUserShell(final String userName, final String homeDirectory) throws UnknownUserException {
        if (users.containsKey(userName)) {
            UserModel user = users.get(userName);
            user.setShell(homeDirectory);
            users.put(user.getUserName(), user);
        } else {
            throw new UnknownUserException(userName);
        }
    }

    public void removeUser(final String userName) throws UnknownUserException {
        if (!users.containsKey(userName)) {
            throw new UnknownUserException(userName);
        } else {
            users.remove(userName);
        }
    }

    public void readPasswdFromDisk(final String filename) throws IOException {
        File file = new File(filename);
        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            String currentLine = reader.nextLine();
            try {
                UserModel user = UserModel.fromString(currentLine);
                users.put(user.getUserName(), user);
            } catch (MalformedPasswdLineException e) {
                // TODO: Switch to logger and log this as a WARN.
                e.printStackTrace();
            }
        }
        reader.close();
    }

    public void writeNewUserToPasswdFile(final UserModel user, final String filename) throws IOException {
        FileWriter writer = new FileWriter(filename, true);
        writer.write(user + "\n");
        writer.close();
    }

    public void writePasswdModelToFile(final String filename) throws IOException {
        FileWriter writer = new FileWriter(filename, false);
        for (String userName : users.keySet()) {
            writer.write(users.get(userName) + "\n");
        }
        writer.close();
    }
}
