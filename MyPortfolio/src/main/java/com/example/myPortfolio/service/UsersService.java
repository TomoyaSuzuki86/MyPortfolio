package com.example.myPortfolio.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.repository.UsersRepository;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public Users createUsers(Users users) {
        // ユーザー作成ロジック
        return usersRepository.save(users);
    }

    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }
}