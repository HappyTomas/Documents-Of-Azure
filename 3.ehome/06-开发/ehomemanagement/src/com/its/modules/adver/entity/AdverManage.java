/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.adver.entity;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.its.common.persistence.DataEntity;
import com.its.modules.sys.entity.Menu;
import com.its.modules.sys.entity.User;
import com.its.modules.village.entity.VillageInfo;

/**
 * 广告管理-发布管理Entity
 * 
 * @author zhujiao
 * @version 2017-07-04
 */
public class AdverManage extends DataEntity<AdverManage> {

    private static final long serialVersionUID = 1L;
    private String openScreenFlag; // 是否开屏广告
    private String positionId; // 广告位置ID
    private String skipTime; // 开屏广告页面跳过时间
    private String displayType; // 开屏广告显示频次类型：0每天显示1次 1间隔指定小时显示1次
    private String displayTimeInterval; // 开屏广告显示间隔时间：当显示频次类型为间隔*小时显示时才填写
    private String advertTitle; // 广告名称
    private String adverPic; // 广告图片
    private String adverType; // 广告类型：0图文广告 1外链广告 2模块链接 3商家链接 4产品链接
    private String adverContent; // 图文广告内容
    private String linkUrl; // 外链地址
    private String moduleId; // 模块ID
    private String categoryId; // 模块ID  new add
    private String businessinfoId; // 商家ID
    private String goodsId; // 商品ID
    private Date starttime; // 投放开始时间
    private Date endTime; // 投放结束时间
    
    private AdverPosition adverPosition;
    private VillageInfo villageInfo ; // 根据楼盘ID查询投放广告
    private List<VillageInfo> villageList = Lists.newArrayList(); // 投放楼盘列表
    
    private String villageLineId;//根据产品线过滤广告列表时用

    public AdverManage() {
        super();
    }

    public AdverManage(String id) {
        super(id);
    }
    public AdverManage(VillageInfo villageInfo) {
        this();
        this.villageInfo = villageInfo;
    }


    @Length(min = 0, max = 1, message = "是否开屏广告长度必须介于 0 和 1 之间")
    public String getOpenScreenFlag() {
        return openScreenFlag;
    }

    public void setOpenScreenFlag(String openScreenFlag) {
        this.openScreenFlag = openScreenFlag;
    }

    @Length(min = 0, max = 64, message = "广告位置ID长度必须介于 0 和 64 之间")
    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    @Length(min = 0, max = 11, message = "开屏广告页面跳过时间长度必须介于 0 和 11 之间")
    public String getSkipTime() {
        return skipTime;
    }

    public void setSkipTime(String skipTime) {
        this.skipTime = skipTime;
    }

    @Length(min = 0, max = 1, message = "开屏广告显示频次类型：0每天显示1次  1间隔指定小时显示1次长度必须介于 0 和 1 之间")
    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getDisplayTimeInterval() {
        return displayTimeInterval;
    }

    public void setDisplayTimeInterval(String displayTimeInterval) {
        this.displayTimeInterval = displayTimeInterval;
    }

    @Length(min = 0, max = 64, message = "广告名称长度必须介于 0 和 64 之间")
    public String getAdvertTitle() {
        return advertTitle;
    }

    public void setAdvertTitle(String advertTitle) {
        this.advertTitle = advertTitle;
    }

    @Length(min = 0, max = 200, message = "广告图片长度必须介于 0 和 200 之间")
    public String getAdverPic() {
        return adverPic;
    }

    public void setAdverPic(String adverPic) {
        this.adverPic = adverPic;
    }

    @Length(min = 0, max = 1, message = "广告类型：0图文广告  1外链广告  2模块链接  3商家链接  4产品链接长度必须介于 0 和 1 之间")
    public String getAdverType() {
        return adverType;
    }

    public void setAdverType(String adverType) {
        this.adverType = adverType;
    }

    public String getAdverContent() {
        return adverContent;
    }

    public void setAdverContent(String adverContent) {
        this.adverContent = adverContent;
    }

    @Length(min = 0, max = 200, message = "外链地址长度必须介于 0 和 200 之间")
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Length(min = 0, max = 64, message = "模块ID长度必须介于 0 和 64 之间")
    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    @Length(min = 0, max = 64, message = "商家ID长度必须介于 0 和 64 之间")
    public String getBusinessinfoId() {
        return businessinfoId;
    }

    public void setBusinessinfoId(String businessinfoId) {
        this.businessinfoId = businessinfoId;
    }

    @Length(min = 0, max = 64, message = "商品ID长度必须介于 0 和 64 之间")
    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<VillageInfo> getVillageList() {
        return villageList;
    }

    public void setVillageList(List<VillageInfo> villageList) {
        this.villageList = villageList;
    }

    
    public List<String> getVillageIdList() {
        List<String> villageIdList = Lists.newArrayList();
        for (VillageInfo village : villageList) {
            villageIdList.add(village.getId());
        }
        return villageIdList;
    }

    public void setVillageIdList(List<String> villageIdList) {
        villageList = Lists.newArrayList();
        for (String villageId : villageIdList) {
            VillageInfo village = new VillageInfo();
            village.setId(villageId);
            villageList.add(village);
        }
    }

    public String getVillageIds() {
        return StringUtils.join(getVillageIdList(), ",");
    }

    public void setVillageIds(String villageIds) {
        villageList = Lists.newArrayList();
        if (villageIds != null) {
            String[] ids = StringUtils.split(villageIds, ",");
            setVillageIdList(Lists.newArrayList(ids));
        }
    }

    public VillageInfo getVillageInfo() {
        return villageInfo;
    }

    public void setVillageInfo(VillageInfo villageInfo) {
        this.villageInfo = villageInfo;
    }

    public AdverPosition getAdverPosition() {
        return adverPosition;
    }

    public void setAdverPosition(AdverPosition adverPosition) {
        this.adverPosition = adverPosition;
    }

    public String getVillageLineId() {
        return villageLineId;
    }

    public void setVillageLineId(String villageLineId) {
        this.villageLineId = villageLineId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}