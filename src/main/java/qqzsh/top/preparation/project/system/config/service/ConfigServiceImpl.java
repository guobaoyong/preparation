package qqzsh.top.preparation.project.system.config.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqzsh.top.preparation.common.constant.UserConstants;
import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.common.utils.text.Convert;
import qqzsh.top.preparation.project.system.config.domain.Config;
import qqzsh.top.preparation.project.system.config.mapper.ConfigMapper;
import qqzsh.top.preparation.common.constant.UserConstants;
import qqzsh.top.preparation.common.utils.StringUtils;
import qqzsh.top.preparation.common.utils.security.ShiroUtils;
import qqzsh.top.preparation.common.utils.text.Convert;

/**
 * 参数配置 服务层实现
 * 
 * @author zsh
 */
@Service
public class ConfigServiceImpl implements IConfigService
{
    @Autowired
    private ConfigMapper configMapper;

    /**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public Config selectConfigById(Long configId)
    {
        Config config = new Config();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数名称
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey)
    {
        Config config = new Config();
        config.setConfigKey(configKey);
        Config retConfig = configMapper.selectConfig(config);
        return StringUtils.isNotNull(retConfig) ? retConfig.getConfigValue() : "";
    }

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<Config> selectConfigList(Config config)
    {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(Config config)
    {
        config.setCreateBy(ShiroUtils.getLoginName());
        return configMapper.insertConfig(config);
    }

    /**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(Config config)
    {
        config.setUpdateBy(ShiroUtils.getLoginName());
        return configMapper.updateConfig(config);
    }

    /**
     * 批量删除参数配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteConfigByIds(String ids)
    {
        return configMapper.deleteConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(Config config)
    {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        Config info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue())
        {
            return UserConstants.CONFIG_KEY_NOT_UNIQUE;
        }
        return UserConstants.CONFIG_KEY_UNIQUE;
    }
}
