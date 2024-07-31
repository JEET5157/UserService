package dev.jeet.userservice.services;

import dev.jeet.userservice.models.Token;
import dev.jeet.userservice.models.User;
import dev.jeet.userservice.repositories.TokenRepository;
import dev.jeet.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Calendar;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Token login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) {
            //throw an exception or redirect user to sign up
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword())){
            // throw an exception
            return null;
        }

        Token token = createToken(user);
        return tokenRepository.save(token);
    }

    @Override
    public User signUp(String name, String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            //throw an exception or redirect user to login
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public User validateToken(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(
                token,false,new Date());

        if(tokenOptional.isEmpty()){
            //throw an exception
            return null;
        }
        return tokenOptional.get().getUser();
    }

    @Override
    public void logout(String tokenValue) {
        Optional<Token> tokenOptional = tokenRepository.findByValueAndDeleted(tokenValue,false);
        if(tokenOptional.isEmpty()){
            //throw some exception
            return;
        }
        Token token = tokenOptional.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    private Token createToken(User user){

        Token token = new Token();
        token.setUser(user);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        Date date30DaysFromToday = calendar.getTime();

        token.setExpiryAt(date30DaysFromToday);
        token.setDeleted(false);

        return token;
    }
}
