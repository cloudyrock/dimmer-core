package com.github.cloudyrock.dimmer.samples.controller;

import com.github.cloudyrock.dimmer.DimmerFeature;
import com.github.cloudyrock.dimmer.samples.controller.model.UserApiRequest;
import com.github.cloudyrock.dimmer.samples.controller.model.UserApiResponse;
import com.github.cloudyrock.dimmer.samples.repository.User;
import com.github.cloudyrock.dimmer.samples.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.cloudyrock.dimmer.samples.configuration.DimmerConfiguration.*;
import static com.github.cloudyrock.dimmer.samples.controller.UserControllerMapper.*;

@RestController
public class UserController {

    public static final String USERS_PATH = "/users";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @DimmerFeature(value = GET_USERS, op = GET_USERS_CONTROLLER)
    @RequestMapping(path = USERS_PATH, method = RequestMethod.GET)
    public
    @ResponseBody
    List<UserApiResponse> getUsers() {
        LOGGER.info("Called the GET /users endpoint");
        return convertToListUserApiResponse(userService.getListOfUsers());
    }

    @DimmerFeature(value = ADD_USER, op = CREATE_USER_CONTROLLER)
    @RequestMapping(path = USERS_PATH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserApiResponse addUser(@RequestBody UserApiRequest userApiRequest) {
        LOGGER.info("Called the POST /users endpoint");
        final User user = convertUserApiRequestToUser(userApiRequest);
        return convertToUserResponse(userService.createUser(user));
    }
}
