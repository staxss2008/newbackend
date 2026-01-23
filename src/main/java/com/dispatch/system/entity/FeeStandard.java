package com.dispatch.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 费用标准实体类
 */
@TableName("fee_standard")
public class FeeStandard implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配置键
     */
    @com.baomidou.mybatisplus.annotation.TableField("`config_key`")
    private String configKey;

    /**
     * 配置值
     */
    @com.baomidou.mybatisplus.annotation.TableField("`config_value`")
    private String configValue;

    /**
     * 配置类型
     */
    @com.baomidou.mybatisplus.annotation.TableField("`config_type`")
    private String configType;

    /**
     * 分类
     */
    @com.baomidou.mybatisplus.annotation.TableField("`category`")
    private String category;

    /**
     * 描述
     */
    @com.baomidou.mybatisplus.annotation.TableField("`description`")
    private String description;

    /**
     * 排序
     */
    @com.baomidou.mybatisplus.annotation.TableField("`sort_order`")
    private Integer sortOrder;

    /**
     * 是否可编辑
     */
    @com.baomidou.mybatisplus.annotation.TableField("`is_editable`")
    private Integer isEditable;

    /**
     * 创建时间
     */
    @com.baomidou.mybatisplus.annotation.TableField("`created_at`")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @com.baomidou.mybatisplus.annotation.TableField("`updated_at`")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Integer isEditable) {
        this.isEditable = isEditable;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
