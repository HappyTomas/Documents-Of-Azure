/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.village.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.company.entity.CompanyInfo;
import com.its.modules.property.entity.PropertyCompany;
import com.its.modules.property.service.PropertyCompanyService;
import com.its.modules.sys.utils.UserUtils;
import com.its.modules.village.dao.VillageInfoDao;
import com.its.modules.village.entity.VillageInfo;
import com.its.modules.village.entity.VillageLine;

/**
 * 楼盘信息Service
 * 
 * @author zhujiao
 * @version 2017-07-03
 */
@Service
@Transactional(readOnly = true)
public class VillageInfoService extends CrudService<VillageInfoDao, VillageInfo> {

	@Autowired
	private VillageInfoDao villageInfoDao;
	@Autowired
	private VillageLineService villageLineService;
	@Autowired
	private PropertyCompanyService propertyCompanyService;

	public VillageInfo get(String id) {
		return super.get(id);
	}

	public List<VillageInfo> findList(VillageInfo villageInfo) {
		return super.findList(villageInfo);
	}
		
	/**
	 * 查询楼盘（查询楼栋信息接口定时任务开始用）
	 * @param villageInfo
	 * @return
	 */
	public List<VillageInfo> getVillageList(VillageInfo villageInfo) {
		return villageInfoDao.getVillageList(villageInfo);
	}
	
	public Page<VillageInfo> findPage(Page<VillageInfo> page, VillageInfo villageInfo) {
		Page<VillageInfo> villageInfoPage = super.findPage(page, villageInfo);

		// 获取关联的产品线
		VillageLine villageLine = null;
		for (VillageInfo tempVillageInfo : villageInfoPage.getList()) {
			villageLine = new VillageLine();
			villageLine.setVillageInfoId(tempVillageInfo.getId());
			tempVillageInfo.setVillageLine(villageLineService.findList(villageLine));
		}
		return villageInfoPage;
	}

	@Transactional(readOnly = false)
	public void save(VillageInfo villageInfo) {
		PropertyCompany propertyCompany = propertyCompanyService.get(villageInfo.getPropertyCompanyId());
		if (propertyCompany != null) {
			villageInfo.setPropertyCompanyName(propertyCompany.getCompanyName());
		}
		super.save(villageInfo);
	}

	@Transactional(readOnly = false)
	public void delete(VillageInfo villageInfo) {
		super.delete(villageInfo);
	}
	/**
	 * 获取楼盘树数据   楼盘线Id  
	 * @param villageInfo
	 * @return
	 * @return List<VillageInfo>
	 * @author zhujiao   
	 * @date 2017年7月12日 下午7:39:47
	 */
	@Transactional(readOnly = false)
	public List<VillageInfo> getVillageTree(VillageInfo villageInfo) {
		List<VillageInfo> list = new ArrayList<>();
		list = villageInfoDao.findPro(villageInfo);
		String villageInfoIdsStr = UserUtils.getUser().getVillageInfoIds();
		for (int i = 0; i < list.size(); i++) {
			List<VillageInfo> list1 = new ArrayList<>();
			list1 = villageInfoDao.findCity(list.get(i));
			list.get(i).setChildren(list1);
			for (int j = 0; j < list1.size(); j++) {
				List<VillageInfo> list2 = new ArrayList<>();
				VillageInfo newModel = new VillageInfo();
				newModel = list1.get(j);
				newModel.setVillageLineId(villageInfo.getVillageLineId());
				newModel.setVillageIds(villageInfoIdsStr);
				list2 = villageInfoDao.findVillage(newModel);
				list1.get(j).setChildren(list2);
			}
		}
		return list;

	}
	/**
	 * 获取楼盘树数据   楼盘Id  
	 * @param villageInfo
	 * @return
	 * @return List<VillageInfo>
	 * @author zhujiao   
	 * @date 2017年7月12日 下午7:34:09
	 */
	@Transactional(readOnly = false)
	public List<VillageInfo> getUserVillageTree(VillageInfo villageInfo) {
		List<VillageInfo> list = new ArrayList<>();
		list = villageInfoDao.findPro(villageInfo);
		for (int i = 0; i < list.size(); i++) {
			List<VillageInfo> list1 = new ArrayList<>();
			list1 = villageInfoDao.findCity(list.get(i));
			list.get(i).setChildren(list1);
			for (int j = 0; j < list1.size(); j++) {
				List<VillageInfo> list2 = new ArrayList<>();
				list2 = villageInfoDao.findUserVillage(list1.get(j));
				list1.get(j).setChildren(list2);
			}
		}
		return list;

	}

