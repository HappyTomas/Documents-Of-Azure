/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.business.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.its.common.config.Global;
import com.its.common.persistence.Page;
import com.its.common.utils.DateUtils;
import com.its.common.utils.FileUtils;
import com.its.common.utils.MyFDFSClientUtils;
import com.its.common.utils.StringUtils;
import com.its.common.web.BaseController;
import com.its.modules.business.entity.BusinessCategorydict;
import com.its.modules.business.entity.BusinessInfo;
import com.its.modules.business.entity.BusinessServicetime;
import com.its.modules.business.service.BusinessCategorydictService;
import com.its.modules.business.service.BusinessInfoService;
import com.its.modules.business.service.BusinessServicetimeService;
import com.its.modules.goods.entity.GoodsInfo;
import com.its.modules.sys.utils.UserUtils;

/**
 * 商家信息管理Controller
 * 
 * @author zhujiao
 * @version 2017-06-26
 */
@Controller
@RequestMapping(value = "${adminPath}/business/businessInfo")
public class BusinessInfoController extends BaseController {

    @Autowired
    private BusinessInfoService businessInfoService;
    @Autowired
    private BusinessCategorydictService businessCategorydictService;
    @Autowired
    private BusinessServicetimeService businessServicetimeService;

    @ModelAttribute
    public BusinessInfo get(@RequestParam(required = false) String id) {
        BusinessInfo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = businessInfoService.get(id);
        }
        if (entity == null) {
            entity = new BusinessInfo();
        }
        return entity;
    }

    @RequiresPermissions("business:businessInfo:view")
    @RequestMapping(value = { "list", "" })
    public String list(BusinessInfo businessInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BusinessInfo> page = businessInfoService.findPage(new Page<BusinessInfo>(request, response), businessInfo);
        // 图片显示编辑
        for (BusinessInfo imgItem : page.getList()) {
            if (StringUtils.isNotBlank(imgItem.getBusinessPic())) {
                try {
                    imgItem.setBusinessPic(MyFDFSClientUtils.get_fdfs_file_url(request, imgItem.getBusinessPic()));
                } catch (IOException | MyException e) {
                }
            }
        }
        model.addAttribute("page", page);
        model.addAttribute("allCategory", businessCategorydictService.findAllList());
        return "modules/business/businessInfoList";
    }

    @RequiresPermissions("business:businessInfo:add")
    @RequestMapping(value = "form")
    public String form(BusinessInfo businessInfo, Model model, HttpServletRequest request) {
        // 根据图片ID取得图片SRC
        try {
            businessInfo.setBusinessPic(MyFDFSClientUtils.get_fdfs_file_url(request, businessInfo.getBusinessPic()));
        } catch (IOException | MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.addAttribute("businessInfo", businessInfo);
        model.addAttribute("allCategory", businessCategorydictService.findAllList());
        return "modules/business/businessInfoEdit";
    }

    @RequiresPermissions("business:businessInfo:edit")
    @RequestMapping(value = "edit")
    public String edit(BusinessInfo businessInfo, Model model, HttpServletRequest request) {
        // 根据图片ID取得图片SRC
        try {
            businessInfo.setBusinessPic(MyFDFSClientUtils.get_fdfs_file_url(request, businessInfo.getBusinessPic()));
        } catch (IOException | MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.addAttribute("businessInfo", businessInfo);
        model.addAttribute("allCategory", businessCategorydictService.findAllList());
        return "modules/business/businessInfoEdit";
    }

    @RequiresPermissions(value = { "business:businessInfo:add", "business:businessInfo:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(BusinessInfo businessInfo, Model model, @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, businessInfo)) {
            return form(businessInfo, model, request);
        }
        if(file.getSize()>0){
            businessInfo.setBusinessPic(FileUtils.uploadImg(request, file));
        }
        if (businessInfo.getShortestTime() != null && "".equals(businessInfo.getShortestTime())) {
            businessInfo.setShortestTime(null);
        }
        if (businessInfo.getShortestArriveTime() != null && "".equals(businessInfo.getShortestArriveTime())) {
            businessInfo.setShortestArriveTime(null);
        }
        if (businessInfo.getDistributeTimeInterval() != null && "".equals(businessInfo.getDistributeTimeInterval())) {
            businessInfo.setDistributeTimeInterval(null);
        }
        if (businessInfo.getServiceTimeInterval() != null && "".equals(businessInfo.getServiceTimeInterval())) {
            businessInfo.setServiceTimeInterval(null);
        }
        // 分类数据有效性验证，过滤不在商家范围内的分类
        List<BusinessCategorydict> categoryList = Lists.newArrayList();
        List<String> categoryIdList = businessInfo.getCategoryIdList();
        for (BusinessCategorydict r : businessCategorydictService.findAllList()) {
            if (categoryIdList.contains(r.getId())) {
                categoryList.add(r);
            }
        }

        businessInfo.setCategoryList(categoryList);
        businessInfoService.saveInfo(businessInfo);
        addMessage(redirectAttributes, "保存商家信息成功");
        return "redirect:" + Global.getAdminPath() + "/business/businessInfo/?repage";
    }

    @RequiresPermissions("business:businessInfo:delete")
    @RequestMapping(value = "delete")
    public String delete(BusinessInfo businessInfo, RedirectAttributes redirectAttributes) {
        businessInfoService.delete(businessInfo);
        addMessage(redirectAttributes, "删除商家信息成功");
        return "redirect:" + Global.getAdminPath() + "/business/businessInfo/?repage";
    }

    /**
     * 验证商家名称是否有效
     * 
     * @author zhujiao
     * @date 2017年7月6日 下午3:50:16
     * @return String
     */
    @ResponseBody
    @RequestMapping(value = "checkName")
    public String checkName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return "true";
        } else if (name != null && businessInfoService.getModelByName(name) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 冻结商家
     * 
     * @author zhujiao
     * @date 2017年7月6日 下午3:56:58
     * @return String
     */
    @RequiresPermissions(value = { "business:businessInfo:frozen", "business:businessInfo:unfrozen" }, logical = Logical.OR)
    @RequestMapping(value = "updateState")
    public String updateState(BusinessInfo businessInfo, RedirectAttributes redirectAttributes) {
        String flagName = "";
        if (businessInfo.getUseState().equals("0")) {
            flagName = "解冻";
        } else {
            flagName = "冻结";
        }
        if (UserUtils.getUser().getId().equals(businessInfo.getId())) {
            addMessage(redirectAttributes, flagName + "商家信息失败, 不允许" + flagName + "当前商家信息");
        } else {
            businessInfoService.updateState(businessInfo);
            addMessage(redirectAttributes, flagName + "商家信息成功");
        }
        return "redirect:" + Global.getAdminPath() + "/business/businessInfo/?repage";
    }

    /**
     * 商家银行账号管理
     * 
     * @author zhujiao
     * @param businessInfo
     * @param model
     * @return
     */
    @RequiresPermissions("business:businessInfo:editBank")
    @RequestMapping(value = "editBank")
    public String editBank(BusinessInfo businessInfo, Model model) {
        model.addAttribute("businessInfo", businessInfo);
        return "modules/business/businessBankForm";
    }

    @RequiresPermissions("business:businessInfo:editBank")
    @RequestMapping(value = "saveBank")
    public String saveBank(BusinessInfo businessInfo, Model model, RedirectAttributes redirectAttributes) {
        businessInfoService.editBank(businessInfo);
        addMessage(redirectAttributes, "保存商家银行账号信息成功");
        return "redirect:" + Global.getAdminPath() + "/business/businessInfo/?repage";
    }

    /**
     * 根据时间间隔 获取 下拉时间列表 add by zhujiao
     * 
     * @param interval
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "bindList")
    public List<BusinessServicetime> bindList(String timetype, String businessinfoId) {
        List<BusinessServicetime> list = new ArrayList<BusinessServicetime>();
        list = businessServicetimeService.findAllList(timetype, businessinfoId);
        return list;
    }

    /**
     * 根据时间间隔 获取 下拉时间列表 add by zhujiao
     * 
     * @param interval
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "getTimeList")
    public List<Map<String, Object>> getTimeList(Integer interval) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        Integer num = interval / 60;
        if (num == 0) {
            num = 1;
        }
        for (int i = 0; i < 24 / num; i++) {
            map.put(i + "", String.format("%02d", i * num));
        }
        listmap.add(map);
        Integer rot = interval % 60;
        if (rot == 0) {
            map = new HashMap<String, Object>();
            map.put(0 + "", "00");
            listmap.add(map);
        } else {
            map = new HashMap<String, Object>();
            map.put(0 + "", "00");
            map.put(1 + "", "30");
            listmap.add(map);
        }
        return listmap;
    }

    /**
     * 
     * @param sHours
     * @param sMinutes
     * @param eHours
     * @param eMinutes
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getResult")
    private String getResult(String sHours, String sMinutes, String eHours, String eMinutes) {
        String[] arrsHours = sHours.split(",");
        String[] arrsMinutes = sMinutes.split(",");
        String[] arreHours = eHours.split(",");
        String[] arreMinutes = eMinutes.split(",");

        String[] stimes = new String[arrsHours.length];
        String[] etimes = new String[arrsHours.length];
        int count = 0;
        String msg = "";
        for (int i = 0; i < arrsHours.length; i++) {
            stimes[i] = arrsHours[i] + ":" + arrsMinutes[i];
            etimes[i] = arreHours[i] + ":" + arreMinutes[i];
            if (DateUtils.compareTo(stimes[i], etimes[i]) >= 0) {
                count += 1;
                msg = "时间段的开始时间应该小于结束时间";
            }
        }
        for (int i = 0; i < stimes.length - 1; i++) {
            if (DateUtils.compareTo(stimes[i + 1], etimes[i]) < 0) {
                count += 1;
                msg = "时间段的结束时间应该小于或者第二个时间段的开始时间";
            }
        }
        System.out.println("错误" + count + "条，消息提示为：" + msg);
        return msg;
    }

    /**
     * 根据分类获取商家列表
     * 
     * @author zhujiao
     * @date 2017年7月7日 下午6:10:00
     * @return List<BusinessInfo>
     */
    @ResponseBody
    @RequestMapping(value = "bindBusinessList")
    public List<BusinessInfo> bindBusinessList(String categorydict) {
        List<BusinessInfo> list = new ArrayList<BusinessInfo>();
        list = businessInfoService.getBusinessList(categorydict);
        return list;
    }

    /**
     * 根据模块获取商家列表
     * 
     * @author zhujiao
     * @date 2017年7月7日 下午6:10:00
     * @return List<BusinessInfo>
     */
    @ResponseBody
    @RequestMapping(value = "getBusListByModule")
    public List<BusinessInfo> getBusListByModule(String moduleId) {
        List<BusinessInfo> list = new ArrayList<BusinessInfo>();
        list = businessInfoService.getBusListByModule(moduleId);
        return list;
    }
}