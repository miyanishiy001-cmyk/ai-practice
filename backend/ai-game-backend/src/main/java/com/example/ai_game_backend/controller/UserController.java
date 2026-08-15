package com.example.ai_game_backend.controller;

import com.example.ai_game_backend.entity.User;
import com.example.ai_game_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 新規ユーザー登録 API
     * POST /api/user/register
     * リクエスト例: { "username": "taro", "password": "pass", "nickname": "タロウ" }
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String nickname = request.getOrDefault("nickname", username);

            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "ユーザー名とパスワードを入力してください"));
            }

            User registeredUser = userService.register(username, password, nickname);
            return ResponseEntity.ok(registeredUser);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "登録中にエラーが発生しました"));
        }
    }

    /**
     * ログイン API
     * POST /api/user/login
     * リクエスト例: { "username": "taro", "password": "pass" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            User loggedInUser = userService.login(username, password);
            return ResponseEntity.ok(loggedInUser);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "ログイン処理中にエラーが発生しました"));
        }
    }
}
