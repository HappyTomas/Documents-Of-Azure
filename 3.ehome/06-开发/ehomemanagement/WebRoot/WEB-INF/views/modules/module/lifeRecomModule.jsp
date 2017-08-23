<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>

<head>
<title>楼盘信息产品线管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<script src="${ctxStatic}/common/layer.js"></script>
<style type="text/css">
.none {
 visibility: hidden;
}

.content {
 width: 1200px;
 margin: 0 auto;
}

.file_upload {
 width: 48%;
 min-height: 400px;
 margin: 10px;
 position: relative;
 display: inline-block;
 vertical-align: top;
}

.file_upload>div {
 width: 100%;
 height: 100%;
}

.file_con .hide {
 width: 120px;
 height: 30px;
 opacity: 0;
 filter: alpha(opacity = 0);
 position: absolute;
 left: 0;
 z-index: 22;
}

.file_con .file_uploader, .upload_bt {
 position: absolute;
 left: 0;
 top: 0;
 display: inline-block;
 padding: 6px 14px;
 color: #fff;
 background: #2ECC71;
 text-align: center;
 z-index: 11;
 border-radius: 15px;
 cursor: pointer;
}

.upload_bt {
 left: 130px;
}

.file_con .hide:hover {
 box-shadow: 1px 2px #44795b;
}

.img_holder, .m_img_holder {
 padding-top: 40px;
}

.img_holder img, .m_img_holder img {
 max-width: 200px;
}

.img_box {
 position: relative;
 display: inline-block;
 vertical-align: initial;
 border: 1px transparent dashed;
 /*  padding: 12px;
                box-shadow: 2px 2px 10px #ccc; */
}

.img_box:hover {
 /*border: 1px #ccc dashed;*/
 
}

.img_box:hover .delete {
 display: block;
}

.img_box .delete {
 position: absolute;
 right: 1px;
 top: 0;
 display: none;
 font-family: Arial;
 font-size: 12px;
 cursor: pointer;
}

