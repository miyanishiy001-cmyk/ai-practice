package com.example.ai_game_backend.service;

import com.example.ai_game_backend.dto.ActionResultDto;
import com.example.ai_game_backend.dto.AiStatusResult;
import com.example.ai_game_backend.entity.CurrentCharacter;
import com.example.ai_game_backend.entity.User;
import com.example.ai_game_backend.mapper.CurrentCharacterMapper;
import com.example.ai_game_backend.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NurtureService {

    private final CurrentCharacterMapper currentCharacterMapper;
    private final UserMapper userMapper;
    private final AiStatusService aiStatusService;
    private final EvolutionService evolutionService;
    private final DexService dexService;

    public NurtureService(CurrentCharacterMapper currentCharacterMapper,
                          UserMapper userMapper,
                          AiStatusService aiStatusService,
                          EvolutionService evolutionService,
                          DexService dexService) {
        this.currentCharacterMapper = currentCharacterMapper;
        this.userMapper = userMapper;
        this.aiStatusService = aiStatusService;
        this.evolutionService = evolutionService;
        this.dexService = dexService;
    }

    /**
     * 行動を入力し、育成データを更新・進化判定まで行うメイン処理
     */
    @Transactional
    public ActionResultDto processAction(Long userId, String actionText) {
        // 1. ユーザーの存在厳格チェック（存在しない場合はエラーを返してフロントで再ログインさせる）
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("ユーザーが存在しません。再ログインしてください。");
        }

        // 2. 現在育成中のキャラクターを取得（なければ初期作成）
        CurrentCharacter character = currentCharacterMapper.findByUserId(userId);
        if (character == null) {
            character = createInitialCharacter(userId);
        }

        // 3. AIによるステータス変換
        AiStatusResult aiResult = aiStatusService.analyzeAction(actionText);

        // 4. ステータスの安全な加算 (Nullセーフ)
        character.setHp(safeGet(character.getHp()) + safeGet(aiResult.getHp()));
        character.setAtk(safeGet(character.getAtk()) + safeGet(aiResult.getAtk()));
        character.setDef(safeGet(character.getDef()) + safeGet(aiResult.getDef()));
        character.setStr(safeGet(character.getStr()) + safeGet(aiResult.getStr()));
        character.setIntStat(safeGet(character.getIntStat()) + safeGet(aiResult.getIntStat()));
        character.setChm(safeGet(character.getChm()) + safeGet(aiResult.getChm()));
        character.setSpd(safeGet(character.getSpd()) + safeGet(aiResult.getSpd()));

        // 5. 行動回数とログの更新
        int nextCount = safeGet(character.getActionCount()) + 1;
        character.setActionCount(nextCount);

        String currentLogs = character.getActionLogs() == null ? "" : character.getActionLogs();
        if (!currentLogs.isEmpty()) {
            currentLogs += "\n";
        }
        character.setActionLogs(currentLogs + actionText);

        // 6. 行動回数に応じた育成段階 (stage) の更新
        if (nextCount >= 10) {
            character.setStage("ADULT");
        } else if (nextCount >= 5) {
            character.setStage("CHILD");
        } else if (nextCount >= 1) {
            character.setStage("BABY");
        }

        // 7. DTOのベース作成
        ActionResultDto resultDto = new ActionResultDto();
        resultDto.setAiComment(aiResult.getComment());
        resultDto.setAddedStatus(aiResult);

        // 8. 10回目に達した場合は進化判定 & AI図鑑生成 & 図鑑保存 & 世代リセット
        if (nextCount >= 10) {
            EvolutionService.EvolutionResult evo = evolutionService.determineEvolution(character);
            resultDto.setEvolved(true);
            resultDto.setCharacterNumber(evo.getCharacterNumber());
            resultDto.setCharacterName(evo.getCharacterName());

            String dexDesc = aiStatusService.generateDexDescription(evo.getCharacterName(), character.getActionLogs());
            resultDto.setDexDescription(dexDesc);

            dexService.saveToDexAndResetCharacter(character, evo.getCharacterNumber(), evo.getCharacterName(), dexDesc);

        } else {
            resultDto.setEvolved(false);
            int updatedRows = currentCharacterMapper.update(character);
            if (updatedRows == 0) {
                currentCharacterMapper.insert(character);
            }
        }

        resultDto.setCurrentCharacter(character);
        return resultDto;
    }

    private int safeGet(Integer val) {
        return val == null ? 0 : val;
    }

    /**
     * 初めてのユーザー用に初期キャラクター（タマゴ）を作成する
     */
    private CurrentCharacter createInitialCharacter(Long userId) {
        CurrentCharacter newChar = new CurrentCharacter();
        newChar.setUserId(userId);
        newChar.setStage("EGG");
        newChar.setGeneration(1);
        newChar.setActionCount(0);
        newChar.setHp(0);
        newChar.setAtk(0);
        newChar.setDef(0);
        newChar.setStr(0);
        newChar.setIntStat(0);
        newChar.setChm(0);
        newChar.setSpd(0);
        newChar.setActionLogs("");

        currentCharacterMapper.insert(newChar);
        return newChar;
    }
}
