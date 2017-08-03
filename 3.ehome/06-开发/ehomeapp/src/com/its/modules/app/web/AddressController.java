package com.its.modules.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.common.config.Global;
import com.its.common.web.BaseController;
import com.its.modules.app.bean.BuildingInfoBean;
import com.its.modules.app.common.CommonGlobal;
import com.its.modules.app.common.ValidateUtil;
import com.its.modules.app.entity.Address;
import com.its.modules.app.entity.RoomCertify;
import com.its.modules.app.service.AddressService;
import com.its.modules.app.service.BuildingInfoService;

/**
 * 收货地址Controller
 * 
 * @author sushipeng
 * 
 * @version 2017-07-06
 */
@Controller
@RequestMapping(value = "${appPath}/my")
public class AddressController extends BaseController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private BuildingInfoService buildingInfoService;

	/**
	 * 获取地址列表
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getAddressList")
	@ResponseBody
	public Map<String, Object> getAddressList(String userID, String buildingID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID)) {
			return toJson;
		}
		List<Address> addresses = addressService.getAddressListByAccountIdAndVillageInfoId(userID, buildingID);
		if (addresses == null || addresses.size() == 0) {
			toJson.put("code", Global.CODE_SUCCESS);
			toJson.put("message", "暂无数据");
			return toJson;
		}

		/* Data数据开始 */
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (Address address : addresses) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("addressID", address.getId());
			data.put("contactPerson", address.getContact());
			data.put("contactPhone", address.getPhoneNum());
			data.put("address", address.getAddress());
			data.put("isDefault", address.getIsDefault());

			datas.add(data);
		}
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", datas);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 删除地址
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @param addressID
	 *            地址ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "deleteAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteAddress(String userID, String buildingID, String addressID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID, addressID)) {
			return toJson;
		}
		Address address = addressService.judgeAddressEnabled(userID, buildingID, addressID);
		if (address == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "地址不存在");
			return toJson;
		}

		addressService.delete(address);

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "删除地址成功");
		return toJson;
	}

	/**
	 * 新增地址
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @param contactPerson
	 *            联系人（不可空）
	 * @param contactPhone
	 *            联系电话（不可空）
	 * @param addressType
	 *            地址类型（不可空）1->直接输入 2->选择楼栋房间
	 * @param address
	 *            地址信息（可空）地址类型为1时传
	 * @param louDongID
	 *            楼栋ID（可空） 地址类型为2时传
	 * @param roomID
	 *            房间ID（可空） 地址类型为2时传
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "addAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addAddress(String userID, String buildingID, String contactPerson, String contactPhone, int addressType, String address, String louDongID, String roomID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID, contactPerson, contactPhone, String.valueOf(addressType))) {
			return toJson;
		}

		// 查询该用户该楼盘下的地址列表，若列表为空，则添加为默认地址
		List<Address> addresses = addressService.getAddressListByAccountIdAndVillageInfoId(userID, buildingID);
		boolean flag = false;
		if (addresses == null || addresses.size() == 0) {
			flag = true;
		}

		Address addressEntity = new Address();
		addressEntity.setAccountId(userID);
		addressEntity.setVillageInfoId(buildingID);
		addressEntity.setContact(contactPerson);
		addressEntity.setPhoneNum(contactPhone);
		if (addressType == 1) {
			addressEntity.setAddressType(CommonGlobal.ADDRESS_TYPE_INPUT);
			addressEntity.setAddress(address);
		} else if (addressType == 2) {
			addressEntity.setAddressType(CommonGlobal.ADDRESS_TYPE_CHOICE);
			addressEntity.setBuilding(louDongID);
			addressEntity.setHouseNumber(roomID);
		} else {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "地址类型参数有误");
			return toJson;
		}
		if (flag) {
			addressEntity.setIsDefault("1");
		} else {
			addressEntity.setIsDefault("0");
		}
		addressService.save(addressEntity);

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "添加地址成功");
		return toJson;
	}

	/**
	 * 根据ID获取地址信息
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @param addressID
	 *            地址ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getAddressByID")
	@ResponseBody
	public Map<String, Object> getAddressByID(String userID, String buildingID, String addressID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID, addressID)) {
			return toJson;
		}
		Address address = addressService.judgeAddressEnabled(userID, buildingID, addressID);
		if (address == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "地址不存在");
			return toJson;
		}

		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("addressID", address.getId());
		data.put("contactPerson", address.getContact());
		data.put("contactPhone", address.getPhoneNum());
		data.put("addressType", "1".equals(address.getAddressType()) ? 1 : 2);
		data.put("address", address.getAddress());
		data.put("louDong", address.getBuilding());
		data.put("roomID", address.getHouseNumber());
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 获取楼栋和房间信息
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getLouDongAndRoom")
	@ResponseBody
	public Map<String, Object> getLouDongAndRoom(String userID, String buildingID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID)) {
			return toJson;
		}
		List<BuildingInfoBean> buildingInfoBeans = buildingInfoService.getBuildingAndRoomList(buildingID);
		if (buildingInfoBeans == null || buildingInfoBeans.size() == 0) {
			toJson.put("code", Global.CODE_SUCCESS);
			toJson.put("message", "暂无数据");
			return toJson;
		}

		/* Data数据开始 */
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (BuildingInfoBean buildingInfoBean : buildingInfoBeans) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("louDongID", buildingInfoBean.getId());
			data.put("longDongName", buildingInfoBean.getBuildingName());

			List<Map<String, Object>> rooms = new ArrayList<Map<String, Object>>();
			List<RoomCertify> roomCertifies = buildingInfoBean.getRoomCertifies();
			for (RoomCertify roomCertify : roomCertifies) {
				Map<String, Object> room = new HashMap<String, Object>();
				room.put("roomID", roomCertify.getFloorCode());
				room.put("roomName", roomCertify.getRoomName());

				rooms.add(room);
			}
			data.put("rooms", rooms);

			datas.add(data);
		}
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", datas);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 修改地址
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @param addressID
	 *            地址ID（不可空）
	 * @param contactPerson
	 *            联系人（不可空）
	 * @param contactPhone
	 *            联系电话（不可空）
	 * @param addressType
	 *            地址类型（不可空）1->直接输入 2->选择楼栋房间
	 * @param address
	 *            地址信息（可空）地址类型为1时传
	 * @param louDongID
	 *            楼栋ID（可空） 地址类型为2时传
	 * @param roomID
	 *            房间ID（可空） 地址类型为2时传
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "updateAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateAddress(String userID, String buildingID, String addressID, String contactPerson, String contactPhone, int addressType, String address, String louDongID, String roomID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID, contactPerson, contactPhone, String.valueOf(addressType))) {
			return toJson;
		}
		Address addressEntity = addressService.judgeAddressEnabled(userID, buildingID, addressID);
		if (addressEntity == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "地址不存在");
			return toJson;
		}

		addressEntity.setAccountId(userID);
		addressEntity.setVillageInfoId(buildingID);
		addressEntity.setContact(contactPerson);
		addressEntity.setPhoneNum(contactPhone);
		if (addressType == 1) {
			addressEntity.setAddressType(CommonGlobal.ADDRESS_TYPE_INPUT);
			addressEntity.setAddress(address);
		} else if (addressType == 2) {
			addressEntity.setAddressType(CommonGlobal.ADDRESS_TYPE_CHOICE);
			addressEntity.setBuilding(louDongID);
			addressEntity.setHouseNumber(roomID);
		} else {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "地址类型参数有误");
			return toJson;
		}
		addressService.update(addressEntity);

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "更新地址成功");
		return toJson;
	}

	/**
	 * 设置默认地址
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @param addressID
	 *            地址ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "setDefaultAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setDefaultAddress(String userID, String buildingID, String addressID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID, addressID)) {
			return toJson;
		}
		Address address = addressService.judgeAddressEnabled(userID, buildingID, addressID);
		if (address == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "地址不存在");
			return toJson;
		}

		Address defaultAddress = addressService.getDefaultAddress(userID, buildingID);
		if (defaultAddress != null) {
			defaultAddress.setIsDefault("0");
			addressService.update(defaultAddress);
		}
		address.setIsDefault("1");
		addressService.update(address);

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "默认地址已更新");
		return toJson;
	}
}