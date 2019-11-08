/* Copyright 2019, Joshua M. Kuhn.  All Rights Reserved. */

package com.kuhnjosh.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.kuhnjosh.exceptions.UnknownUserException;
import com.kuhnjosh.models.PasswdModel;
import com.kuhnjosh.models.UserModel;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Path("users")
public class UsersHandler {

    private static final String PASSWD_FILE_PATH = "./passwd";

    // TODO: Add Spring! This would allow us to keep the passwd model in memory to improve performance.

    /**
     * This method implements the requirement to get a list of users, or show a user's GECOS details.
     * To list user names send a GET request to [http://localhost/users]
     * To list GECOS details send a GET request to [http://localhost/users?username=jsmith]
     * To get multiple users, something like [http://localhost/users?username=jsmith&username=adent] is also valid.
     * NOTE: URL paths and parameters are case sensitive.
     * @param uriInfo
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();

        PasswdModel passwdModel;

        try {
            passwdModel = new PasswdModel(PASSWD_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("Failed to load passwd file.").build();
        }

        if (queryParams.containsKey("username")) {
            List<String> userNames = queryParams.get("username");
            ArrayList<String> gecosListings = new ArrayList<>();

            for (String userName : userNames) {
                try {
                    UserModel userModel = passwdModel.getUser(userName);
                    gecosListings.add(userModel.getGecos());
                } catch (UnknownUserException e) {
                    // Empty since we want to ignore any unknown users provided via the URI params.
                }
            }

            return Response.status(200).entity(gecosListings).build();

        } else {
            Set<String> userList = passwdModel.getUsers();
            return Response.status(200).entity(userList).build();
        }
    }

    /**
     * This method implements the deletion of user from the passwd file.
     * To use it, POST a JSON payload to the delete URI (http://localhost/users/delete).
     * The expected structure of the payload is { "userName": "jsmith" }
     * @param payload
     * @return
     */
    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(JsonNode payload) {
        //
        String userToRemove = new String();
        List<String> userNames = payload.findValuesAsText("userName");
        if (userNames.size() > 0) {
            userToRemove = userNames.get(0);
        }

        PasswdModel passwdModel;

        try {
            passwdModel = new PasswdModel(PASSWD_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("Failed to read password model from file.").build();
        }

        try {
            passwdModel.removeUser(userToRemove);
        } catch (UnknownUserException e) {
           return Response.status(400).entity("UNKNOWN USER " + e.userName).build();
        }

        try {
            passwdModel.writePasswdModelToFile(PASSWD_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("Failed to write updated passwd file.").build();
        }

        return Response.status(200).entity("SUCCESS").build();
    }

    /**
     * This method relies on jax-rs to marshal a POJO from the JSON payload
     * To invoke it, POST a JSON payload to the create URI (http://localhost/users/create).
     * The structure of the payload should be { "userName": "newuser", "password": "pass123", ... , "shell": "/bin/zsh"}
     * If the payload doesn't match the POJO, a 415 response is automatically sent back.
     * @param user
     * @return
     */
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserModel user) {
        PasswdModel passwdModel;

        try {
            passwdModel = new PasswdModel(PASSWD_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("Failed to read password model from file.").build();
        }

        passwdModel.addUser(user);

        try {
            passwdModel.writeNewUserToPasswdFile(user, PASSWD_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).entity("Failed to write new user to passwd file.").build();
        }

        return Response.status(200).entity("SUCCESS").build();
    }
}
