package dev.jeet.userservice.services;

import dev.jeet.userservice.dtos.LoginRequestDto;
import dev.jeet.userservice.models.Token;
import dev.jeet.userservice.models.User;

public interface UserService {
   public Token login(String email, String password);

   public User signUp(String name, String email, String password);

   public User validateToken(String token);

   public void logout(String token);
}
