package com.example.myPortfolio.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {

  @Autowired
  private UsersService usersService;

  @PostMapping("/register")
  public ResponseEntity<Users> registerUser(@RequestBody Users users) {
    Users newUsers = usersService.createUser(users);
    return ResponseEntity.ok(newUsers);
  }

  @GetMapping("/{email}")
  public ResponseEntity<Users> getUserByEmail(@PathVariable String email) {
    Optional<Users> users = usersService.findByEmail(email);
    if (users.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404を返す
    }
    return ResponseEntity.ok(users.get()); // 成功した場合のリストを返す
  }
}
