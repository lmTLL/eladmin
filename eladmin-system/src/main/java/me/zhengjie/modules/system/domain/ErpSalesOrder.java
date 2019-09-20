package me.zhengjie.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class ErpSalesOrder{

	private Integer orderId;
	private String saleOrderNo;
	private String paymentId;
	private String wechatId;
	private String wechatName;
	private String buyerName;
	private Integer projectId;
	private String station;
	private Integer quantity;
	private BigDecimal unitPrice;
	private BigDecimal totalPrice;
	private BigDecimal actualAmount;
	private BigDecimal costUnitPrice;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dealTime;
	private Integer issueOrder;
	private Integer paymentStatus;
	private String remark;
	private Integer dataStatus;
	private Integer auditStatus;
	private String rejectReason;
	private BigDecimal alipaySum;
	private String asinInfo;
	private Integer createUser;
	private Integer updateUser;
	private String shopName;
	private String baseUrl;
	private String buyerQQ;
	private String buyerPhone;
	private String refundType;
	private String refundStatus;
	private String refundAccount;
	private String refundAccountName;
	private BigDecimal refundAmount;
	private String refundImageUrl;
	private String refundNumber;
	private Date refundConfirmTime;
	private String profitExtension;
	private Integer refundId;
	private String refundRemarks;
	private BigDecimal productCost;
	private Integer saleOrderEmergency;
	private BigDecimal allProfit;
	private String payUser;
	private Integer reviewed;

	public Integer getReviewed() {
		return reviewed;
	}

	public void setReviewed(Integer reviewed) {
		this.reviewed = reviewed;
	}

	public String getPayUser() {
		return payUser;
	}

	public void setPayUser(String payUser) {
		this.payUser = payUser;
	}

	public BigDecimal getAllProfit() {
		return allProfit;
	}

	public void setAllProfit(BigDecimal allProfit) {
		this.allProfit = allProfit;
	}
	public Integer getRefundId() {
		return refundId;
	}

	public void setRefundId(Integer refundId) {
		this.refundId = refundId;
	}
	public Integer getSaleOrderEmergency() {
		return saleOrderEmergency;
	}

	public void setSaleOrderEmergency(Integer saleOrderEmergency) {
		this.saleOrderEmergency = saleOrderEmergency;
	}

	public String getProfitExtension() {
		return profitExtension;
	}

	public void setProfitExtension(String profitExtension) {
		this.profitExtension = profitExtension;
	}

	public Date getRefundConfirmTime() {
		return refundConfirmTime;
	}

	public void setRefundConfirmTime(Date refundConfirmTime) {
		this.refundConfirmTime = refundConfirmTime;
	}

	public String getRefundImageUrl() {
		return refundImageUrl;
	}

	public void setRefundImageUrl(String refundImageUrl) {
		this.refundImageUrl = refundImageUrl;
	}

	public String getRefundNumber() {
		return refundNumber;
	}

	public void setRefundNumber(String refundNumber) {
		this.refundNumber = refundNumber;
	}

	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

	public String getRefundAccountName() {
		return refundAccountName;
	}

	public void setRefundAccountName(String refundAccountName) {
		this.refundAccountName = refundAccountName;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getBuyerQQ() {
		return buyerQQ;
	}

	public void setBuyerQQ(String buyerQQ) {
		this.buyerQQ = buyerQQ;
	}

	public String getBuyerPhone() {
		return buyerPhone;
	}

	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}
	public BigDecimal getProductCost() {
		return productCost;
	}

	public void setProductCost(BigDecimal productCost) {
		this.productCost = productCost;
	}
	/**
	 * 服务项目名称
	 */
	private String projectName;
	/**
	 * 销售姓名
	 */
	private String createUserName;
	
	/**
	 * 关联采购单号
	 */
	private String purchaseOrderNo;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getSaleOrderNo() {
		return saleOrderNo;
	}
	public void setSaleOrderNo(String saleOrderNo) {
		this.saleOrderNo = saleOrderNo;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getWechatId() {
		return wechatId;
	}
	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}
	public String getWechatName() {
		return wechatName;
	}
	public void setWechatName(String wechatName) {
		this.wechatName = wechatName;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public BigDecimal getCostUnitPrice() {
		return costUnitPrice;
	}
	public void setCostUnitPrice(BigDecimal costUnitPrice) {
		this.costUnitPrice = costUnitPrice;
	}
	public Date getDealTime() {
		return dealTime;
	}
	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
	public Integer getIssueOrder() {
		return issueOrder;
	}
	public void setIssueOrder(Integer issueOrder) {
		this.issueOrder = issueOrder;
	}
	public Integer getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public BigDecimal getAlipaySum() {
		return alipaySum;
	}
	public void setAlipaySum(BigDecimal alipaySum) {
		this.alipaySum = alipaySum;
	}
	public Integer getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
	public Integer getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Integer updateUser) {
		this.updateUser = updateUser;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public BigDecimal getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}
	public String getAsinInfo() {
		return asinInfo;
	}
	public void setAsinInfo(String asinInfo) {
		this.asinInfo = asinInfo;
	}
	public String getPurchaseOrderNo() {
		return purchaseOrderNo;
	}
	public void setPurchaseOrderNo(String purchaseOrderNo) {
		this.purchaseOrderNo = purchaseOrderNo;
	}
	public String getRefundRemarks() {
		return refundRemarks;
	}
	public void setRefundRemarks(String refundRemarks) {
		this.refundRemarks = refundRemarks;
	}

	@Override
	public String toString() {
		return "SalesOrder{" +
				"orderId=" + orderId +
				", saleOrderNo='" + saleOrderNo + '\'' +
				", paymentId='" + paymentId + '\'' +
				", wechatId='" + wechatId + '\'' +
				", wechatName='" + wechatName + '\'' +
				", buyerName='" + buyerName + '\'' +
				", projectId=" + projectId +
				", station='" + station + '\'' +
				", quantity=" + quantity +
				", unitPrice=" + unitPrice +
				", totalPrice=" + totalPrice +
				", actualAmount=" + actualAmount +
				", costUnitPrice=" + costUnitPrice +
				", dealTime=" + dealTime +
				", issueOrder=" + issueOrder +
				", paymentStatus=" + paymentStatus +
				", remark='" + remark + '\'' +
				", dataStatus=" + dataStatus +
				", auditStatus=" + auditStatus +
				", rejectReason='" + rejectReason + '\'' +
				", alipaySum=" + alipaySum +
				", asinInfo='" + asinInfo + '\'' +
				", createUser=" + createUser +
				", updateUser=" + updateUser +
				", shopName='" + shopName + '\'' +
				", baseUrl='" + baseUrl + '\'' +
				", buyerQQ='" + buyerQQ + '\'' +
				", buyerPhone='" + buyerPhone + '\'' +
				", refundType='" + refundType + '\'' +
				", refundStatus='" + refundStatus + '\'' +
				", refundAccount='" + refundAccount + '\'' +
				", refundAccountName='" + refundAccountName + '\'' +
				", refundAmount=" + refundAmount +
				", refundImageUrl='" + refundImageUrl + '\'' +
				", refundNumber='" + refundNumber + '\'' +
				", refundConfirmTime=" + refundConfirmTime +
				", profitExtension='" + profitExtension + '\'' +
				", refundId=" + refundId +
				", refundRemarks='" + refundRemarks + '\'' +
				", productCost=" + productCost +
				", saleOrderEmergency=" + saleOrderEmergency +
				", allProfit=" + allProfit +
				", payUser='" + payUser + '\'' +
				", reviewed=" + reviewed +
				", projectName='" + projectName + '\'' +
				", createUserName='" + createUserName + '\'' +
				", purchaseOrderNo='" + purchaseOrderNo + '\'' +
				'}';
	}
}
