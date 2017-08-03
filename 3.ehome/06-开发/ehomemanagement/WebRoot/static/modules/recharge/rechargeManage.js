// 以下注释中的“充值金额”与“充值面额”为两个概念：“充 ‘充值金额’ 元 -- 赠送 ‘赠送金额’ 元”，上述的整体为“充值面额”

// 充值面额输入框
var rechargeMoneyInput = "<span id='rechargeElemSpan'><span>充</span>&nbsp;<input id='rechargeMoney' name='tempRechargeMoney' class='input-mini' value='' maxLength='13'/><span>&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;——&nbsp;&nbsp;&nbsp;&nbsp;赠送</span>&nbsp;<input id='giveMoney' name='tempGiveMoney' class='input-mini' maxLength='13'/>&nbsp;<span>元</span></span><br/>";
// 充值面额输入框添加、删除操作按钮
var rechargeMoneyInputLink = "<span><a href='javascript:void(0);' onclick='addRechargeMoneyInput()' style='text-decoration: none;'>&nbsp;&nbsp;&nbsp;&nbsp;<font color='#0000FF'>添加</font></a><a href='javascript:void(0);' onclick='delRechargeMoneyInput()' style='text-decoration: none;'>&nbsp;&nbsp;<font color='#999999'>删除</font></a></span>";
//必填标记
var requiredSign = "<span class='help-inline'><font color='red'>*</font> </span>";
//1到10位正整数 或 1到12位正浮点数（1到2位小数）
var moneyPatrn=/^[1-9]\d{0,9}\.\d{1,2}|0\.\d{1,2}|[1-9]\d{0,9}|[0-9]$/;

//格式化充值面额
function formatRechargeDiv(){
	// 默认3条记录
	for (var i = $("#rechargeMoneyDiv").children("span[id='rechargeElemSpan']").length ; i < 3 ; i++) {
		$("#rechargeMoneyDiv").append(rechargeMoneyInput); 
	}
	
	// 必填标记
	if ($("#rechargeMoneyDiv").children().length != 0) {
		$("#rechargeMoneyDiv").children().eq(0).after(requiredSign);
	}
	
	// 充值面额输入框添加、删除操作按钮
	$("#rechargeMoneyDiv").children(":last-child").before(rechargeMoneyInputLink);
	
	// 充值面额value
	$.each($("input[name='tempRechargeMoney']"),function(index,value,array){
		var num = new Number($(value).val());
		if (num == 0) {
			$(value).val("");
		} else {
			$(value).val(num);
		}
	});
	$.each($("input[name='tempGiveMoney']"),function(index,value,array){
		var num = new Number($(value).val());
		if (num == 0) {
			$(value).val("");
		} else {
			$(value).val(num);
		}
	});
}

// 提交前数据预处理
function dataPretreatment(){
	//定义一个对象数组 存放一个键值对
	var postData = "";
	// value不为空的充值金额输入框的个数
	var effectiveRechargeInputCount = 0;
	$.each($("#rechargeMoneyDiv").find("input[name='tempRechargeMoney']"),function(index,value,array){
		if ($(value).val() != null && $(value).val() != "") {
			effectiveRechargeInputCount++;
		}
	});
	// 当前已经添加进Json的充值金额的个数
	var count = 1;
	// 遍历“充值面额”的输入框，rechargeMoney作为key，giveMoney作为value，存入对象数组
	$.each($("#rechargeMoneyDiv").children("span[id='rechargeElemSpan']"),function(index,value,array){
		var key = $(value).find("#rechargeMoney").val() == "" ? 0 : $(value).find("#rechargeMoney").val();
		var value = $(value).find("#giveMoney").val() == "" ? 0 : $(value).find("#giveMoney").val();
		// 构造Json字符串
		if (key != 0) {
			// 充值金额不属于空时
			postData = postData + "'" + key + "'" + ":" + "'" + value + "'";
			if(effectiveRechargeInputCount > count){
				postData = postData + ",";
				count++;
			}
		}
	});
	postData = "{" + postData + "}";
	// 将对象数组的Json字符串通过hidRechargeMoney传到后台
	$("#hidRechargeMoney").val(postData);
}

//添加充值面额输入框
function addRechargeMoneyInput(){
	// 删除“添加、删除操作按钮”
	$("#rechargeMoneyDiv").children(":last-child").prev().remove();
	
	// 在末尾追加“充值面额输入框”
	$("#rechargeMoneyDiv").append(rechargeMoneyInput);
	
	// 充值面额输入框添加、删除操作按钮
	$("#rechargeMoneyDiv").children(":last-child").before(rechargeMoneyInputLink);
}

// 删除充值面额输入框
function delRechargeMoneyInput(){
	// 仅剩一个输入框时不进行删除
	if ($("#rechargeMoneyDiv").children("span[id='rechargeElemSpan']").length <= 1) {
		alertx("此项为必填项，请勿继续删除");
		return;
	}
	
	// 删除“添加、删除操作按钮”
	$("#rechargeMoneyDiv").children(":last-child").prev().remove();
	
	// 删除最后一个“充值面额输入框”
	$("#rechargeMoneyDiv").children(":last-child").remove();
	$("#rechargeMoneyDiv").children(":last-child").remove();
	
	// 充值面额输入框添加、删除操作按钮
	$("#rechargeMoneyDiv").children(":last-child").before(rechargeMoneyInputLink);
}

//校验单个金额
function checkMoney(elem){
	if ($(elem).val() != moneyPatrn.exec($(elem).val())) {
		alertx("输入的金额格式不正确，请重新输入");
	}
}

// 校验全体金额
function checkMoneys(){
	var flag = true;
	$.each($("input[name='tempRechargeMoney']"),function(index,value,array){
		if ($(value).val() != "" && $(value).val() != moneyPatrn.exec($(value).val())) {
			flag = false;
			alertx("输入的金额格式不正确，请重新输入");
			return flag;
		}
	});
if (flag == false) {
    return flag;
}
	$.each($("input[name='tempGiveMoney']"),function(index,value,array){
		if ($(value).val() != "" && $(value).val() != moneyPatrn.exec($(value).val())) {
			flag = false;
			alertx("输入的金额格式不正确，请重新输入");
			return flag;
		}
	});
	return flag;
}

//校验充值面额是否重复
function checkDuplicateMoneys(){
    var moneyArray = $("input[name='tempRechargeMoney']");
    for(var i = 0; i < moneyArray.size(); i++){
        for (var j = i + 1; j < moneyArray.size(); j++) {
            if ($(moneyArray[i]).val() != null && $(moneyArray[i]).val() != "" 
                && $(moneyArray[j]).val() != null && $(moneyArray[j]).val() != "" 
                && $(moneyArray[i]).val() == $(moneyArray[j]).val()) {
                alertx("您输入了重复的充值面额，请重新输入");
                return false;
            }
        }
    }
    return true;
}