	/**
	 * 更新楼盘状态
	 * 
	 * @param villageInfo
	 */
	@Transactional(readOnly = false)
	public void updateState(VillageInfo villageInfo) {
		villageInfoDao.updateState(villageInfo);
	}

	/**
	 * 保存关联的产品线
	 * 
	 * @param villageInfo
	 *            包含楼盘ID、产品线值字符串
	 * @return 保存是否成功
	 */
	@Transactional(readOnly = false)
	public boolean saveProductLine(VillageInfo villageInfo) {
		// 一个产品线对应一条数据
		VillageLine villageLine = null;
		if (villageInfo == null || villageInfo.getVillageLineId() == null || villageInfo.getVillageLineId().isEmpty()) {
			return false;
		}

		// 该楼盘数据库中已存在的所有产品线
		List<VillageLine> villageLineList = villageLineService.findListByVillageId(villageInfo.getId());
		// 该楼盘已选择的所有产品线
		String[] villageProductLines = villageInfo.getVillageLineId().split(",");

		int index = 0;
		if (villageProductLines.length >= villageLineList.size()) {
			// 进行更新操作
			for (VillageLine tempVillageLine : villageLineList) {
				tempVillageLine.setProductLine(villageProductLines[index]);
				// 更新删除状态为正常
				tempVillageLine.setDelFlag(VillageLine.DEL_FLAG_NORMAL);
				index++;
				villageLineService.save(tempVillageLine);
			}

			// 进行新增操作
			for (int i = index; i < villageProductLines.length; i++) {
				villageLine = new VillageLine();
				villageLine.setVillageInfoId(villageInfo.getId());
				villageLine.setProductLine(villageProductLines[i]);
				villageLineService.save(villageLine);
			}
		} else if (villageProductLines.length < villageLineList.size()) {
			// 进行更新操作
			for (String villageProductLine : villageProductLines) {
				villageLine = villageLineList.get(index);
				villageLine.setProductLine(villageProductLine);
				villageLine.setDelFlag(VillageLine.DEL_FLAG_NORMAL);
				villageLineService.save(villageLine);
				index++;
			}

			// 进行删除操作 删除未被更新的数据库中数据
			for (int i = index; i < villageLineList.size(); i++) {
				villageLineService.delete(villageLineList.get(i));
			}
		}
		return true;
	}
	
	/**
	 * 获取当前登录用户的权限下的楼盘信息
	 * @param villageInfo 可带有其他参数的楼盘检索条件
	 * @return
	 */
	public List<VillageInfo> findListByLoginUser(VillageInfo villageInfo) {
		// 获取当前用户权限下的楼盘ID
		String villageInfoIdsStr = UserUtils.getUser().getVillageInfoIds();
		List<String> villageInfoIdList = new ArrayList<String>();
		if (villageInfoIdsStr != null && !villageInfoIdsStr.isEmpty()) {
			String[] villageInfoIds = villageInfoIdsStr.split(",");
			for (String villageInfoId : villageInfoIds) {
				if (villageInfoId != null && !villageInfoId.isEmpty()) {
					villageInfoIdList.add(villageInfoId);
				}
			}
		}
		
		// 根据省、市、用户权限下的楼盘ID、所有状态进行查询
		villageInfo.setUserVillageIds(villageInfoIdList);
		return super.findList(villageInfo);
	}
	

	 /**
    * 查询全部数据
    * 
    * @author zhujiao
    * @date 2017年7月4日 下午7:39:39
    * @return List<AdverPosition>
    */
   public List<VillageInfo> findAllList() {
       return villageInfoDao.findAllList(new VillageInfo());
   }
}