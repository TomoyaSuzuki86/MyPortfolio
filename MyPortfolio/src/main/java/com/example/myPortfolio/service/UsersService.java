package com.example.myPortfolio.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.exception.DuplicateEmailException;
import com.example.myPortfolio.repository.UsersRepository;

@Service
public class UsersService {

  @Autowired
  private UsersRepository usersRepository;

  /**
   * 新しいユーザーを登録する
   * 
   * @param users 登録するユーザー情報
   * @return 登録されたユーザー
   * @throws DuplicateEmailException メールアドレスが既に存在する場合
   */
  public Users createUsers(Users users) {
    // 既に同じメールアドレスのユーザーが存在しないか確認
    Optional<Users> existingUser = usersRepository.findByEmail(users.getEmail());
    if (existingUser.isPresent()) {
      throw new DuplicateEmailException("メールアドレス " + users.getEmail() + " は既に使用されています。");
    }

    return usersRepository.save(users);

  }

  /**
   * 指定されたメールアドレスとパスワードでユーザー認証を行う
   * 
   * @param email    メールアドレス
   * @param password パスワード
   * @return 認証されたユーザー（存在しない場合は空）
   */
  public Optional<Users> authenticateUser(String email, String password) {
    return usersRepository.findByEmailAndPassword(email, password);
  }

  /**
   * ユーザーIDでユーザーを検索する
   * 
   * @param userId 検索するユーザーのID
   * @return 該当するユーザー（存在しない場合は空）
   */
  public Optional<Users> findById(Long userId) {
    return usersRepository.findById(userId);
  }

  /**
   * メールアドレスでユーザーを検索する
   * 
   * @param email メールアドレス
   * @return 該当するユーザー（存在しない場合は空）
   */
  public Optional<Users> findByEmail(String email) {
    return usersRepository.findByEmail(email);
  }

}
