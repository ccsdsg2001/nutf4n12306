package com.example.mapper;

import com.example.domain.Trainstation;
import com.example.domain.TrainstationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TrainstationMapper {
    long countByExample(TrainstationExample example);

    int deleteByExample(TrainstationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Trainstation record);

    int insertSelective(Trainstation record);

    List<Trainstation> selectByExample(TrainstationExample example);

    Trainstation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Trainstation record, @Param("example") TrainstationExample example);

    int updateByExample(@Param("record") Trainstation record, @Param("example") TrainstationExample example);

    int updateByPrimaryKeySelective(Trainstation record);

    int updateByPrimaryKey(Trainstation record);
}