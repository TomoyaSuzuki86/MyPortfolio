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

import com.example.myPortfolio.entity.User;
import com.example.myPortfolio.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@RequestBody User user) {
    User newUser = userService.createUser(user);
    return ResponseEntity.ok(newUser);
  }

  @GetMapping("/{email}")
  public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
    Optional<User> user = userService.findByEmail(email);
    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404を返す
    }
    return ResponseEntity.ok(user.get()); // 成功した場合のリストを返す
  }
}
