package com.dispatch.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 月度统计汇总实体类
 */
@TableName("monthly_statistics")
public class MonthlyStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 统计年月（YYYY-MM）
     */
    @com.baomidou.mybatisplus.annotation.TableField("`year_month`")
    private String yearMonth;

    /**
     * 驾驶员ID
     */
    @com.baomidou.mybatisplus.annotation.TableField("`driver_id`")
    private Long driverId;

    /**
     * 总公里数
     */
    @com.baomidou.mybatisplus.annotation.TableField("`total_mileage`")
    private BigDecimal totalMileage;

    /**
     * 公里单价
     */
    @com.baomidou.mybatisplus.annotation.TableField("`mileage_unit_price`")
    private BigDecimal mileageUnitPrice;

    /**
     * 公里数补贴
     */
    @com.baomidou.mybatisplus.annotation.TableField("`mileage_subsidy`")
    private BigDecimal mileageSubsidy;

    /**
     * 实际金额（封顶后）
     */
    @com.baomidou.mybatisplus.annotation.TableField("`actual_mileage_amount`")
    private BigDecimal actualMileageAmount;

    /**
     * 小时加班费
     */
    @com.baomidou.mybatisplus.annotation.TableField("`overtime_amount`")
    private BigDecimal overtimeAmount;

    /**
     * 值班补贴
     */
    @com.baomidou.mybatisplus.annotation.TableField("`duty_subsidy`")
    private BigDecimal dutySubsidy;

    /**
     * 法定假日加班费
     */
    @com.baomidou.mybatisplus.annotation.TableField("`legal_holiday_amount`")
    private BigDecimal legalHolidayAmount;

    /**
     * 公休日工资
     */
    @com.baomidou.mybatisplus.annotation.TableField("`rest_day_wage_total`")
    private BigDecimal restDayWageTotal;

    /**
     * 安全奖
     */
    @com.baomidou.mybatisplus.annotation.TableField("`safety_bonus_total`")
    private BigDecimal safetyBonusTotal;

    /**
     * 底薪
     */
    @com.baomidou.mybatisplus.annotation.TableField("`base_salary`")
    private BigDecimal baseSalary;

    /**
     * 合计金额
     */
    @com.baomidou.mybatisplus.annotation.TableField("`total_amount`")
    private BigDecimal totalAmount;

    /**
     * 状态：1-草稿，2-已确认，3-已审核
     */
    @com.baomidou.mybatisplus.annotation.TableField("`status`")
    private Integer status;

    /**
     * 确认人
     */
    @com.baomidou.mybatisplus.annotation.TableField("`confirmed_by`")
    private Long confirmedBy;

    /**
     * 确认时间
     */
    @com.baomidou.mybatisplus.annotation.TableField("`confirmed_at`")
    private LocalDateTime confirmedAt;

    /**
     * 审核人
     */
    @com.baomidou.mybatisplus.annotation.TableField("`audited_by`")
    private Long auditedBy;

    /**
     * 审核时间
     */
    @com.baomidou.mybatisplus.annotation.TableField("`audited_at`")
    private LocalDateTime auditedAt;

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

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public BigDecimal getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(BigDecimal totalMileage) {
        this.totalMileage = totalMileage;
    }

    public BigDecimal getMileageUnitPrice() {
        return mileageUnitPrice;
    }

    public void setMileageUnitPrice(BigDecimal mileageUnitPrice) {
        this.mileageUnitPrice = mileageUnitPrice;
    }

    public BigDecimal getMileageSubsidy() {
        return mileageSubsidy;
    }

    public void setMileageSubsidy(BigDecimal mileageSubsidy) {
        this.mileageSubsidy = mileageSubsidy;
    }

    public BigDecimal getActualMileageAmount() {
        return actualMileageAmount;
    }

    public void setActualMileageAmount(BigDecimal actualMileageAmount) {
        this.actualMileageAmount = actualMileageAmount;
    }

    public BigDecimal getOvertimeAmount() {
        return overtimeAmount;
    }

    public void setOvertimeAmount(BigDecimal overtimeAmount) {
        this.overtimeAmount = overtimeAmount;
    }

    public BigDecimal getDutySubsidy() {
        return dutySubsidy;
    }

    public void setDutySubsidy(BigDecimal dutySubsidy) {
        this.dutySubsidy = dutySubsidy;
    }

    public BigDecimal getLegalHolidayAmount() {
        return legalHolidayAmount;
    }

    public void setLegalHolidayAmount(BigDecimal legalHolidayAmount) {
        this.legalHolidayAmount = legalHolidayAmount;
    }

    public BigDecimal getRestDayWageTotal() {
        return restDayWageTotal;
    }

    public void setRestDayWageTotal(BigDecimal restDayWageTotal) {
        this.restDayWageTotal = restDayWageTotal;
    }

    public BigDecimal getSafetyBonusTotal() {
        return safetyBonusTotal;
    }

    public void setSafetyBonusTotal(BigDecimal safetyBonusTotal) {
        this.safetyBonusTotal = safetyBonusTotal;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(Long confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Long getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(Long auditedBy) {
        this.auditedBy = auditedBy;
    }

    public LocalDateTime getAuditedAt() {
        return auditedAt;
    }

    public void setAuditedAt(LocalDateTime auditedAt) {
        this.auditedAt = auditedAt;
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
