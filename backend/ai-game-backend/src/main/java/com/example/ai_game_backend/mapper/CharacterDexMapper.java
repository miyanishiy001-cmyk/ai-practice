package com.example.ai_game_backend.mapper;

import com.example.ai_game_backend.entity.CharacterDex;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CharacterDexMapper {

    @Select("SELECT id, user_id AS userId, generation, character_number AS characterNumber, " +
            "character_name AS characterName, description, hp, atk, def, str, int_stat AS intStat, " +
            "chm, spd, history_summary AS historySummary, created_at AS createdAt " +
            "FROM character_dex WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<CharacterDex> findByUserId(Long userId);

    @Select("SELECT id, user_id AS userId, generation, character_number AS characterNumber, " +
            "character_name AS characterName, description, hp, atk, def, str, int_stat AS intStat, " +
            "chm, spd, history_summary AS historySummary, created_at AS createdAt " +
            "FROM character_dex WHERE user_id = #{userId} AND character_number = #{characterNumber} ORDER BY created_at DESC")
    List<CharacterDex> findByUserIdAndCharacterNumber(@Param("userId") Long userId, @Param("characterNumber") Integer characterNumber);

    @Insert("INSERT INTO character_dex(user_id, generation, character_number, character_name, description, hp, atk, def, str, int_stat, chm, spd, history_summary) " +
            "VALUES(#{userId}, #{generation}, #{characterNumber}, #{characterName}, #{description}, #{hp}, #{atk}, #{def}, #{str}, #{intStat}, #{chm}, #{spd}, #{historySummary})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CharacterDex dex);
}
