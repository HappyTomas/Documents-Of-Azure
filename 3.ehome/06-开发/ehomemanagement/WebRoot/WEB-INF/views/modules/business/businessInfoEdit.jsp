<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商家信息管理</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" href="${ctxStatic}/common/singlefileUpload.css" type="text/css" />
<script type="text/javascript">
    $(document).ready(function() {
        $("#shangpin").hide();//0如果选择的商家分类所对应的产品模式为“商品配买”，那么关联显示出此块内容
        $("#fuwu").hide();//1如果选择的商家分类所对应的产品模式为“服务预约”，那么关联显示出此块内容。
        fillPro();//加载全部省市区数据；
        getTimeList(30);
        bindServiceType($("#serviceModel").val());
        var balanceModel = $("#balanceModel").val();
        $(":radio[name='balanceModel'][value='" + balanceModel + "']").prop("checked", "checked");
        if (balanceModel == 0) {
            $("#rate").val($("#collectFees").val());
        } else {
            $("#money").val($("#collectFees").val());
        }
        //如果为编辑状态则服务范围不可修改
        if ($('#businessinfoId').val() != null && $('#businessinfoId').val() != "") {
            $("input[name='villageIdList']").each(function() {
                $(this).prop("disabled", true)
            });
            $("#addrDetail").prop("disabled", true);
            $("#longitude").prop("disabled", true);
            $("#latitude").prop("disabled", true);
        }
        jQuery.validator.addMethod("checkSpecialCharacters", function(value, element,params) {
	        var patrn=/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
	        return this.optional(element) || (patrn.test(value));
	    }, "请输入中文，英文或数字");
        jQuery.validator.addMethod("checkImg", function(value, element,params) {
            var imgArea = $("#imgArea").children().find(".up-section").val();
            if(imgArea != ''){
                return false;
            }else{
                return true;
            }
	    }, "请上传商家图片");
        $("#inputForm").validate({
            rules : {
                businessName : {
                    checkSpecialCharacters : true,
                    remote : {
                        type:"POST",
                        url : "${ctx}/business/businessInfo/checkName",
                        data : {
                            name:function(){return $("#businessName").val();},
                            oldName : '${businessInfo.businessName}'
                        }
                    }
                },
                phoneNum : {
                    required : true,
                    minlength : 11,
                    isMobile : true
                },
                categoryIdList : {
                    required : true
                },
                villageIdList : {
                    required : true
                },
                addrCity : {
                    required : true
                },
                addrPro : {
                    required : true
                },
                addrArea : {
                    required : true
                },
                checkImg:{
                    checkImg:"params"
                }
            },
            messages : {
                checkImg:{
                    checkImg:"请上传商家图片"
                },
                businessName : {
                    remote : "商家名称已存在",
                    required : "请输入商家名称",
                },
                phoneNum : {
                    required : "请输入手机号",
                    minlength : "确认手机不能小于11个字符",
                    isMobile : "请正确填写您的手机号码"
                },
                contactPerson : {
                    required : "请输入联系人"
                },
                phoneNum : {
                    required : "请输入联系电话"
                },
                phoneNum : {
                    required : "请输入联系电话"
                },
                categoryIdList : {
                    required : "请选择商家分类"
                },
                villageIdList : {
                    required : "请选择服务范围"
                },
                addrCity : {
                    required : "请选择服务城市"
                },
                addrPro : {
                    required : "请选择服务省份"
                },
                addrArea : {
                    required : "请选择服务区域"
                },
            },
            submitHandler : function(form) {
                var villageIdList = '';
                $("input[name='villageIdList']").each(function() {
                    if ($(this).attr("checked") == 'checked') {
                        villageIdList += $(this).val();
                    }
                });
                
                if (villageIdList == '') {
                    top.$.jBox.tip('请选择服务范围', 'error');
                    return;
                } 
                /*check服务时间段是否符合时间规则  */
                $.ajax({
                    type : 'POST',
                    url : '${ctx}/business/businessInfo/getResult',
                    data : {
                        sHours : getTime("sHours"),
                        eHours : getTime("eHours"),
                        sMinutes : getTime("sMinutes"),
                        eMinutes : getTime("eMinutes")
                    },
                    success : function(data) {
                        if (data == '') {
                            /*check配送时间段是否符合时间规则  */
                            $.ajax({
                                type : 'POST',
                                url : '${ctx}/business/businessInfo/getResult',
                                data : {
                                    sHours : getTime("sHours1"),
                                    eHours : getTime("eHours1"),
                                    sMinutes : getTime("sMinutes1"),
                                    eMinutes : getTime("eMinutes1")
                                },
                                success : function(data) {
                                    if (data == '') {
                                        /*submint之前给结算模式 赋值结算规则  */
                                        if ($("input[name='balanceModel']:checked").val() == 0) {
                                            $("#collectFees").val($("#rate").val());
                                        } else {
                                            $("#collectFees").val($("#money").val());
                                        }
                                        loading('正在提交，请稍等...');
                                        form.submit();
                                    } else {
                                        top.$.jBox.tip('配送' + data, 'error');
                                    }
                                }
                            })
                        } else {
                            top.$.jBox.tip('服务' + data, 'error');
                        }
                    }
                })
            },
            errorContainer : "#messageBox",
            errorPlacement : function(error, element) {
                $("#messageBox").text("输入有误，请先更正。");
                if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                    error.appendTo(element.parent().parent());
                } else {
                    error.insertAfter(element);
                }
            }
        });
        $("input[name='balancemodel']").click(function() {
            if ($(this).val() == 0) {
                $("#lable").html("每笔订单收取订单金额");
                $("#unit").html("%")
            } else {
                $("#lable").html("每笔订单收取");
                $("#unit").html("元")
            }
        });
        /* 根据服务时间间隔  读取时间段下拉数据 */
        $("#serviceTimeInterval").change(function() {
            getTimeList($(this).val());
        })
        /* 根据服务方式判断是否显示服务费用  上门显示*/
        $("input[name='serviceModel']").click(function() {
            bindServiceType($(this).val())
        });

        //通过分类选择 判断是否显示 或者是否可以选择
        var str = '';
        $("input[name='categoryIdList']").click(function() {
            var pattren = $(this).attr("pattren");
            if ($(this).attr('checked') == "checked") {
                if (str.indexOf(pattren) < 0) {
                    str += $(this).attr("pattren") + ','
                } else {
                    top.$.jBox.tip('商家只能匹配同个产品模式的1个商家分类', 'error');
                    $(this).removeAttr('checked');
                }
            } else {
                if (str.indexOf(pattren) < 0) {
                    str += $(this).attr("pattren") + ','
                } else {
                    str = str.replace(pattren + ",", "");
                    $(this).removeAttr('checked');
                }

            }
            if (str.indexOf("0") >= 0) {
                $("#shangpin").show();
            } else {
                $("#shangpin").hide();
            }
            if (str.indexOf("1") >= 0) {
                $("#fuwu").show();
            } else {
                $("#fuwu").hide();
            }
        });
        var arr = $("#categoryIds").val().replace("[", "").replace("]", "").replace(/\s/g, "");//split(",")
        arr = arr.split(",")
        for (var i = 0; i < arr.length; i++) {
            $("input[name='categoryIdList']").each(function() {
                if ($(this).val() == arr[i]) {
                    $(this).attr("checked", "ture");
                    var pattren = $(this).attr("pattren");
                    if (pattren == 0) {
                        $("#shangpin").show();
                    } else if (pattren == 1) {
                        $("#fuwu").show();
                    }
                }
            })
        }
        /* 省市 服务范围 */
        $("#addrpro").change(function() {
            getVillageList1();
            if($(this).val()!=""){
                cleanErroMsg(this,0);
            }
        })
        $("#addrcity").change(function() {
            getVillageList1();
            if($(this).val()!=""){
                cleanErroMsg(this,1);
            }
        })
        $("#addrarea").change(function() {
            if($(this).val()!=""){
                cleanErroMsg(this,2);
            }
        })
        var ids = "${businessInfo.villageIds}".split(",");
    });
    /* 绑定上门、到店应该展示的数据 */
    function bindServiceType(type) {
        if (type == 1) {
            $("#shangmen").hide();
            $(".servicesLable").html("到店")
        } else {
            $("#shangmen").show();
            $(".servicesLable").html("上门")
        }
    }

    function valiTime(sHours, eHours, sMinutes, eMinutes) {
        var message = '';
        $.ajax({
            type : 'POST',
            url : '${ctx}/business/businessInfo/getResult',
            data : {
                sHours : getTime("sHours"),
                eHours : getTime("eHours"),
                sMinutes : getTime("sMinutes"),
                eMinutes : getTime("eMinutes")
            },
            success : function(data) {
                message = data;
                return message;
            }
        });
    }

    function trim(s) {
        return s.replace(/(^\s*)|(\s*$)/g, "");
    }

    /* 传入下拉时间的name属性获取时间值 */
    function getTime(nameVal) {
        var time = "";
        $("select[name=" + nameVal + "]").each(function(i) {
            var val = $(this).val();
            var node = i != 11 ? val + "," : val;
            time += val + ",";
        });
        return time;
    }

    function bindList() {
        $.ajax({
            type : 'POST',
            url : '${ctx}/business/businessInfo/bindList',
            data : {
                timetype : $("#serviceModel").val(),
                businessinfoId : $("#businessinfoId").val()
            },
            dataType : 'json',
            success : function(data) {
                for (var i = 0; i < data.length - 1; i++) {
                    addRow();
                }
                for (var i = 0; i < data.length; i++) {
                    var sHours = data[i].beginHour;
                    var eHours = data[i].endHour;
                    var sMinutes = data[i].beginMinute;
                    var eMinutes = data[i].endMinute;
                    $("select[name=sHours]").eq(i).val(sHours);
                    $("select[name=eHours]").eq(i).val(eHours);
                    $("select[name=sMinutes]").eq(i).val(sMinutes);
                    $("select[name=eMinutes]").eq(i).val(eMinutes);
                }
            }
        })
    }

    function bindList1() {
        $.ajax({
            type : 'POST',
            url : '${ctx}/business/businessInfo/bindList',
            data : {
                timetype : 2,
                businessinfoId : $("#businessinfoId").val()
            },
            dataType : 'json',
            success : function(data) {
                for (var i = 0; i < data.length - 1; i++) {
                    addRow1();
                }
                for (var i = 0; i < data.length; i++) {
                    var sHours = data[i].beginHour;
                    var eHours = data[i].endHour;
                    var sMinutes = data[i].beginMinute;
                    var eMinutes = data[i].endMinute;
                    $("select[name=sHours1]").eq(i).val(sHours);
                    $("select[name=eHours1]").eq(i).val(eHours);
                    $("select[name=sMinutes1]").eq(i).val(sMinutes);
                    $("select[name=eMinutes1]").eq(i).val(eMinutes);
                }
            }
        })
    }

    function getTimeList(interval) {
        $("#timeList").html("");
        $("#timeList1").html("");
        $.ajax({
            type : 'POST',
            url : '${ctx}/business/businessInfo/getTimeList',
            data : {
                "interval" : interval
            },
            dataType : 'json',
            success : function(data) {
                var i = 0;
                var sHousrs = '<select class="timelist" name="sHours">';
                var eHousrs = '<select class="timelist" name="eHours">';
                var sHousrs1 = '<select class="timelist" name="sHours1">';
                var eHousrs1 = '<select class="timelist" name="eHours1">';
                var optionHours1 = '';
                for ( var each in data[0]) {
                    if (data[0][each] == "08") {
                        optionHours1 += '<option selected="ture"  value="'+data[0][each]+'">' + data[0][each] + '</option>';
                    } else {
                        optionHours1 += '<option value="'+data[0][each]+'">' + data[0][each] + '</option>';
                    }
                    i++;
                }
                var optionHours2 = '';
                for ( var each in data[0]) {
                    if (data[0][each] == "10") {
                        optionHours2 += '<option selected="ture"  value="'+data[0][each]+'">' + data[0][each] + '</option>';
                    } else {
                        optionHours2 += '<option value="'+data[0][each]+'">' + data[0][each] + '</option>';
                    }
                    i++;
                }
                var sMinutes = '<select class="timelist" name="sMinutes">';
                var eMinutes = '<select class="timelist" name="eMinutes">';
                var sMinutes1 = '<select class="timelist" name="sMinutes1">';
                var eMinutes1 = '<select class="timelist" name="eMinutes1">';
                var optionMinutes = '';
                for ( var each in data[1]) {
                    optionMinutes += '<option  value="'+data[1][each]+'">' + data[1][each] + '</option>';
                    i++;
                }
                var selectEnd = '</select>';
                var domRow = '<div class="controls" id="item">';
                domRow += sHousrs + optionHours1 + selectEnd + sMinutes + optionMinutes + selectEnd;
                domRow += '至';
                domRow += eHousrs + optionHours2 + selectEnd + eMinutes + optionMinutes + selectEnd;
                domRow += '<a class="btn btn-default" onclick="addRow()"><i class="icon-plus"></i></a>';
                domRow += '</div>';
                $("#timeList").append($(domRow))

                var domRow1 = '<div class="controls" id="item1">';
                domRow1 += sHousrs1 + optionHours1 + selectEnd + sMinutes1 + optionMinutes + selectEnd;
                domRow1 += '至';
                domRow1 += eHousrs1 + optionHours2 + selectEnd + eMinutes1 + optionMinutes + selectEnd;
                domRow1 += '<a class="btn btn-default" onclick="addRow1()"><i class="icon-plus"></i></a>';
                domRow1 += '</div>';
                $("#timeList1").append($(domRow1));
                if ($("#businessinfoId").val() != '') {
                    bindList();
                    bindList1();
                }
                //bindList();
                //bindList1();
            }
        })
    }

    function getVillageList1() {
        $.ajax({
            type : "POST",
            url : ctx + "/village/villageInfo/findList",
            data : {
                provinceId : $("#addrpro").val(),
                cityId : $("#addrcity").val()
            },
            dataType : "JSON",
            success : function(data) {
                $("#villageIdList").html('');
                var domRow = '';
                var arr = $("#villageIds").val();
                $.each(data, function(indx, item) {
                    if (arr.indexOf(item.id) < 0) {
                        domRow += '<input  name="villageIdList"  type="checkbox" value="' + item.id + '" >' + item.villageName + '</input>';
                    } else {
                        domRow += '<input  name="villageIdList" checked="ture" type="checkbox" value="' + item.id + '" >' + item.villageName + '</input>';
                    }
                })
                domRow += '<span class="help-inline"><font color="red">*</font> </span>';
                $("#villageIdList").append($(domRow));
                //如果为修改 则服务范围不能修改
                if ($('#businessinfoId').val() != null && $('#businessinfoId').val() != "") {
                    $("input[name='villageIdList']").each(function() {
                        $(this).prop("disabled", true)
                    });
                }
            }
        })
    }
    function synchroModuleicon(elem) {
        setImagePreview();
        $("#selectedFile").val($(elem).val());
    }
    function cleanErroMsg(obj,index_eq) {
        console.log("手动移除错误提示")
        $(obj).parent().find("label").eq(index_eq).html("").removeClass('error'); ;
    }
    
