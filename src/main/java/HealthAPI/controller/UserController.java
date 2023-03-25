package HealthAPI.controller;

import HealthAPI.converter.UserConverter;
import HealthAPI.dto.User.UserCreateDto;
import HealthAPI.dto.User.UserDto;
import HealthAPI.messages.Responses;
import HealthAPI.model.User;
import HealthAPI.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto userDto = userConverter.fromUserToUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/myAccount")
    public ResponseEntity<UserDto> getMyAccount(@NonNull HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        UserDto userDto = userService.getUserByToken(jwt);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserCreateDto> createUser(@Valid @RequestBody UserCreateDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserCreateDto savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateDto userCreateDto) {
        UserDto user = userService.updateUser(id, userCreateDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/myAccount")
    @Secured({"ROLE_ADMIN", "ROLE_HEALTHCAREPROVIDERS"})
    public ResponseEntity<UserDto> updateMyAccount(@NonNull HttpServletRequest request, @Valid @RequestBody UserCreateDto userCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
            }
        }
        String jwt = request.getHeader("Authorization").substring(7);
        UserDto user = userService.getUserByToken(jwt);
        UserDto updatedUser = userService.updateUser(user.getId(), userCreateDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(Responses.DELETED_USER.formatted(id), HttpStatus.OK);
    }

}