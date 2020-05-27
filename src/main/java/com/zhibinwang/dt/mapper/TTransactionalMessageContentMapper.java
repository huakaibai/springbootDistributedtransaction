package com.zhibinwang.dt.mapper;

import com.zhibinwang.dt.model.TTransactionalMessageContent;
import com.zhibinwang.dt.model.TTransactionalMessageContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TTransactionalMessageContentMapper {
    int countByExample(TTransactionalMessageContentExample example);

    int deleteByExample(TTransactionalMessageContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TTransactionalMessageContent record);

    int insertSelective(TTransactionalMessageContent record);

    List<TTransactionalMessageContent> selectByExampleWithBLOBs(TTransactionalMessageContentExample example);

    List<TTransactionalMessageContent> selectByExample(TTransactionalMessageContentExample example);

    TTransactionalMessageContent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TTransactionalMessageContent record, @Param("example") TTransactionalMessageContentExample example);

    int updateByExampleWithBLOBs(@Param("record") TTransactionalMessageContent record, @Param("example") TTransactionalMessageContentExample example);

    int updateByExample(@Param("record") TTransactionalMessageContent record, @Param("example") TTransactionalMessageContentExample example);

    int updateByPrimaryKeySelective(TTransactionalMessageContent record);

    int updateByPrimaryKeyWithBLOBs(TTransactionalMessageContent record);

    int updateByPrimaryKey(TTransactionalMessageContent record);
}