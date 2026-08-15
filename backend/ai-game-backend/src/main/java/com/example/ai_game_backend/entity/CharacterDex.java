package com.example.ai_game_backend.entity;

import java.time.LocalDateTime;

public class CharacterDex {
    private Long id;
    private Long userId;
    private Integer generation;
    private Integer characterNumber;
    private String characterName;
    private String description;
    private Integer hp;
    private Integer atk;
    private Integer def;
    private Integer str;
    private Integer intStat;
    private Integer chm;
    private Integer spd;
    private String historySummary;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getGeneration() { return generation; }
    public void setGeneration(Integer generation) { this.generation = generation; }

    public Integer getCharacterNumber() { return characterNumber; }
    public void setCharacterNumber(Integer characterNumber) { this.characterNumber = characterNumber; }

    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getHp() { return hp; }
    public void setHp(Integer hp) { this.hp = hp; }

    public Integer getAtk() { return atk; }
    public void setAtk(Integer atk) { this.atk = atk; }

    public Integer getDef() { return def; }
    public void setDef(Integer def) { this.def = def; }

    public Integer getStr() { return str; }
    public void setStr(Integer str) { this.str = str; }

    public Integer getIntStat() { return intStat; }
    public void setIntStat(Integer intStat) { this.intStat = intStat; }

    public Integer getChm() { return chm; }
    public void setChm(Integer chm) { this.chm = chm; }

    public Integer getSpd() { return spd; }
    public void setSpd(Integer spd) { this.spd = spd; }

    public String getHistorySummary() { return historySummary; }
    public void setHistorySummary(String historySummary) { this.historySummary = historySummary; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
