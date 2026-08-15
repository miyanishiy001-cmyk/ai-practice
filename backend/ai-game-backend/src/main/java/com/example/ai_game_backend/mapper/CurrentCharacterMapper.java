package com.example.ai_game_backend.mapper;

import com.example.ai_game_backend.entity.CurrentCharacter;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CurrentCharacterMapper {

    @Select("SELECT id, user_id AS userId, stage, generation, action_count AS actionCount, " +
            "hp, atk, def, str, int_stat AS intStat, chm, spd, action_logs AS actionLogs " +
            "FROM current_character WHERE user_id = #{userId}")
    CurrentCharacter findByUserId(Long userId);

    @Insert("INSERT INTO current_character(user_id, stage, generation, action_count, hp, atk, def, str, int_stat, chm, spd, action_logs) " +
            "VALUES(#{userId}, #{stage}, #{generation}, #{actionCount}, #{hp}, #{atk}, #{def}, #{str}, #{intStat}, #{chm}, #{spd}, #{actionLogs})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CurrentCharacter character);

    @Update("UPDATE current_character SET stage=#{stage}, generation=#{generation}, action_count=#{actionCount}, " +
            "hp=#{hp}, atk=#{atk}, def=#{def}, str=#{str}, int_stat=#{intStat}, chm=#{chm}, spd=#{spd}, action_logs=#{actionLogs} " +
            "WHERE user_id=#{userId}")
    int update(CurrentCharacter character);

    @Delete("DELETE FROM current_character WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
}
