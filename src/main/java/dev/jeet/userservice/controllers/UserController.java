package dev.jeet.userservice.controllers;

import dev.jeet.userservice.dtos.*;
import dev.jeet.userservice.dtos.ResponseStatus;
import dev.jeet.userservice.models.User;
import dev.jeet.userservice.models.Token;
import dev.jeet.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

        private UserService userService;

        public UserController(UserService userService){
            this.userService = userService;
        }

        @PostMapping("/login")
        public LoginResponseDto login(@RequestBody LoginRequestDto requestDto){
            Token token = userService.login(requestDto.getEmail(),requestDto.getPassword());
            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setToken(token);
            return responseDto;
        }

        @PostMapping("/signup")
        public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){
            User user = userService.signUp(signUpRequestDto.getName(),signUpRequestDto.getEmail(),
                    signUpRequestDto.getPassword());
            SignUpResponseDto responseDto = new SignUpResponseDto();
            responseDto.setUser(user);
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
            return responseDto;
        }

        @PostMapping("/validate")
        public UserDto validateToken(@RequestHeader("Authorization") String token){
            User user = userService.validateToken(token);
            return UserDto.fromUser(user);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto){
            userService.logout(logoutRequestDto.getToken());
            return new ResponseEntity<>(HttpStatus.OK);
        }

}
