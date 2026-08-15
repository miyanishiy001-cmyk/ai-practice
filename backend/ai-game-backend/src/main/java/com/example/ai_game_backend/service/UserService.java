package com.example.ai_game_backend.service;

import com.example.ai_game_backend.entity.CurrentCharacter;
import com.example.ai_game_backend.entity.User;
import com.example.ai_game_backend.mapper.CurrentCharacterMapper;
import com.example.ai_game_backend.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final CurrentCharacterMapper currentCharacterMapper;

    public UserService(UserMapper userMapper, CurrentCharacterMapper currentCharacterMapper) {
        this.userMapper = userMapper;
        this.currentCharacterMapper = currentCharacterMapper;
    }

    /**
     * ユーザー新規登録処理 (アカウント作成と同時に第1世代タマゴを初期生成)
     */
    @Transactional
    public User register(String username, String password, String nickname) {
        // 重複チェック
        User existing = userMapper.findByUsername(username);
        if (existing != null) {
            throw new IllegalArgumentException("このユーザー名は既に使われています");
        }

        // 1. ユーザーの保存
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        userMapper.insert(user);

        // 2. 初期育成キャラクター（第1世代タマゴ）の作成
        CurrentCharacter initialChar = new CurrentCharacter();
        initialChar.setUserId(user.getId());
        initialChar.setStage("EGG");
        initialChar.setGeneration(1);
        initialChar.setActionCount(0);
        initialChar.setHp(0);
        initialChar.setAtk(0);
        initialChar.setDef(0);
        initialChar.setStr(0);
        initialChar.setIntStat(0);
        initialChar.setChm(0);
        initialChar.setSpd(0);
        initialChar.setActionLogs("");

        currentCharacterMapper.insert(initialChar);

        return user;
    }

    /**
     * ユーザーログイン認証処理
     */
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("ユーザー名またはパスワードが正しくありません");
        }
        return user;
    }
}
