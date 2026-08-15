package com.example.ai_game_backend.controller;

import com.example.ai_game_backend.dto.ActionResultDto;
import com.example.ai_game_backend.entity.CurrentCharacter;
import com.example.ai_game_backend.mapper.CurrentCharacterMapper;
import com.example.ai_game_backend.service.NurtureService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/nurture")
public class NurtureController {

    private final NurtureService nurtureService;
    private final CurrentCharacterMapper currentCharacterMapper;

    public NurtureController(NurtureService nurtureService, CurrentCharacterMapper currentCharacterMapper) {
        this.nurtureService = nurtureService;
        this.currentCharacterMapper = currentCharacterMapper;
    }

    /**
     * 行動テキストを入力してキャラクターを育成・進めるエンドポイント
     * URL: POST /api/nurture/action
     * 送信JSON例: { "userId": 1, "actionText": "プログラミングを頑張った" }
     */
    @PostMapping("/action")
    public ActionResultDto executeAction(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.getOrDefault("userId", 1).toString());
        String actionText = (String) request.getOrDefault("actionText", "何もしなかった");

        return nurtureService.processAction(userId, actionText);
    }

    /**
     * 現在のキャラクター情報を取得するエンドポイント
     * URL: GET /api/nurture/current/1
     */
    @GetMapping("/current/{userId}")
    public CurrentCharacter getCurrentCharacter(@PathVariable Long userId) {
        return currentCharacterMapper.findByUserId(userId);
    }
}
