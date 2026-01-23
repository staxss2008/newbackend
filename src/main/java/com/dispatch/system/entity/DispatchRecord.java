package com.dispatch.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 派车记录实体类
 */
@TableName("dispatch_record")
public class DispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 出车日期
     */
    private LocalDate recordDate;

    /**
     * 驾驶人ID
     */
    private Long driverId;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 用车单位
     */
    private String useUnit;

    /**
     * 乘车人
     */
    private String passengers;

    /**
     * 用车事由
     */
    private String reason;

    /**
     * 出车地点
     */
    private String departurePlace;

    /**
     * 领车人
     */
    private String leader;

    /**
     * 批准人
     */
    private String approver;

    /**
     * 出车时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime departureTime;

    /**
     * 收车时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime returnTime;

    /**
     * 出车公里数
     */
    private BigDecimal startMileage;

    /**
     * 收车公里数
     */
    private BigDecimal endMileage;

    /**
     * 合计公里数
     */
    private BigDecimal totalMileage;

    /**
     * 加油费
     */
    private BigDecimal fuelFee;

    /**
     * 过桥修车费
     */
    private BigDecimal bridgeRepairFee;

    /**
     * 加班小时数
     */
    private BigDecimal overtimeHours;

    /**
     * 加班类型：0-平时加班，1-法定假日，2-公休日
     */
    private Integer overtimeType;

    /**
     * 加班金额
     */
    private BigDecimal overtimeAmount;

    /**
     * 是否值班：1-是，0-否
     */
    private Integer isDuty;

    /**
     * 值班补贴
     */
    private BigDecimal dutySubsidy;

    /**
     * 安全奖
     */
    private BigDecimal safetyBonus;

    /**
     * 公休日工资
     */
    private BigDecimal restDayWage;

    /**
     * 本次合计金额
     */
    private BigDecimal totalAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getUseUnit() {
        return useUnit;
    }

    public void setUseUnit(String useUnit) {
        this.useUnit = useUnit;
    }

    public String getPassengers() {
        return passengers;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalTime returnTime) {
        this.returnTime = returnTime;
    }

    public BigDecimal getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(BigDecimal startMileage) {
        this.startMileage = startMileage;
    }

    public BigDecimal getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(BigDecimal endMileage) {
        this.endMileage = endMileage;
    }

    public BigDecimal getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(BigDecimal totalMileage) {
        this.totalMileage = totalMileage;
    }

    public BigDecimal getFuelFee() {
        return fuelFee;
    }

    public void setFuelFee(BigDecimal fuelFee) {
        this.fuelFee = fuelFee;
    }

    public BigDecimal getBridgeRepairFee() {
        return bridgeRepairFee;
    }

    public void setBridgeRepairFee(BigDecimal bridgeRepairFee) {
        this.bridgeRepairFee = bridgeRepairFee;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public Integer getOvertimeType() {
        return overtimeType;
    }

    public void setOvertimeType(Integer overtimeType) {
        this.overtimeType = overtimeType;
    }

    public BigDecimal getOvertimeAmount() {
        return overtimeAmount;
    }

    public void setOvertimeAmount(BigDecimal overtimeAmount) {
        this.overtimeAmount = overtimeAmount;
    }

    public Integer getIsDuty() {
        return isDuty;
    }

    public void setIsDuty(Integer isDuty) {
        this.isDuty = isDuty;
    }

    public BigDecimal getDutySubsidy() {
        return dutySubsidy;
    }

    public void setDutySubsidy(BigDecimal dutySubsidy) {
        this.dutySubsidy = dutySubsidy;
    }

    public BigDecimal getSafetyBonus() {
        return safetyBonus;
    }

    public void setSafetyBonus(BigDecimal safetyBonus) {
        this.safetyBonus = safetyBonus;
    }

    public BigDecimal getRestDayWage() {
        return restDayWage;
    }

    public void setRestDayWage(BigDecimal restDayWage) {
        this.restDayWage = restDayWage;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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
