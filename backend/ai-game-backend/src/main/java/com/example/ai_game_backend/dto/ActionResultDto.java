package com.example.ai_game_backend.dto;

import com.example.ai_game_backend.entity.CurrentCharacter;

public class ActionResultDto {

    private String aiComment;
    private AiStatusResult addedStatus;
    private CurrentCharacter currentCharacter;
    private boolean isEvolved;
    private Integer characterNumber;
    private String characterName;
    private String dexDescription;

    // ゲッター & セッター
    public String getAiComment() { return aiComment; }
    public void setAiComment(String aiComment) { this.aiComment = aiComment; }

    public AiStatusResult getAddedStatus() { return addedStatus; }
    public void setAddedStatus(AiStatusResult addedStatus) { this.addedStatus = addedStatus; }

    public CurrentCharacter getCurrentCharacter() { return currentCharacter; }
    public void setCurrentCharacter(CurrentCharacter currentCharacter) { this.currentCharacter = currentCharacter; }

    public boolean isEvolved() { return isEvolved; }
    public void setEvolved(boolean evolved) { isEvolved = evolved; }

    public Integer getCharacterNumber() { return characterNumber; }
    public void setCharacterNumber(Integer characterNumber) { this.characterNumber = characterNumber; }

    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }

    public String getDexDescription() { return dexDescription; }
    public void setDexDescription(String dexDescription) { this.dexDescription = dexDescription; }
}
