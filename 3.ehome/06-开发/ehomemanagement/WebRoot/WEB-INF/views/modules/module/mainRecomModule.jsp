<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>

<head>
<title>楼盘信息产品线管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<script type="text/javascript">
    $(document).ready(function() {
        //添加主导航验证
        $.validator.addMethod("checkMaxSize", function(value, element, params) {
            var num = $("input[name='maintRecomModuleIds']:checked").size();
            console.log(num);
            if (num > 5) {
                return false;
            } else {
                return true;
            }
        }, "最多只能勾选5个模块");
        $.validator.addMethod("checkMinSize", function(value, element, params) {
            var num = $("input[name='maintRecomModuleIds']:checked").size();
            console.log(num);
            if (num < 5) {
                return false;
            } else {
                return true;
            }
        }, "首页推荐模块推荐个数不足5个");
        $("#inputForm").validate({
            rules : {
                maintRecomModuleIds : {
                    checkMaxSize : "param",
                    checkMinSize : "param"
                }
            },
            messages : {
                maintRecomModuleIds : {
                    checkMinSize : "首页推荐模块推荐个数不足5个",
                    checkMaxSize : "最多只能勾选5个模块"
                },
            },
            submitHandler : function(form) {
                loading('正在提交，请稍等...');
                form.submit();
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

        $("input[name='maintRecomModuleIds']").click(function() {
            $("#addMaintRecomModule").show();
            var id = $(this).val();
            var total = $("#addMaintRecomModule").children().size() + 1;
            if ($(this).attr('checked') == "checked") {
                var domRow = '<span id="' + $(this).val() + '"><lable>' + $(this).next().text() + '</lable><lable class="lable-num">' + total + '</lable></span>';
                $("#addMaintRecomModule").append($(domRow));
            } else {
                $("span[id=" + id + "]").remove();
            }
        })

        $("input[name='maintRecomModuleIds']:checked").each(function() {
            // 默认选中的社区模块 
            var total = $("#addMaintRecomModule").children().size() + 1;
            var domRow = '<span id="' + $(this).val() + '"><lable>' + $(this).next().text() + '</lable><lable class="lable-num">' + total + '</lable></span>';
            $("#addMaintRecomModule").append($(domRow));
        })
        var len=$("#addMaintRecomModule").children().size();
        if(len>0){
            $("#addMaintRecomModule").show();
        }else{
            $("#addMaintRecomModule").hide();
        }
    });
</script>
</head>

<body>
    <ul class="nav nav-tabs">
        <li>
            <span><a href="${ctx}/module/villageLine/recommendList">模块管理 </a>> <a href="${ctx}/module/villageLine/recommendList">推荐管理 > </a><a>设置管理</a></span>
        </li>
    </ul>
    <ul class="nav nav-tabs">
        <li class="active">
            <a href="${ctx}/module/villageLine/mainRecomFrom?id=${villageLine.id}">首页推荐</a>
        </li>
        <li>
            <a href="${ctx}/module/villageLine/communityRecomFrom?id=${villageLine.id}">社区推荐</a>
        </li>
        <li >
            <a href="${ctx}/module/villageLine/lifeRecomFrom?id=${villageLine.id}">生活推荐</a>
        </li>
    </ul>
    <form:form id="inputForm" modelAttribute="villageLine" action="${ctx}/module/villageLine/updateMaintRecomModule" method="post" class="form-horizontal">
        <form:hidden path="id" />
        <sys:message content="${message}" />
        <div class="control-group">
            <label class="control-label">楼盘名称</label>
            <div class="controls">
                <label>${villageLine.villageInfo.villageName }</label>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">首页推荐模块</label>
            <div class="controls" style="border: 1px solid #ccc; padding: 5px;">
                <div style="border: 1px solid #ccc; padding: 20px" id="addMaintRecomModule"></div>
                <div style="border: 1px solid #ccc; padding: 20px; margin-top: 20px;">
                    <form:checkboxes items="${moduleList}" path="maintRecomModuleIds" itemLabel="moduleName" itemValue="id" class="required" />
                    <span class="help-inline"><font color="red">*</font> </span>
                </div>
            </div>
        </div>
        <div class="form-actions">
            <shiro:hasPermission name="module:villageLine:batchSetModule">
                <input id="btnSubmit" class="btn btn-success" type="submit" value="保 存" />&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="module:villageLine:batchSetModule">
                <input id="" class="btn btn-success" type="button" value="预览" />&nbsp;
            </shiro:hasPermission>
            <input id="btnCancel" class="btn btn-success" type="button" value="返 回" onclick="history.go(-1)" />
        </div>
    </form:form>
</body>

</html>