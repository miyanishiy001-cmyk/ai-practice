package com.example.ai_game_backend.entity;

public class CurrentCharacter {
    private Long id;
    private Long userId;
    private String stage;
    private Integer generation;
    private Integer actionCount;
    private Integer hp;
    private Integer atk;
    private Integer def;
    private Integer str;
    private Integer intStat;
    private Integer chm;
    private Integer spd;
    private String actionLogs;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public Integer getGeneration() { return generation; }
    public void setGeneration(Integer generation) { this.generation = generation; }

    public Integer getActionCount() { return actionCount; }
    public void setActionCount(Integer actionCount) { this.actionCount = actionCount; }

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

    public String getActionLogs() { return actionLogs; }
    public void setActionLogs(String actionLogs) { this.actionLogs = actionLogs; }
}
