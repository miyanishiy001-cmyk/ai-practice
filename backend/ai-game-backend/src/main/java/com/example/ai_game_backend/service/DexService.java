package com.example.ai_game_backend.service;

import com.example.ai_game_backend.entity.CharacterDex;
import com.example.ai_game_backend.entity.CurrentCharacter;
import com.example.ai_game_backend.mapper.CharacterDexMapper;
import com.example.ai_game_backend.mapper.CurrentCharacterMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DexService {

    private final CharacterDexMapper characterDexMapper;
    private final CurrentCharacterMapper currentCharacterMapper;

    public DexService(CharacterDexMapper characterDexMapper, CurrentCharacterMapper currentCharacterMapper) {
        this.characterDexMapper = characterDexMapper;
        this.currentCharacterMapper = currentCharacterMapper;
    }

    /**
     * 完成したキャラクターを図鑑へ登録し、現在育成中のキャラクターを次世代のタマゴへリセットする
     */
    @Transactional
    public CharacterDex saveToDexAndResetCharacter(CurrentCharacter character, int characterNumber, String characterName, String description) {
        // 1. 図鑑エンティティの作成と保存
        CharacterDex dex = new CharacterDex();
        dex.setUserId(character.getUserId());
        dex.setGeneration(character.getGeneration());
        dex.setCharacterNumber(characterNumber);
        dex.setCharacterName(characterName);
        dex.setDescription(description);
        dex.setHp(character.getHp());
        dex.setAtk(character.getAtk());
        dex.setDef(character.getDef());
        dex.setStr(character.getStr());
        dex.setIntStat(character.getIntStat());
        dex.setChm(character.getChm());
        dex.setSpd(character.getSpd());
        dex.setHistorySummary(character.getActionLogs());

        characterDexMapper.insert(dex);

        // 2. 現在のキャラクターを次世代のタマゴにリセット
        character.setGeneration(character.getGeneration() + 1);
        character.setStage("EGG");
        character.setActionCount(0);
        character.setHp(0);
        character.setAtk(0);
        character.setDef(0);
        character.setStr(0);
        character.setIntStat(0);
        character.setChm(0);
        character.setSpd(0);
        character.setActionLogs("");

        currentCharacterMapper.update(character);

        return dex;
    }

    /**
     * ユーザーの図鑑一覧を取得する
     */
    public List<CharacterDex> getDexList(Long userId) {
        return characterDexMapper.findByUserId(userId);
    }
}