</script>
<style type="text/css">

</style>
</head>
<body>
    <ul class="nav nav-tabs">
        <li>
            <span>
                <a href="${ctx}/business/businessInfo">商户管理 </a>> <a href="${ctx}/business/businessInfo">${not empty businessInfo.id?'修改':'添加'}商户</a>
            </span>
        </li>
    </ul>
    <br />
    <form:form id="inputForm" modelAttribute="businessInfo" action="${ctx}/business/businessInfo/save" method="post" enctype="multipart/form-data" class="form-horizontal">
        <form:hidden id="businessinfoId" path="id" />
        <input id="serviceModel" type="hidden" value="${businessInfo.serviceModel}" />
        <input id="villageIds" type="hidden" value="${businessInfo.villageIds}" />
        <input id="categoryIds" type="hidden" value="${businessInfo.categoryIdList}" />
        <input id="collectFees" type="hidden" name="collectFees" value="${businessInfo.collectFees}" />
        <input id="balanceModel" type="hidden" value="${businessInfo.balanceModel}" />
        <form:hidden path="useState" />
        <sys:message content="${message}" />
        <div class="control-group">
            <label class="control-label">商户名称：</label>
            <div class="controls">
                <c:choose>
                    <c:when test="${not empty businessInfo.id}">
                        <input id="businessName" type="text" value="${businessInfo.businessName}" readonly="readonly" name="businessName">
                    </c:when>
                    <c:otherwise>
                        <form:input path="businessName" htmlEscape="false" maxlength="64" class="input-xlarge required" />
                    </c:otherwise>
                </c:choose>

                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">商家图片：</label>
            <div id="localImag">
                <div class="controls">
                    <div class="img-box full" id="imgArea">
                        <section class="img-section">
                            <div class="z_photo upimg-div clear">
                                <c:if test="${businessInfo.businessPic != '' && businessInfo.businessPic !=null}">
                                    <section class="up-section fl">
                                        <span class="up-span"></span>
                                        <img src="${ctxStatic}/images/a7.png" class="close-upimg"> <img src="${businessInfo.businessPic}" class="up-img">
                                    </section>
                                </c:if>
                                <section class="z_file fl" <c:if test="${businessInfo.businessPic != '' && businessInfo.businessPic !=null}">style="display: none;"</c:if>>
                                    <img src="${ctxStatic}/images/a10.png" class="add-img">
                                    <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" />
                                </section>
                            </div>
                        </section>
                    </div>
                    <aside class="mask works-mask">
                        <div class="mask-content" id="imgAside">
                            <p class="del-p ">您确定要删除图片吗？</p>
                            <p class="check-p">
                                <span class="del-com wsdel-ok">确定</span>
                                <span class="wsdel-no">取消</span>
                            </p>
                        </div>
                    </aside>
                    <input id="checkImg" name="checkImg" value="" style="width: 0px;height: 0px;border: 0px;"> 
                    <span class="help-inline">
                        <font color="red">*</font>
                    </span>
                </div>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">联系人：</label>
            <div class="controls">
                <form:input path="contactPerson" htmlEscape="false" maxlength="64" class="input-xlarge required" />
                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">联系电话：</label>
            <div class="controls">
                <form:input path="phoneNum" htmlEscape="false" maxlength="64" class="input-xlarge required" />
                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">商家分类:</label>
            <div class="controls">
                <span>
                    <c:forEach items="${allCategory}" var="category" varStatus="status">
                        <input ${not empty businessInfo.id?'disabled="disabled" ':''} name="categoryIdList" type="checkbox" pattren="${category.prodType}" value="${category.id}">${category.categoryName}</input>
                    </c:forEach>
                    <span class="help-inline">
                        <font color="red">*</font>
                    </span>
                </span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">城市：</label>
            <div class="controls">
                <input type="text" class="hide" id="hidProId" value="${businessInfo.addrPro }">
                <input type="text" class="hide" id="hidCityId" value="${businessInfo.addrCity }">
                <input type="text" class="hide" id="hidAreaId" value="${businessInfo.addrArea }">
                <select id="addrpro" ${not empty businessInfo.id?'disabled="disabled" ':''} name="addrPro" style="width: 120px" onchange="changeCity()">
                    <option value="">全部省份</option>
                </select>
                <select id="addrcity" ${not empty businessInfo.id?'disabled="disabled" ':''} name="addrCity" style="width: 120px" onchange="changeArea()">
                    <option value="">全部城市</option>
                </select>
                <select id="addrarea" ${not empty businessInfo.id?'disabled="disabled" ':''} name="addrArea" style="width: 120px">
                    <option value="">全部区域</option>
                </select>
                <form:input path="addrDetail" placeholder="详细地址" htmlEscape="false" maxlength="200" class="input-xlarge required" />
                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">经度：</label>
            <div class="controls">
                <form:input path="longitude" maxlength="16" htmlEscape="false" class="input-xlarge required number" />
                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">纬度：</label>
            <div class="controls">
                <form:input path="latitude" maxlength="16" htmlEscape="false" class="input-xlarge required number" />
                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">商家标签：</label>
            <div class="controls">
                <form:input path="businessLabel" htmlEscape="false" maxlength="128" class="input-xlarge required" />
                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">营业时间：</label>
            <div class="controls">
                <form:input path="businessHours" htmlEscape="false" maxlength="200" class="input-xlarge" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">服务范围：</label>

            <div class="controls" id="villageIdList"></div>
        </div>
        <div class="control-group">
            <label class="control-label">是否推荐：</label>
            <div class="controls">
                <form:radiobuttons path="recomFlag" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required" />
                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">是否在线交易：</label>
            <div class="controls">
                <form:radiobuttons path="onlineFlag" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">是否支持团购：</label>
            <div class="controls">
                <form:radiobuttons path="groupPurchaseFlag" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">结算模式：</label>
            <div class="controls">
                <input type="radio" name="balanceModel" value="0">
                单笔订单比例抽成
            </div>
            <div class="controls" style="margin-top: 5px;">
                <label class="control-label">每笔订单收取订单金额</label>
                <input id="rate" class="input-mini required number" maxlength="10" />
                <label id="unit">%</label>
            </div>
            <div class="controls" style="margin-top: 5px;">
                <input type="radio" name="balanceModel" value="1">
                单笔订单固定金额抽成
            </div>
            <div class="controls" style="margin-top: 5px;">
                <label class="control-label">每笔订单收取</label>
                <input id="money" class="input-mini required number" maxlength="10"/>
                <label id="unit">元</label>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">结算周期：</label>
            <div class="controls">
                <form:select path="balanceCycle" class="input-medium required number">
                    <form:option value="" label="" />
                    <form:options items="${fns:getDictList('balancecycle')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                </form:select>
                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">商家介绍描述：</label>
            <div class="controls">
                <form:textarea path="businessIntroduce" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">营业状态：</label>
            <div class="controls">
                <form:radiobuttons path="businessState" items="${fns:getDictList('businessstate')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">暂停营业起始时间：</label>
            <div class="controls">
                <input name="stopBusinessBeginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate " value="<fmt:formatDate value="${businessInfo.stopBusinessBeginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">暂停营业结束时间：</label>
            <div class="controls">
                <input name="stopBusinessEndTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate " value="<fmt:formatDate value="${businessInfo.stopBusinessEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
            </div>
        </div>
        <div id="fuwu">
            <div class="control-group">
                <label class="control-label">预约服务方式：</label>
                <div class="controls">
                    <form:radiobuttons path="serviceModel" items="${fns:getDictList('servicemodel')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required" />
                    <span class="help-inline">
                        <font color="red">*</font>
                    </span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="servicesLable">上门</span>服务时段：</label>
                <div id="timeList"></div>
            </div>
            <div class="control-group">
                <label class="control-label"><span class="servicesLable">上门</span>服务时间间隔：</label>
                <div class="controls">
                    <form:select path="serviceTimeInterval" class="timelist">
                        <form:options items="${fns:getDictList('servicetimeinterval')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                    </form:select>
                    分钟
                    <span class="help-inline">
                        <font color="red">*</font>
                    </span>
                </div>
            </div>
            <div id="shangmen">
                <div class="control-group">
                    <label class="control-label">最短上门时间：</label>
                    <div class="controls">
                        <form:select path="shortestTime" class="timelist">
                            <form:options items="${fns:getDictList('servicetimeinterval')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                        </form:select>
                        分钟
                        <span class="help-inline">
                            <font color="red">*</font>
                        </span>
                    </div>
                </div>
            </div>
        </div>
        <div id="shangpin">
            <div class="control-group">
                <label class="control-label">商品配送方式：</label>
                <div class="controls">
                    <form:radiobuttons path="distributeModel" items="${fns:getDictList('distributemodel')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required" />
                    <span class="help-inline">
                        <font color="red">*</font>
                    </span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">上门配送时段：</label>
                <div id="timeList1"></div>
            </div>
            <div class="control-group">
                <label class="control-label">时间片显示区间：</label>
                <div class="controls">
                    <form:select path="distributeTimeInterval" class="timelist">
                        <form:options items="${fns:getDictList('servicetimeinterval')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                    </form:select>
                    分钟
                    <span class="help-inline">指：取送时间间隔 </span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">最短送达时间：</label>
                <div class="controls">
                    <form:select path="shortestArriveTime" class="timelist">
                        <form:options items="${fns:getDictList('servicetimeinterval')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                    </form:select>
                    分钟
                    <span class="help-inline">
                        <font color="red">*</font>
                    </span>
                </div>
            </div>
        </div>
        <div class="form-actions">
            <shiro:hasPermission name="business:businessInfo:edit">
                <input id="btnSubmit" class="btn btn-success" type="submit" value="保 存" />&nbsp;
                </shiro:hasPermission>
            <input id="btnCancel" class="btn btn-success" type="button" value="返 回" onclick="history.go(-1)" />
        </div>
    </form:form>
    <script src="${ctxStatic}/common/singlefileUpload.js"></script>
    <script type="text/javascript">
        //服务时间段 添加
        function addRow() {
            var domRow = '<div class="controls">';
            domRow += $('#item').html();
            domRow += '<a class="btn btn-default" onclick="removeRow()"><i class="icon-remove"></i></a>';
            domRow += '</div>';
            $("#timeList").append($(domRow))
        }
        //服务时间段 移除
        function removeRow() {
            var total = $("#timeList").children().size();//
            $("#timeList").children().eq(total - 1).remove();
        }
        //配送时间段 添加
        function addRow1() {
            var domRow = '<div class="controls">';
            domRow += $('#item1').html();
            domRow += '<a class="btn btn-default" onclick="removeRow1()"><i class="icon-remove"></i></a>';
            domRow += '</div>';
            $("#timeList1").append($(domRow))
        }
        //配送时间段 移除
        function removeRow1() {
            var total = $("#timeList1").children().size();//
            $("#timeList1").children().eq(total - 1).remove();
        }
    </script>
</body>
</html>