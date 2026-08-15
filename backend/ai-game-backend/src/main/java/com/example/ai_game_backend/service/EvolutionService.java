package com.example.ai_game_backend.service;

import com.example.ai_game_backend.entity.CurrentCharacter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EvolutionService {

    /**
     * 進化結果を保持するデータ構造
     */
    public static class EvolutionResult {
        private final int characterNumber;
        private final String characterName;

        public EvolutionResult(int characterNumber, String characterName) {
            this.characterNumber = characterNumber;
            this.characterName = characterName;
        }

        public int getCharacterNumber() { return characterNumber; }
        public String getCharacterName() { return characterName; }
    }

    /**
     * キャラクターの最終ステータスから進化先（全30種）を判定する
     */
    public EvolutionResult determineEvolution(CurrentCharacter character) {
        int hp = character.getHp();
        int atk = character.getAtk();
        int def = character.getDef();
        int str = character.getStr();
        int intStat = character.getIntStat();
        int chm = character.getChm();
        int spd = character.getSpd();

        int totalExp = hp + atk + def + str + intStat + chm + spd;

        // 1. 全ステータスが非常に高い伝説級 (トータルExp 250以上)
        if (totalExp >= 250) {
            return new EvolutionResult(29, "伝説のワンダーナイト");
        }

        // 2. 最も高いステータス（最高値）を調べる
        Map<String, Integer> stats = Map.of(
            "HP", hp, "ATK", atk, "DEF", def,
            "STR", str, "INT", intStat, "CHM", chm, "SPD", spd
        );

        String highestStat = "HP";
        int maxVal = -1;

        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            if (entry.getValue() > maxVal) {
                maxVal = entry.getValue();
                highestStat = entry.getKey();
            }
        }

        // 特化型分岐 (1〜7番)
        switch (highestStat) {
            case "HP":
                return new EvolutionResult(1, "のんびりギガカメ");
            case "ATK":
                return new EvolutionResult(2, "バーニングドラゴン");
            case "DEF":
                return new EvolutionResult(3, "アイアンゴーレム");
            case "STR":
                return new EvolutionResult(4, "ヘビータイガー");
            case "INT":
                return new EvolutionResult(5, "サイバーウィザード");
            case "CHM":
                return new EvolutionResult(6, "プリンセスキャット");
            case "SPD":
                return new EvolutionResult(7, "ライトニングチーター");
        }

        // デフォルト/バランス型
        return new EvolutionResult(30, "まったりスライム");
    }
}
