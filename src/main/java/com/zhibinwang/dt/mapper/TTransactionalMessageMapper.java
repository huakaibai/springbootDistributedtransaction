package com.zhibinwang.dt.mapper;

import com.zhibinwang.dt.model.TTransactionalMessage;
import com.zhibinwang.dt.model.TTransactionalMessageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TTransactionalMessageMapper {
    int countByExample(TTransactionalMessageExample example);

    int deleteByExample(TTransactionalMessageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TTransactionalMessage record);

    int insertSelective(TTransactionalMessage record);

    List<TTransactionalMessage> selectByExample(TTransactionalMessageExample example);

    TTransactionalMessage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TTransactionalMessage record, @Param("example") TTransactionalMessageExample example);

    int updateByExample(@Param("record") TTransactionalMessage record, @Param("example") TTransactionalMessageExample example);

    int updateByPrimaryKeySelective(TTransactionalMessage record);

    int updateByPrimaryKey(TTransactionalMessage record);
}