.progress {
 display: inline-block;
 margin-top: 10px;
}
</style>
<script src="${ctxStatic}/common/setImagePreview.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function() {
        //添加主导航验证
        $.validator.addMethod("checkMaxSize", function(value, element, params) {
            var num = $("input[name='lifeRecomModuleIds']:checked").size();
            if (num > 4) {
                return false;
            } else {
                return true;
            }
        }, "最多只能勾选4个模块");
        $("#inputForm").validate({
            rules : {
                lifeRecomModuleIds : {
                    checkMaxSize : "param"
                },
            },
            messages : {
                lifeRecomModuleIds : {
                    required : "请选择推荐模块",
                    checkMaxSize : "最多只能勾选4个模块"
                },
            },
            submitHandler : function(form) {
                var lifeRecomIds="";
                $("#addLifeRecomModule").find(".lifeRecom").each(function(i,dom){
                    lifeRecomIds+= $(dom).attr("id")+",";
                })
                $("#lifeRecomModule").val(lifeRecomIds);
                
                var lifeRecomSort="";
                $("#addLifeRecomModule").find(".lifeRecom").each(function(i,dom){
                    lifeRecomSort+= $(dom).find("input").val()+",";
                })
                $("#lifeRecomModuleSort").val(lifeRecomSort);
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

        $("input[name='lifeRecomModuleIds']").click(function() {
            $("#addLifeRecomModule").show();
            var id = $(this).val();
            var total = getMaxNum($("#addLifeRecomModule"));
            if ($(this).attr('checked') == "checked") {
                var domRow = '<span class="lifeRecom" id="' + $(this).val() + '">'
                +'  <lable>' + $(this).next().text() 
                +'  </lable><input style="width:20px;" class="required number" maxlength="2" value="'+total+'"></span>';
                $("#addLifeRecomModule").append($(domRow));
            } else {
                $("span[id=" + id + "]").hide().removeClass('lifeRecom');
            }
        })
        
        var alist=${fns:toJson(getlifeList)};
        var arrLife=$("#lifeRecomModuleSort").val().split(",")
        var arrIds=[];
        $('input[name="lifeRecomModuleIds"]:checked').each(function(){ 
            arrIds.push($(this).val()); 
        }); 
        for (var i=0; i<alist.length; i++){
            if($.inArray(alist[i].id, arrIds)>=0){
                var domRow = '<span class="lifeRecom" id="' +alist[i].id + '">'
                           + '  <lable>' +alist[i].moduleName + '</lable>'
                           + '  <input style="width:20px;" class="required number" maxlength="2" value="'+arrLife[i]+'"></span>';
                $("#addLifeRecomModule").append($(domRow));
            }
        }
        
        
        var len = $("#addLifeRecomModule").children().size();
        if (len > 0) {
            $("#addLifeRecomModule").show();
        } else {
            $("#addLifeRecomModule").hide();
        }
    });
    //获取以选取的最大值
    function getMaxNum(obj){
        var maxNum=0;
        $(obj).find(".number").each(function(i,dom){
            var newNum=Number($(this).val());
            if(newNum>=maxNum){
               maxNum=newNum;
           }
        })
        return (maxNum+1);
    }
    //模块的change事件
    function getBuslist(obj,count) {
        var moduleId=$(obj).val();
        $.ajax({
            type : "POST",
            url : ctx + "/business/businessInfo/getBusListByModule",
            data : {
                moduleId : moduleId
            },
            dataType : "JSON",
            success : function(data) {
                $("#businessinfoId"+count).empty();
                var option = "<option value=''>商家名称</option>";
                $.each(data, function(indx, item) {
                    option += "<option value='"+item.id+"'>" + item.businessName + "</option>";
                })
                $("#businessinfoId"+count).append(option);
                $("#businessinfoId"+count).val($("#HidBusinessinfoId"+count).val());//修改初始时，带值选中
            }
        })
    };
    //添加商家推荐1
    function addRowModule(){
        var domRow='';
        var index=0;
        var total=$("#recomOne").find(".controls").size()-1;
        if(total==0){
            index=0;
        }else{
            index=total*2;
        }
        var describes1="";
        var describes2="";
        domRow+='<div class="controls"  id="moduleRow'+total+'" style="border: 1px solid #ccc;margin-top:10px;  padding: 20px;">';
        domRow+='<div id="recomModuleList'+index+'" style="padding-top: 10px;">'
            +'    <lable>推荐一</lable>'
            +'     <select id="module'+index+'" onchange="getBuslist(this,'+index+')"  name="recomModuleList['+index+'].recomModuleId" class="input-small">'
            +'         <option value="" >模块选择</option>'
            +'         <c:forEach items="${moduleList}" var="module">'
            +'             <option value="${module.id}" >${module.moduleName}</option>'
            +'         </c:forEach>'
            +'     </select>'
            +'     <select id="businessinfoId'+index+'" name="recomModuleList['+index+'].recomBusinessId" class="input-small">'
            +'          <option value="">商家名称</option>'
            +'     </select> <input id="HidBusinessinfoId'+(index)+'" type="hidden"/>'
            +'     <input id="describes'+index+'" name="recomModuleList['+index+'].describes" type="text"  value="'+describes1+'"  maxlength="8" class="min:0 input-small "/>'
            +'     <span> 不超过8个字 </span>'
            //图片上传相关标签 开始
            +'     <span id="scan" style="position: relative" class="btn btn-primary input-group-addon">上传图片'
            +'     <input onchange="synchroImg_module(this,\''+index+'\')" id="module_file_'+index+'" type="file"  name="file'+index+'" style="width: 100px; height: 40px; position: absolute; top: -6px; left: -4px; opacity: 0; filter: alpha(opacity = 0)" >'
            +'     </span><lable id="module_lable_'+index+'"> 未选择图片 </lable>'
            
            +'     <div class="img_box">'
            +'       <span id="module_img_del_'+index+'" class="delete" onclick="moduleImgDel('+index+')"> <img src="${ctxStatic}/images/a7.png" class="close-upimg"></span>'
            +'       <img id="moduleImg'+index+'" src="" style="width:45px; height:45px;display:none" />'
            +'     </div>'
            +'     <lable class="hide" id="module_lable_size_'+index+'"> 图片尺寸 ***</lable>'
            //图片上传相关标签结束
            +'     <input id="recomModuleFlag'+index+'" type="hidden" name="recomModuleList['+index+'].delFlag"  />'
            +'     <input id="recomModuleId'+index+'" type="hidden" name="recomModuleList['+index+'].id"  />'
            +'     <input id="delModulePicId'+index+'" type="hidden" name="recomModuleList['+index+'].picId"  />'
            +'  </div>';
         domRow+='<div id="recomModuleList'+(index+1)+'" style="padding-top: 10px;">'
            +'    <lable>推荐二</lable>'
            +'     <select id="module'+(index+1)+'" onchange="getBuslist(this,'+(index+1)+')"  name="recomModuleList['+(index+1)+'].recomModuleId" class="input-small">'
            +'         <option value="" >模块选择</option>'
            +'         <c:forEach items="${moduleList}" var="module">'
            +'             <option value="${module.id}" >${module.moduleName}</option>'
            +'         </c:forEach>'
            +'     </select>'
            +'     <select id="businessinfoId'+(index+1)+'" name="recomModuleList['+(index+1)+'].recomBusinessId" class="input-small">'
            +'             <option value="">商家名称</option>'
            +'     </select><input id="HidBusinessinfoId'+(index+1)+'" type="hidden"/>'
            +'     <input id="describes'+(index+1)+'" name="recomModuleList['+(index+1)+'].describes" type="text"  value="'+describes2+'"  maxlength="8" class="min:0 input-small"/>'
            +'     <span> 不超过8个字 </span>'
            //图片上传相关标签 开始
            +'     <span id="scan" style="position: relative" class="btn btn-primary input-group-addon">上传图片'
            +'     <input onchange="synchroImg_module(this,\''+(index+1)+'\')" id="module_file_'+(index+1)+'" type="file"  name="file'+(index+1)+'" style="width: 100px; height: 40px; position: absolute; top: -6px; left: -4px; opacity: 0; filter: alpha(opacity = 0)" >'
            +'     </span><lable id="module_lable_'+(index+1)+'"> 未选择图片 </lable>'
            
            +'     <div class="img_box">'
            +'       <span id="module_img_del_'+(index+1)+'" class="delete" onclick="moduleImgDel('+(index+1)+')"> <img src="${ctxStatic}/images/a7.png" class="close-upimg"></span>'
            +'       <img id="moduleImg'+(index+1)+'" src="" style="width:45px; height:45px;display:none" />'
            +'     </div>'
            +'     <lable class="hide" id="module_lable_size_'+(index+1)+'"> 图片尺寸 ***</lable>'
            //图片上传相关标签结束
            +'     <input id="recomModuleFlag'+(index+1)+'" type="hidden" name="recomModuleList['+(index+1)+'].delFlag"  />'
            +'     <input id="recomModuleId'+(index+1)+'" type="hidden" name="recomModuleList['+(index+1)+'].id"  />'
            +'     <input id="delModulePicId'+(index+1)+'" type="hidden" name="recomModuleList['+(index+1)+'].picId"  />'
            +'  </div>';
            domRow+='</div>';
            domRow+='<div class="add-remove" style="float: right; margin-top: -20px; margin-right: -60px;"><a onclick="addRowModule()">添加</a>  <a onclick="removeRowModule(this,'+total+')">删除</a></div>';
            $("#recomOne").append($(domRow));
           
           
    }
    //移除模块推荐图片
    function moduleImgDel(index){
        if(moduleData[index]!='undefined'){
            $("#delModulePicId"+index).val(moduleData[index].picId);
        }
        $("#module_lable_size_"+index).hide();
        $("#moduleImg"+index).hide();
        $("#module_lable_"+index).show();
    }
    //商家推荐1 -- 图片上传
    function synchroImg_module(obj,index_num) {
       setImagePreview2($(obj),$("#moduleImg"+index_num),$("#recomModuleList"+index_num),$("#module_lable_size_"+index_num),$("#module_lable_"+index_num));
    };
    //商家推荐1 移除
    function removeRowModule(obj,index_num) {
        var index=0;
        if(index_num==0){
            index=0;
        }else{
            index=index_num*2;
        }
        var total = $("#recomOne").find(".controls").size()-1;//
        if(total==1){
            top.$.jBox.tip('请至少保留一组商家推荐1数据', 'error');
            return;
        }
        if($("#recomModuleId"+index).val()!=""&&$("#recomModuleId"+index_num).val()!=null){
            $(obj).parent().addClass("hide");
            $("#moduleRow"+index_num).addClass("hide");
            $("#moduleRow"+index_num).removeClass("controls");
            $("#recomModuleFlag"+index).val("1");
            $("#recomModuleFlag"+(index+1)).val("1")
        }else{
            $(obj).parent().remove();
            $("#moduleRow"+index_num).remove();
        }
    }
  //绑定商家推荐1
    var moduleData= ${fns:toJson(villageLine.recomModuleList)};
    function initRecomModule(){
        for (var i=0; i<moduleData.length/2-1; i++){
            addRowModule();
        }
        for (var i=0; i<moduleData.length; i++){
            console.log(moduleData[i].delFlag)
            $("#recomModuleFlag"+i).val(moduleData[i].delFlag);//给删除标记赋值
            $("#recomModuleId"+i).val(moduleData[i].id);//给删除标记赋值
            $("#HidBusinessinfoId"+i).val(moduleData[i].recomBusinessId);
            $("#module"+i).val(moduleData[i].recomModuleId).trigger("change");
            $("#businessinfoId"+i).val(moduleData[i].recomBusinessId);
            $("#describes"+i).val(moduleData[i].describes);
            if(moduleData[i].picUrl!=undefined){
              $("#module_lable_"+i).hide();
              $("#moduleImg"+i).show();
              getImgSize($("#module_lable_size_"+i),moduleData[i].picUrl);
            }
            $("#moduleImg"+i).attr("src",moduleData[i].picUrl);//给推荐详情的图片赋值
        }
    }
  function getImgSize(obj,picUrl){
      var img = new Image();
      img.onload = function () {
          $(obj).show();
          $(obj).html("图片尺寸("+this.width+"X"+this.height+")");
      }
      img.src=picUrl;
  }
   
    //添加专题推荐1
    function addSpecial(){
        var total=$("#recomTwo").find(".controls").size();
        var domRow='<div class="controls" id="recomSpecialList'+total+'" style="border: 1px solid #ccc; margin-top: 10px; padding: 20px;">'
                +'  <label>专题名称 </label>'
                +'     <input id="recomSpecialId'+total+'" type="hidden" name="recomSpecialList['+(total)+'].id"  />'
                +'        <input id="specialName'+total+'" name="recomSpecialList['+total+'].specialName" value="" class="input-small" maxlength="5" />'
                +'        <label>不超过5个字 </label>'
                +'      <div style="margin-top: 10px">'
                +'          <label> 推荐一: </label>'
                +'          <input id="recomSpecialDetailId'+total+'0" type="hidden" name="recomSpecialList['+(total)+'].recomSpecialDetailList[0].id" />'
                +'          <input onchange="getSpecialModule(this,1,\''+total+'0\')" name="recomSpecialList['+total+'].recomSpecialDetailList[0].recomType" value="1" type="radio" />'
                +'          <span>模块列表 </span>'
                +'          <select id="recomModuleId'+total+0+'"  name="recomSpecialList['+total+'].recomSpecialDetailList[0].recomModuleId" class="input-small">'
                +'             <option value="">模块选择</option>'
                +'          </select> <input id="HidRecomModuleId'+total+'0" type="hidden"/>'
                +'          <input onchange="getSpecialModule(this,0,\''+total+'0\')" name="recomSpecialList['+total+'].recomSpecialDetailList[0].recomType" value="0" type="radio" />'
                +'          <span>商家列表</span>'
                +'          <select onchange="getBusiList(this,\''+total+'0\')" id="businessCategoryDictId'+total+0+'"  name="recomSpecialList['+total+'].recomSpecialDetailList[0].businessCategoryDictId" class="input-small">'
                +'             <option value="">商家分类</option>'
                +'          </select> <input id="HidBusinessCategoryDictId'+total+'0" type="hidden"/>'
                +'          <select id="recomBusinessId'+total+0+'" name="recomSpecialList['+total+'].recomSpecialDetailList[0].recomBusinessId" class="input-small">'
                +'             <option value="">商家选择</option>'
                +'          </select> <input id="HidRecomBusinessId'+total+'0" type="hidden"/>'
                +'          <input id="specialDescribes'+total+'0" name="recomSpecialList['+total+'].recomSpecialDetailList[0].describes" maxlength="8" class="min:0 input-small" type="text" />'
                +'          <span> 不超过8个字 </span>'
                //专题推荐图片上传  开始
                +'          <span id="scan" style="position: relative" class="btn btn-primary input-group-addon">上传图片'
                +'          <input onchange="synchroImg_special(this,\''+total+'0\')" id="specialFile_'+total+'0" type="file"  name="specialFile_'+total+'_0" style="width: 100px; height: 40px; position: absolute; top: -6px; left: -4px; opacity: 0; filter: alpha(opacity = 0)" >'
                +'          </span><lable id="special_lable_'+total+'0"> 未选择图片 </lable>'
                +'         <div class="img_box">'
                +'           <span id="special_img_del_'+total+'0" class="delete" onclick="specialImgDel('+total+',0)"> <img src="${ctxStatic}/images/a7.png" class="close-upimg"></span>'
                +'            <img id="specialImg'+total+'0" src="" style="width:45px; height:45px;display:none" />'
                +'         </div>'
                +'         <lable class="hide" id="special_lable_size_'+total+'0"> 图片尺寸 ***</lable>'
                +'         <input id="delSpecialPicId'+total+'0" type="hidden" name="recomSpecialList['+total+'].recomSpecialDetailList[0].picId"  />'
                //专题推荐图片上传  结束
                +'      </div>'
                +'      <div style="margin-top: 10px">'
                +'          <label> 推荐二: </label>'
                +'          <input id="recomSpecialDetailId'+total+'1" type="hidden" name="recomSpecialList['+(total)+'].recomSpecialDetailList[1].id" />'
                +'          <input onchange="getSpecialModule(this,1,\''+total+'1\')" name="recomSpecialList['+total+'].recomSpecialDetailList[1].recomType" value="1" type="radio" />'
                +'          <span>模块列表 </span>'
                +'          <select id="recomModuleId'+total+1+'"  name="recomSpecialList['+total+'].recomSpecialDetailList[1].recomModuleId" class="input-small">'
                +'             <option value="">模块选择</option>'
                +'          </select> <input id="HidRecomModuleId'+total+'1" type="hidden"/>'
                +'          <input onchange="getSpecialModule(this,0,\''+total+'1\')" name="recomSpecialList['+total+'].recomSpecialDetailList[1].recomType" value="0" type="radio" />'
                +'          <span>商家列表</span>'
                +'          <select onchange="getBusiList(this,\''+total+'1\')" id="businessCategoryDictId'+total+1+'" name="recomSpecialList['+total+'].recomSpecialDetailList[1].businessCategoryDictId" class="input-small">'
                +'             <option value="">商家分类</option>'
                +'          </select>  <input id="HidBusinessCategoryDictId'+total+'1" type="hidden"/>'
                +'          <select id="recomBusinessId'+total+1+'" name="recomSpecialList['+total+'].recomSpecialDetailList[1].recomBusinessId" class="input-small">'
                +'             <option value="">商家选择</option>'
                +'          </select> <input id="HidRecomBusinessId'+total+'1" type="hidden"/>'
                +'          <input id="specialDescribes'+total+'1" name="recomSpecialList['+total+'].recomSpecialDetailList[1].describes" maxlength="8" class="min:0 input-small" type="text" />'
                +'          <span> 不超过8个字 </span> '
                //专题推荐图片上传  开始
                +'          <span id="scan" style="position: relative" class="btn btn-primary input-group-addon">上传图片'
                +'          <input onchange="synchroImg_special(this,\''+total+'1\')" id="specialFile_'+total+'1" type="file"  name="specialFile_'+total+'_1" style="width: 100px; height: 40px; position: absolute; top: -6px; left: -4px; opacity: 0; filter: alpha(opacity = 0)" >'
                +'          </span><lable id="special_lable_'+total+'1"> 未选择图片 </lable>'


                +'          <div class="img_box">'
                +'           <span id="special_img_del_'+total+'1" class="delete" onclick="specialImgDel('+total+',1)"> <img src="${ctxStatic}/images/a7.png" class="close-upimg"></span>'
                +'           <img id="specialImg'+total+'1" src="" style="width:45px; height:45px;display:none" />'
                +'          </div>'
                +'          <lable class="hide" id="special_lable_size_'+total+'1"> 图片尺寸 ***</lable>'
                +'          <input id="delSpecialPicId'+total+'1" type="hidden" name="recomSpecialList['+total+'].recomSpecialDetailList[1].picId"  />'
               
                //专题推荐图片上传  结束'
                +'      </div>'
                +'      <div style="margin-top: 10px">'
                +'          <label> 推荐三: </label>'
                +'          <input id="recomSpecialDetailId'+total+'2" type="hidden" name="recomSpecialList['+(total)+'].recomSpecialDetailList[2].id" />'
                +'          <input onchange="getSpecialModule(this,1,\''+total+'2\')" name="recomSpecialList['+total+'].recomSpecialDetailList[2].recomType" value="1" type="radio" />'
                +'          <span>模块列表 </span>'
                +'          <select id="recomModuleId'+total+2+'"  name="recomSpecialList['+total+'].recomSpecialDetailList[2].recomModuleId" class="input-small">'
                +'             <option value="">模块选择</option>'
                +'          </select> <input id="HidRecomModuleId'+total+'2" type="hidden"/>'
                +'          <input onchange="getSpecialModule(this,0,\''+total+'2\')" name="recomSpecialList['+total+'].recomSpecialDetailList[2].recomType" value="0" type="radio" />'
                +'          <span>商家列表</span>'
                +'          <select onchange="getBusiList(this,\''+total+'2\')" id="businessCategoryDictId'+total+2+'" name="recomSpecialList['+total+'].recomSpecialDetailList[2].businessCategoryDictId" class="input-small">'
                +'             <option value="">商家分类</option>'
                +'          </select> <input id="HidBusinessCategoryDictId'+total+'2" type="hidden"/>'
                +'          <select id="recomBusinessId'+total+2+'" name="recomSpecialList['+total+'].recomSpecialDetailList[2].recomBusinessId" class="input-small">'
                +'             <option value="">商家选择</option>'
                +'          </select> <input id="HidRecomBusinessId'+total+'2" type="hidden"/>'
                +'          <input id="specialDescribes'+total+'2" name="recomSpecialList['+total+'].recomSpecialDetailList[2].describes" maxlength="8" class="min:0 input-small" type="text" />'
                +'          <span> 不超过8个字 </span>'
                //专题推荐图片上传  开始
                +'          <span id="scan" style="position: relative" class="btn btn-primary input-group-addon">上传图片'
                +'          <input onchange="synchroImg_special(this,\''+total+'2\')" id="specialFile_'+total+'2" type="file"  name="specialFile_'+total+'_2" style="width: 100px; height: 40px; position: absolute; top: -6px; left: -4px; opacity: 0; filter: alpha(opacity = 0)" >'
                +'          </span><lable id="special_lable_'+total+'2"> 未选择图片 </lable>'

                +'          <div class="img_box">'
                +'           <span id="special_img_del_'+total+'2" class="delete" onclick="specialImgDel('+total+',2)"> <img src="${ctxStatic}/images/a7.png" class="close-upimg"></span>'
                +'           <img id="specialImg'+total+'2" src="" style="width:45px; height:45px;display:none" />'
                +'          </div>'
                +'          <lable class="hide" id="special_lable_size_'+total+'2"> 图片尺寸 ***</lable>'
                +'          <input id="delSpecialPicId'+total+'2" type="hidden" name="recomSpecialList['+total+'].recomSpecialDetailList[2].picId"  />'
               
                //专题推荐图片上传  结束'
                +'      </div>'
                +'      <div style="margin-top: 10px">'
                +'          <label> 推荐四: </label>'
                +'          <input id="recomSpecialDetailId'+total+'3" type="hidden" name="recomSpecialList['+(total)+'].recomSpecialDetailList[3].id" />'
                +'          <input onchange="getSpecialModule(this,1,\''+total+'3\')" name="recomSpecialList['+total+'].recomSpecialDetailList[3].recomType" value="1" type="radio" />'
                +'          <span>模块列表 </span>'
                +'          <select id="recomModuleId'+total+3+'"  name="recomSpecialList['+total+'].recomSpecialDetailList[3].recomModuleId" class="input-small">'
                +'             <option value="">模块选择</option>'
                +'          </select> <input id="HidRecomModuleId'+total+'3" type="hidden"/>'
                +'          <input onchange="getSpecialModule(this,0,\''+total+'3\')" name="recomSpecialList['+total+'].recomSpecialDetailList[3].recomType" value="0" type="radio" />'
                +'          <span>商家列表</span>'
                +'          <select onchange="getBusiList(this,\''+total+'3\')" id="businessCategoryDictId'+total+3+'" name="recomSpecialList['+total+'].recomSpecialDetailList[3].businessCategoryDictId" class="input-small">'
                +'             <option value="">商家分类</option>'
                +'          </select>  <input id="HidBusinessCategoryDictId'+total+'3" type="hidden"/>'
                +'          <select id="recomBusinessId'+total+3+'" name="recomSpecialList['+total+'].recomSpecialDetailList[3].recomBusinessId" class="input-small">'
                +'             <option value="">商家选择</option>'
                +'          </select> <input id="HidRecomBusinessId'+total+'3" type="hidden"/>'
                +'          <input id="specialDescribes'+total+'3" name="recomSpecialList['+total+'].recomSpecialDetailList[3].describes" maxlength="8" class="min:0 input-small"  type="text" />'
                +'          <span> 不超过8个字 </span>'
                //专题推荐图片上传  开始
                +'          <span id="scan" style="position: relative" class="btn btn-primary input-group-addon">上传图片'
                +'          <input onchange="synchroImg_special(this,\''+total+'3\')" id="specialFile_'+total+'3" type="file"  name="specialFile_'+total+'_3" style="width: 100px; height: 40px; position: absolute; top: -6px; left: -4px; opacity: 0; filter: alpha(opacity = 0)" >'
                +'          </span><lable id="special_lable_'+total+'3"> 未选择图片 </lable>'


                +'          <div class="img_box">'
                +'           <span id="special_img_del_'+total+'3" class="delete" onclick="specialImgDel('+total+',3)"> <img src="${ctxStatic}/images/a7.png" class="close-upimg"></span>'
                +'          <img id="specialImg'+total+'3" src="" style="width:45px; height:45px;display:none" />'
                +'          </div>'
                +'          <lable class="hide" id="special_lable_size_'+total+'3"> 图片尺寸 ***</lable>'
                +'          <input id="delSpecialPicId'+total+'3" type="hidden" name="recomSpecialList['+total+'].recomSpecialDetailList[3].picId"  />'
               
                //专题推荐图片上传  结束'
                +'      </div>'
                +'      <div style="margin-top: 10px">'
                +'          <label> 推荐五: </label>'
                +'          <input id="recomSpecialDetailId'+total+'4" type="hidden" name="recomSpecialList['+(total)+'].recomSpecialDetailList[4].id" />'
                +'          <input onchange="getSpecialModule(this,1,\''+total+'4\')" name="recomSpecialList['+total+'].recomSpecialDetailList[4].recomType" value="1" type="radio" />'
                +'          <span>模块列表 </span>'
                +'          <select id="recomModuleId'+total+4+'"  name="recomSpecialList['+total+'].recomSpecialDetailList[4].recomModuleId" class="input-small">'
                +'             <option value="">模块选择</option>'
                +'          </select> <input id="HidRecomModuleId'+total+'4" type="hidden"/>'
                +'          <input onchange="getSpecialModule(this,0,\''+total+'4\')" name="recomSpecialList['+total+'].recomSpecialDetailList[4].recomType" value="0" type="radio" />'
                +'          <span>商家列表</span>'
                +'          <select onchange="getBusiList(this,\''+total+'4\')" id="businessCategoryDictId'+total+4+'" name="recomSpecialList['+total+'].recomSpecialDetailList[4].businessCategoryDictId" class="input-small">'
                +'             <option value="">商家分类</option>'
                +'          </select> <input id="HidBusinessCategoryDictId'+total+'4" type="hidden"/>'
                +'          <select id="recomBusinessId'+total+4+'" name="recomSpecialList['+total+'].recomSpecialDetailList[4].recomBusinessId" class="input-small">'
                +'             <option value="">商家选择</option>'
                +'          </select> <input id="HidRecomBusinessId'+total+'4" type="hidden"/>'
                +'          <input id="specialDescribes'+total+'4" name="recomSpecialList['+total+'].recomSpecialDetailList[4].describes" maxlength="8" class="min:0 input-small"  type="text" />'
                +'          <span> 不超过8个字 </span>'
                //专题推荐图片上传  开始
                +'          <span id="scan" style="position: relative" class="btn btn-primary input-group-addon">上传图片'
                +'          <input onchange="synchroImg_special(this,\''+total+'4\')" id="specialFile_'+total+'4" type="file"  name="specialFile_'+total+'_4" style="width: 100px; height: 40px; position: absolute; top: -6px; left: -4px; opacity: 0; filter: alpha(opacity = 0)" >'
                +'          </span><lable id="special_lable_'+total+'4"> 未选择图片 </lable>'
 

                +'          <div class="img_box">'
                +'           <span id="special_img_del_'+total+'4" class="delete" onclick="specialImgDel('+total+',4)"> <img src="${ctxStatic}/images/a7.png" class="close-upimg"></span>'
                +'          <img id="specialImg'+total+'4" src="" style="width:45px; height:45px;display:none" />'
                +'          </div>'
                +'          <lable class="hide" id="special_lable_size_'+total+'4"> 图片尺寸 ***</lable>'
                +'          <input id="delSpecialPicId'+total+'4" type="hidden" name="recomSpecialList['+total+'].recomSpecialDetailList[4].picId"  />'
               
                //专题推荐图片上传  结束'
                +'      </div>'
                +'      <div style="margin-top: 10px">'
                +'          <label> 推荐六: </label>'
                +'          <input id="recomSpecialDetailId'+total+'5" type="hidden" name="recomSpecialList['+(total)+'].recomSpecialDetailList[5].id" />'
                +'          <input onchange="getSpecialModule(this,1,\''+total+'5\')" name="recomSpecialList['+total+'].recomSpecialDetailList[5].recomType" value="1" type="radio" />'
                +'          <span>模块列表 </span>'
                +'          <select id="recomModuleId'+total+5+'"  name="recomSpecialList['+total+'].recomSpecialDetailList[5].recomModuleId" class="input-small">'
                +'             <option value="">模块选择</option>'
                +'          </select> <input id="HidRecomModuleId'+total+'5" type="hidden"/>'
                +'          <input onchange="getSpecialModule(this,0,\''+total+'5\')" name="recomSpecialList['+total+'].recomSpecialDetailList[5].recomType" value="0" type="radio" />'
                +'          <span>商家列表</span>'
                +'          <select onchange="getBusiList(this,\''+total+'5\')"  id="businessCategoryDictId'+total+5+'" name="recomSpecialList['+total+'].recomSpecialDetailList[5].businessCategoryDictId" class="input-small">'
                +'             <option value="">商家分类</option>'
                +'          </select> <input id="HidBusinessCategoryDictId'+total+'5" type="hidden"/>'
                +'          <select id="recomBusinessId'+total+5+'" name="recomSpecialList['+total+'].recomSpecialDetailList[5].recomBusinessId" class="input-small">'
                +'             <option value="">商家选择</option>'
                +'          </select> <input id="HidRecomBusinessId'+total+'5" type="hidden"/>'
                +'          <input id="specialDescribes'+total+'5" name="recomSpecialList['+total+'].recomSpecialDetailList[5].describes" maxlength="8" class="min:0 input-small"  type="text" />'
                +'          <span> 不超过8个字 </span>'
                //专题推荐图片上传  开始
                +'          <span id="scan" style="position: relative" class="btn btn-primary input-group-addon">上传图片'
                +'          <input onchange="synchroImg_special(this,\''+total+'5\')" id="specialFile_'+total+'5" type="file"  name="specialFile_'+total+'_5" style="width: 100px; height: 40px; position: absolute; top: -6px; left: -4px; opacity: 0; filter: alpha(opacity = 0)" >'
                +'          </span><lable id="special_lable_'+total+'5"> 未选择图片 </lable>'
                +'          <div class="img_box">'
                +'           <span id="special_img_del_'+total+'5" class="delete" onclick="specialImgDel('+total+',5)"> <img src="${ctxStatic}/images/a7.png" class="close-upimg"></span>'
                +'          <img id="specialImg'+total+'5" src="" style="width:45px; height:45px;display:none" />'
                +'          </div>'
                +'          <lable class="hide" id="special_lable_size_'+total+'5"> 图片尺寸 ***</lable>'
                +'          <input id="delSpecialPicId'+total+'5" type="hidden" name="recomSpecialList['+total+'].recomSpecialDetailList[5].picId"  />'
                //专题推荐图片上传  结束
                +'      </div>'
                +'     <div style="float: left;"></div><input id="recomSpecialFlag'+total+'" type="hidden"  value="0" name="recomSpecialList['+total+'].delFlag"  />'
                +'  </div>';
         domRow+='<div class="add-remove" style="float: right; margin-top: -20px; margin-right: -60px;"><a onclick="addSpecial()">添加</a>  <a onclick="removeRowSpecial(this,'+total+')">删除</a></div>';
        $("#recomTwo").append($(domRow));
    }
    //专题推荐1 -- 图片上传
    function synchroImg_special(obj,index_num) {
       setImagePreview2($(obj),$("#specialImg"+index_num),$("#recomSpecialList"+index_num),$("#special_lable_size_"+index_num),$("#special_lable_"+index_num));
    };
    //移除专题推荐图片
    function specialImgDel(total,index){
        console.log(specialData[total].recomSpecialDetailList[index].picId);
        if(specialData[total].recomSpecialDetailList[index]!='undefined'){
            $("#delSpecialPicId"+total+index).val(specialData[total].recomSpecialDetailList[index].picId);
        }
        $("#special_lable_size_"+total+index).hide();
        $("#specialImg"+total+index).hide();
        $("#special_lable_"+total+index).show();
    }
    //专题推荐 移除
    function removeRowSpecial(obj,index_num) {
        var total = $("#recomTwo").find(".controls").size();//
        console.log(total);
        if(total==1){
            top.$.jBox.tip('请至少保留一组专题推荐数据', 'error');
            return;
        }
        if($("#recomSpecialId"+index_num).val()!=""&&$("#recomSpecialId"+index_num).val()!=null){
            $(obj).parent().addClass("hide");
            $("#recomSpecialList"+index_num).addClass("hide");
            $("#recomSpecialList"+index_num).removeClass("controls");
            $("#recomSpecialFlag"+index_num).val("1");
        }else{
            $(obj).parent().remove();
            $("#recomSpecialList"+index_num).remove();
        }
    }
  //绑定专题推荐
    var specialData= ${fns:toJson(villageLine.recomSpecialList)};
    function initRecomSpecial(){
        for (var i=0; i<specialData.length-1; i++){
            addSpecial();
        }
        for (var i=0; i<specialData.length; i++){
           $("#specialName"+i).val(specialData[i].specialName);//给专题名称赋值
           $("#recomSpecialFlag"+i).val(specialData[i].delFlag);//给删除标记赋值
           $("#recomSpecialId"+i).val(specialData[i].id);//给删除标记赋值
            for (var j=0; j<specialData[i].recomSpecialDetailList.length; j++){
               $("#specialDescribes"+i+j).val(specialData[i].recomSpecialDetailList[j].describes);
               $("#HidRecomModuleId"+i+j).val(specialData[i].recomSpecialDetailList[j].recomModuleId);
               $("#HidRecomBusinessId"+i+j).val(specialData[i].recomSpecialDetailList[j].recomBusinessId);
               $("#HidBusinessCategoryDictId"+i+j).val(specialData[i].recomSpecialDetailList[j].businessCategoryDictId);
               $("#recomSpecialDetailId"+i+j).val(specialData[i].recomSpecialDetailList[j].id);//给推荐详情的ID赋值
               if(specialData[i].recomSpecialDetailList[j].picUrl!=undefined){
                   $("#special_lable_"+i+j).hide();
                   $("#specialImg"+i+j).show();
                   getImgSize($("#special_lable_size_"+i+j),specialData[i].recomSpecialDetailList[j].picUrl);
               }
               $("#specialImg"+i+j).attr("src",specialData[i].recomSpecialDetailList[j].picUrl);//给推荐详情的图片赋值
               var recomType=specialData[i].recomSpecialDetailList[j].recomType;
               $(":radio[name='recomSpecialList["+i+"].recomSpecialDetailList["+j+"].recomType'][value='" + recomType + "']").prop("checked", "checked").trigger("change");
            }
        }
    }
    //根据商家分类获取商家列表
    function getBusiList(obj,index_num){
        var categorydict=$(obj).val();
        $.ajax({
            type : "POST",
            url : ctx + "/business/businessInfo/bindBusinessList",
            data : {
                categorydict : categorydict,
            },
            dataType : "JSON",
            success : function(data) {
                $("#recomBusinessId"+index_num).empty().append("");
                var option = "";
                //console.log(data);
                $.each(data, function(indx, item) {
                    option += "<option value='"+item.id+"'>" + item.businessName + "</option>";
                })
                $("#recomBusinessId"+index_num).append(option);
                $("#recomBusinessId"+index_num).val($("#HidRecomBusinessId"+index_num).val()).trigger("change");//修改初始时，带值选中
            }
        })
    }
    //添加商家推荐2
    function addBusType(){
        var total=$("#recomThree").find(".controls").size();
        var domRow='<div class="controls" id="recomBusTypeModule'+total+'" style="border: 1px solid #ccc; margin-top: 10px; padding: 20px;">'
              +'      <div id="recomBusTypeList'+total+'" >'
              +'        <lable>推荐一</lable>'
              +'        <select id="recomTypeBusId'+total+'" onchange="getTypeList(this,'+total+')" name="recomBusTypeList['+total+'].recomBusinessId" class="input-small">'
              +'          <option value="">商家名称</option>'
              +'          <c:forEach items="${allBusList}" var="bus">'
              +'             <option value="${bus.id}" >${bus.businessName}</option>'
              +'          </c:forEach>'
              +'        </select>'
              +'      </div>'
              +'      <input id="recomBusTypeFlag'+total+'" type="hidden" name="recomBusTypeList['+total+'].delFlag"  />'
              +'      <input id="recomBusTypeId'+total+'" type="hidden" name="recomBusTypeList['+total+'].id"  />'
              +'    </div>';
         domRow+='<div class="add-remove" style="float: right; margin-top: -20px; margin-right: -60px;"><a onclick="addBusType()">添加</a>  <a onclick="removeRowBusType(this,'+total+')">删除</a></div>';
        $("#recomThree").append($(domRow));
    }
    var busData= ${fns:toJson(villageLine.recomBusTypeList)};
    function getTypeList(obj,index_num){
        var businessinfoId=$(obj).val();
        $.ajax({
            type : "POST",
            url : ctx + "/module/villageLine/getTypeList",
            data : {
                businessinfoId : businessinfoId
            },
            dataType : "JSON",
            success : function(data) {
                //添加之前移除分类信息
                $(obj).nextAll().remove();
                //开始添加分类信息
                var domRow='';
                $.each(data, function(indx, item) {
                    console.log(indx);
                    domRow+='<div id="recomBusTypeDetailList'+index_num+indx+'" style="margin-top: 10px;">'
                          +'  <input value="'+item.prodType+'" onclick="getBusTypeId(this,\''+index_num+indx+'\')" id="cbType'+index_num+indx+'" type="checkbox" class="min:1" >'
                          +'  <input id="buy_type_id_'+index_num+indx+'" class="hide"  name="recomBusTypeList['+index_num+'].recomBusTypeDetailList['+indx+'].id">'
                          +'  <input id="prodTypeId'+index_num+indx+'" class="hide" name="recomBusTypeList['+index_num+'].recomBusTypeDetailList['+indx+'].prodType">'
                          +'  <span>'+item.categoryName+'</span>'
                          +'  <span id="scan" style="position: relative" class="btn btn-primary input-group-addon">上传图片'
                          +'  <input onchange="synchroImg(this,\''+index_num+indx+'\')" id="busTyefile_'+index_num+indx+'" type="file"  name="busTyefile_'+index_num+'_'+indx+'" style="width: 100px; height: 40px; position: absolute; top: -6px; left: -4px; opacity: 0; filter: alpha(opacity = 0)" >'
                          +'  </span><lable id="type_lable_'+index_num+indx+'"> 未选择图片 </lable>'
                          
                        
                          
                          +'  <div class="img_box">'
                          +'     <span id="busType_img_del_'+index_num+indx+'" class="delete" onclick="buyTypeImgDel('+index_num+','+indx+')"> <img src="${ctxStatic}/images/a7.png" class="close-upimg"></span>'
                          +'     <img id="imgPreview_'+index_num+indx+'" src="" style="width:45px; height:45px;display:none" />'
                          +'  </div>'
                          +'  <lable class="hide" id="lable_size_'+index_num+indx+'"> 图片尺寸 ***</lable>'
                          +'  <input id="delBusTypePicDetailId'+index_num+indx+'" type="hidden" name="recomBusTypeList['+index_num+'].recomBusTypeDetailList['+indx+'].picId"  />'
                          
                          
                          +'  <input disabled="disabled" onclick="getDefaultFlagId(this,\''+index_num+indx+'\','+index_num+')" id="defaultFlag'+index_num+indx+'" type="radio" value="1"  name="defaultFlag['+index_num+']"><span>设置默认</span>'
                          +'  <input id="defaultFlag_'+index_num+indx+'" class="hide"  name="recomBusTypeList['+index_num+'].recomBusTypeDetailList['+indx+'].defaultFlag">'
                          +'  <input id="busType_del_Flag_'+index_num+indx+'" value="0" class="hide"  name="recomBusTypeList['+index_num+'].recomBusTypeDetailList['+indx+'].delFlag">'
                          +'</div>'
                })
                $("#recomBusTypeList"+index_num).append($(domRow));
                initBusType();
            }
        })
    }
    //图片上传
    function synchroImg(obj,index_num) {
        //console.log(index_num)
        //console.log($("#imgPreview_"+index_num));
        setImagePreview2($(obj),$("#imgPreview_"+index_num),$("#recomBusTypeDetailList"+index_num),$("#lable_size_"+index_num),$("#type_lable_"+index_num));
    };
    //移除专题推荐图片
    function buyTypeImgDel(total,index){
        console.log(busData[total].recomBusTypeDetailList[index].picId);
        if(busData[total].recomBusTypeDetailList[index]!='undefined'){
            $("#delBusTypePicDetailId"+total+index).val(busData[total].recomBusTypeDetailList[index].picId);
        }
        $("#lable_size_"+total+index).hide();
        $("#imgPreview_"+total+index).hide();
        $("#type_lable_"+total+index).show();
    }
    //根据商家分类的选中状态  修改默认状态的显示状态- 获取商家分类的ID  
    function getBusTypeId(obj,index_num){
        //console.log($(obj).attr('checked'))
        if($(obj).attr('checked')=='checked'){
            $("#prodTypeId"+index_num).val($(obj).val());
            $("#defaultFlag"+index_num).prop("disabled","");
            $("#busType_del_Flag_"+index_num).val("0");//给选中的分类赋值为正常状态
        }else{
            $("#prodTypeId"+index_num).val('');
            $("#defaultFlag"+index_num).prop("disabled","disabled");
            $("#busType_del_Flag_"+index_num).val("1");//给选中的分类赋值为删除状态
        }
    }
    //给设置默认赋值
    function getDefaultFlagId(obj,index_num,total){
        $("#recomBusTypeModule"+total).find("input:radio").each(function(){
           $(this).next().next().val("0");
           //console.log( $(this).val());
        })
        $("#defaultFlag_"+index_num).val("1");
    }
    
    //商家推荐2 移除
    function removeRowBusType(obj,index_num) {
        var total = $("#recomThree").find(".controls").size();//
        if(total==1){
            top.$.jBox.tip('请至少保留一组商家推荐2数据', 'error');
            return;
        }
        if($("#recomBusTypeId"+index_num).val()!=""&&$("#recomBusTypeId"+index_num).val()!=null){
            $(obj).parent().addClass("hide");
            $("#recomBusTypeModule"+index_num).addClass("hide");
            $("#recomBusTypeModule"+index_num).removeClass("controls");
            $("#recomBusTypeFlag"+index_num).val("1");
        }else{
            $(obj).parent().remove();
            $("#recomBusTypeModule"+index_num).remove();
        }
        /* if(total>1){
           
        }else{
            $(obj).hide();
        } */
    }
    //绑定商家推荐2
    function initRecomBusType(){
        for (var i=0; i<busData.length-1; i++){
            addBusType();
        }
        console.log(busData);
        for (var i=0; i<busData.length; i++){
           $("#recomTypeBusId"+i).val(busData[i].recomBusinessId).trigger("change");
           $("#recomBusTypeId"+i).val(busData[i].id);
           //console.log(busData[i].id);
           console.log($("#recomBusTypeId"+i).val());
        }
    }
    //绑定商家分类数据
    function initBusType(){
        for (var i=0; i<busData.length; i++){
            var soreNum="",defaultFlag="",detailId="",picUrl="";
            if(busData[i].recomBusTypeDetailList!=undefined){
                for (var j=0; j<busData[i].recomBusTypeDetailList.length; j++){
                   soreNum+=busData[i].recomBusTypeDetailList[j].sortNum+",";
                   defaultFlag+=busData[i].recomBusTypeDetailList[j].defaultFlag+",";
                   detailId+=busData[i].recomBusTypeDetailList[j].id+","
                   picUrl+=busData[i].recomBusTypeDetailList[j].picUrl+","
                }
                var sortNumArr=soreNum.split(",")
                var defaultFlagArr=defaultFlag.split(",");
                var idArr=detailId.split(",");
                var picUrlArr=picUrl.split(",");
                //console.log(picUrlArr);
                for(var h=0;h<sortNumArr.length;h++){
                   var num=sortNumArr[h];
                   $("#cbType"+i+num).prop("checked","checked");
                   $("#defaultFlag"+i+num).prop("disabled","");
                   $("#prodTypeId"+i+num).val($("#cbType"+i+num).val());
                   $("#buy_type_id_"+i+num).val(idArr[h]);
                   if(picUrlArr[h]!="undefined"){
                       $("#type_lable_"+i+num).hide();
                       $("#imgPreview_"+i+num).show();
                       $("#imgPreview_"+i+num).attr("src",picUrlArr[h]);//给推荐详情的图片赋值
                       getImgSize($("#lable_size_"+i+num),picUrlArr[h]);
                   }
                   if(defaultFlagArr[h]==1){
                      $("#defaultFlag"+i+num).prop("checked","checked");
                      $("#defaultFlag_"+i+num).val("1");//给设置默认赋值
                   }
                }
            }
         }
    }
    //模块的change事件 
    function getSpecialModule(obj,type,count) {
        $("#businessCategoryDictId"+count).empty().append("<option value=''>商家分类</option>");
        $("#recomModuleId"+count).empty().append("<option value=''>模块名称</option>");
        if(type==0){
            $("#businessCategoryDictId"+count).empty();
            var categoryList= ${fns:toJson(getCategoryList)};
            var option = "";
            for (var i=0; i<categoryList.length; i++){
                option += "<option value='"+categoryList[i].businessCategoryDictId+"'>" + categoryList[i].moduleName + "</option>";
            }
            $("#businessCategoryDictId"+count).append(option);
            $("#businessCategoryDictId"+count).val($("#HidBusinessCategoryDictId"+count).val()).trigger("change");
        }else{
            var moduleList= ${fns:toJson(getModuleList)};
            var option = "";
            for (var i=0; i<moduleList.length; i++){
                option += "<option value='"+moduleList[i].id+"'>" + moduleList[i].moduleName + "</option>";
            }
            $("#recomModuleId"+count).append(option);
            $("#recomModuleId"+count).val($("#HidRecomModuleId"+count).val());
        }
    };
    $(function(){
        //初始化推荐商家1的模块
        addRowModule();
        //初始化推荐商家1的数据
        initRecomModule();
        
        //初始话专题推荐1的模块
        addSpecial();
        //初始话专题推荐1的数据
        initRecomSpecial();
        //初始化商家推荐2的模块
        addBusType();
        //初始化商家推荐2的数据
        initRecomBusType();
    })
    
</script>
</head>

<body>
    <ul class="nav nav-tabs">
        <li>
            <span>
                <a href="${ctx}/module/villageLine/recommendList">模块管理 </a>> <a href="${ctx}/module/villageLine/recommendList">推荐管理 > </a><a>设置管理</a>
            </span>
        </li>
    </ul>
    <ul class="nav nav-tabs">
        <li>
            <a href="${ctx}/module/villageLine/mainRecomFrom?id=${villageLine.id}">首页推荐</a>
        </li>
        <li>
            <a href="${ctx}/module/villageLine/communityRecomFrom?id=${villageLine.id}">社区推荐</a>
        </li>
        <li class="active">
            <a href="${ctx}/module/villageLine/lifeRecomFrom?id=${villageLine.id}">生活推荐 </a>
        </li>
    </ul>
    <form:form id="inputForm" style="margin: 0 50px;" modelAttribute="villageLine" action="${ctx}/module/villageLine/updateLifeRecomModule" method="post" class="form-horizontal" enctype="multipart/form-data">
        <form:hidden path="id" />
        <input id="lifeRecomModule" type="hidden" name="lifeRecomModule" value="${villageLine.lifeRecomModule}">
        <input id="lifeRecomModuleSort" type="hidden" name="lifeRecomModuleSort" value="${villageLine.lifeRecomModuleSort}">
        <sys:message content="${message}" />
        <div class="control-group">
            <label class="control-label">楼盘名称</label>
            <div class="controls">
                <label>${villageLine.villageInfo.villageName }</label>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">推荐模块1<br>可推荐4个
            </label>
            <div class="controls" style="border: 1px solid #ccc; padding: 20px" id="addLifeRecomModule"></div>
            <div class="controls" style="border: 1px solid #ccc; padding: 20px; margin-top: 20px;">
                <c:choose>
                    <c:when test="${lifeModuleList.size()>0}">
                        <form:checkboxes items="${lifeModuleList}" path="lifeRecomModuleIds" itemLabel="moduleName" itemValue="id" class="required" />
                    </c:when>
                    <c:otherwise>
                        <font color="red">没有可选择的模块，请在"模块管理"-"设置管理" 进行楼盘模块设置 </font>
                        <form:input path="lifeRecomModuleIds" style="width: 0px; height: 0px; border: 0px;opacity: 0;" class="required" />
                    </c:otherwise>
                </c:choose>

                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>
        <!-- 商家推荐1 -->
        <div class="control-group" style="margin-right: 60px" id="recomOne">
            <label class="control-label">商家推荐1</label>
            <div class="controls">
                <label>（建议推荐2个或4个商家） </label>
            </div>
        </div>
        <!-- 专题推荐 -->
        <div class="control-group" style="margin-right: 60px" id="recomTwo">
            <label class="control-label">专题推荐</label>
        </div>
        <!-- 商家推荐2 -->
        <div class="control-group" style="margin-right: 60px" id="recomThree">
            <label class="control-label">商家推荐2</label>
        </div>
        <div class="form-actions">
            <shiro:hasPermission name="module:villageLine:batchSetModule">
                <input id="btnSubmit" class="btn btn-success" type="submit" value="保 存" />&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="module:villageLine:batchSetModule">
                <input onclick="showPage()" id="" class="btn btn-success" type="button" value="预览" />&nbsp;
            </shiro:hasPermission>
            <input id="btnCancel" class="btn btn-success" type="button" value="返 回" onclick="history.go(-1)" />
        </div>
        <script type="text/javascript">
        function showPage(){
            var formdata=new FormData($("#inputForm")[0]);
            console.log(formdata);
            $.ajax({
                type : "POST",
                url : ctx + "/module/villageLine/mainPreview",
                data : formdata,
                dataType : "JSON",
                processData: false,
                contentType: false,
                success : function(data) {
                    console.log(data);
                    layer.open({
                        type: 2,
                        title:'首页推荐预览',
                        area: ['414px', '736px'],
                        scrollbar: true,
                        maxmin: true,
                        content: '${contextPath}/moblie/page/home/index.html',
                        zIndex: layer.zIndex, //重点1
                        success: function(layero){
                            
                            layer.setTop(layero); //重点2
                        },
                    });  
                }
            })
            
        }
        </script>
    </form:form>
</body>



</html>