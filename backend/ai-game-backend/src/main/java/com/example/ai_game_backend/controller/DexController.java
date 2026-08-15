package com.example.ai_game_backend.controller;

import com.example.ai_game_backend.entity.CharacterDex;
import com.example.ai_game_backend.service.DexService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dex")
public class DexController {

    private final DexService dexService;

    public DexController(DexService dexService) {
        this.dexService = dexService;
    }

    /**
     * ユーザーの図鑑コレクション一覧を取得するエンドポイント
     * URL: GET /api/dex/1
     */
    @GetMapping("/{userId}")
    public List<CharacterDex> getDexList(@PathVariable Long userId) {
        return dexService.getDexList(userId);
    }
}
