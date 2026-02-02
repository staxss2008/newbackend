package com.dispatch.system.cache;

import com.dispatch.system.entity.FeeStandard;
import com.dispatch.system.mapper.FeeStandardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 费用标准缓存类
 * 用于缓存费用标准配置，减少数据库查询次数
 */
@Component
public class FeeStandardCache {

    private static final Logger log = LoggerFactory.getLogger(FeeStandardCache.class);

    private final Map<String, FeeStandard> cache = new ConcurrentHashMap<>();

    @Autowired
    private FeeStandardMapper feeStandardMapper;

    @PostConstruct
    public void init() {
        refreshCache();
        log.info("费用标准缓存初始化完成，共加载 {} 条配置", cache.size());
    }

    /**
     * 刷新缓存
     */
    public void refreshCache() {
        try {
            List<FeeStandard> all = feeStandardMapper.selectList(null);
            cache.clear();
            all.forEach(s -> cache.put(s.getConfigKey(), s));
            log.info("费用标准缓存刷新完成，共加载 {} 条配置", cache.size());
        } catch (Exception e) {
            log.error("刷新费用标准缓存失败", e);
        }
    }

    /**
     * 获取费用标准
     * @param key 配置键
     * @return 费用标准，如果不存在返回null
     */
    public FeeStandard get(String key) {
        return cache.get(key);
    }

    /**
     * 获取费用标准值
     * @param key 配置键
     * @return 配置值，如果不存在返回null
     */
    public String getValue(String key) {
        FeeStandard standard = cache.get(key);
        return standard != null ? standard.getConfigValue() : null;
    }

    /**
     * 获取费用标准值，如果不存在返回默认值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public String getValue(String key, String defaultValue) {
        String value = getValue(key);
        return value != null ? value : defaultValue;
    }
}
