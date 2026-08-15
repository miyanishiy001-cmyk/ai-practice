package com.example.ai_game_backend.dto;

public class AiStatusResult {
    private int hp;
    private int atk;
    private int def;
    private int str;
    private int intStat;
    private int chm;
    private int spd;
    private String comment;

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public int getAtk() { return atk; }
    public void setAtk(int atk) { this.atk = atk; }

    public int getDef() { return def; }
    public void setDef(int def) { this.def = def; }

    public int getStr() { return str; }
    public void setStr(int str) { this.str = str; }

    public int getIntStat() { return intStat; }
    public void setIntStat(int intStat) { this.intStat = intStat; }

    public int getChm() { return chm; }
    public void setChm(int chm) { this.chm = chm; }

    public int getSpd() { return spd; }
    public void setSpd(int spd) { this.spd = spd; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
