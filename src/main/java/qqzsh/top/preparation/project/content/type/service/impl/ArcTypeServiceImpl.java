package qqzsh.top.preparation.project.content.type.service.impl;

import java.util.List;

import qqzsh.top.preparation.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.project.content.type.mapper.ArcTypeMapper;
import qqzsh.top.preparation.project.content.type.domain.ArcType;
import qqzsh.top.preparation.project.content.type.service.IArcTypeService;
import qqzsh.top.preparation.common.utils.text.Convert;

/**
 * 资源类别Service业务层处理
 * 
 * @author zsh
 * @date 2019-12-30
 */
@Service
public class ArcTypeServiceImpl implements IArcTypeService {

    @Autowired
    private ArcTypeMapper arcTypeMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询资源类别
     * 
     * @param srcTypeId 资源类别ID
     * @return 资源类别
     */
    @Override
    public ArcType selectArcTypeById(Long srcTypeId)
    {
        return arcTypeMapper.selectArcTypeById(srcTypeId);
    }

    /**
     * 查询资源类别列表
     * 
     * @param arcType 资源类别
     * @return 资源类别
     */
    @Override
    public List<ArcType> selectArcTypeList(ArcType arcType)
    {
        return arcTypeMapper.selectArcTypeList(arcType);
    }

    /**
     * 新增资源类别
     * 
     * @param arcType 资源类别
     * @return 结果
     */
    @Override
    public int insertArcType(ArcType arcType) {
        int row = arcTypeMapper.insertArcType(arcType);
        updateRedis();
        return row;
    }

    /**
     * 修改资源类别
     * 
     * @param arcType 资源类别
     * @return 结果
     */
    @Override
    public int updateArcType(ArcType arcType) {
        int row = arcTypeMapper.updateArcType(arcType);
        updateRedis();
        return row;
    }

    /**
     * 删除资源类别对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteArcTypeByIds(String ids) {
        int row = arcTypeMapper.deleteArcTypeByIds(Convert.toStrArray(ids));
        updateRedis();
        return row;
    }

    /**
     * 删除资源类别信息
     * 
     * @param srcTypeId 资源类别ID
     * @return 结果
     */
    @Override
    public int deleteArcTypeById(Long srcTypeId) {
        int row = arcTypeMapper.deleteArcTypeById(srcTypeId);
        updateRedis();
        return row;
    }

    /**
     * 更新redis中的资源类别
     */
    public void updateRedis(){
        if (redisUtil.hasKey("arc_type_list")){
            redisUtil.delete("arc_type_list");
        }
        // 从数据库中查询所有资源类别并放入redis
        List<ArcType> arcTypes = selectArcTypeList(new ArcType());
        redisUtil.set("arc_type_list",arcTypes);
    }
}
