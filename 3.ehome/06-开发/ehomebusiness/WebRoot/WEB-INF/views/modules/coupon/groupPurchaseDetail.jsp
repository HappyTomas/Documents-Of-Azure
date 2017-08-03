<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>验券管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	    //页面初始加载
		$(document).ready(function() {
			var id = '${id}';
			//修改的场合
			if(id != null && id !=''){
				//绑定团购时间详情
				bindList();
			}
		});
	    
		//团购时间详情绑定
		function bindList(){
			$.ajax({
				type : 'POST',
				url : '${ctx}/coupon/groupPurchase/bindList',
				data : {
					groupPurchaseId:'${id}'
	    		 },
				dataType : 'json',
				success : function(data) {
					for(var  i=0;i<data.length-1;i++){
						var section = null;
						if(i==0){
							section = "二";
						}else if(i==1){
							section = "三";
						}else if(i==2){
							section = "四";
						}else if(i==3){
							section = "五";
						}else if(i==4){
							section = "六";
						}else if(i==5){
							section = "七";
						}else if(i==6){
							section = "八";
						}else if(i==7){
							section = "九";
						}else if(i==8){
							section = "十";
						}else if(i==9){
							section = "十一";
						}else if(i==10){
							section = "十二";
						}else{
							section = "N";
						}
						
						addRow(i+1,section);
					}
					for(var  i=0;i<data.length;i++){					
						var startTimes=data[i].startTime;
						var endTimes=data[i].endTime;
						var stockNums=data[i].stockNum;
						var saleNums=data[i].saleNum;

						$("#startTime"+i).text(startTimes.substr(0,13));
						$("#endTime"+i).text(endTimes.substr(0,13));
						$("#stockNum"+i).text(stockNums);
						
						if(saleNums ==null || saleNums=='' || saleNums=='undefined'){
							$("#saleNum"+i).text("0");
						}else{
							$("#saleNum"+i).text(saleNums);
						}						
					}
				}
			})
		}
		
		//团购开始时间-增加一行
		function addRow(i,section){
			var domRow='<div class="control-group" style="border-bottom:none">';
			
			var item='<label class="control-label"  style="font-weight:bold;font-size:14px;">'+section+'阶段：</label>'+
			'<div class="controls"><label style="margin-left:370px">团购库存：</label>'+'<label id="stockNum'+i+'" ></label>&nbsp;件(出售&nbsp;<label id="saleNum'+i+
			'"></label>件)</div></div><div class="control-group">	<label class="control-label">开始时间：</label><div class="controls"><label id="startTime'+i+
			'"></label>&nbsp;点<label style="margin-left:265px">结束时间：</label><label id="endTime'+i+'"></label>&nbsp;点</div>'

			domRow+=item;
			domRow+='</div>';
			$("#timeList").append($(domRow))
		}
	</script>
</head>
<body>
	
	<ul class="nav nav-tabs">
        <li style="height:30px"><span><a href="${ctx}/coupon/groupPurchase/">优惠/验券管理 </a>
        > 团购详情
        </span>
        </li>
    </ul>

	<form:form id="inputForm" modelAttribute="groupPurchase" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table style="width:100%">
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">团购商家：</label>
						<div class="controls">
							${groupPurchase.businessName}
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">团购名称：</label>
						<div class="controls">
							${groupPurchase.groupPurcName}
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">团购图片：</label>
						<div class="controls">
							<img id="preview" src="${groupPurchase.groupPurcPic}" style="width:400px;height:300px;"/>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">	
					    <label class="control-label">市场价：</label>					
						<div class="controls">						
							<label style="width:180px">${groupPurchase.marketMoney}&nbsp;元</label>
							<label style="margin-left:200px">团购价：</label>
							${groupPurchase.groupPurcMoney}&nbsp;元
						</div>
					</div>					
				</td>
			</tr>
			<tr>
				<td>
					<div id="timeList">
						<div class="control-group" style="border-bottom:none">	
						    <label class="control-label"  style="font-weight:bold;font-size:14px;">一阶段：</label>					
							<div class="controls">
								<label style="margin-left:370px">团购库存：</label>
								<label id="stockNum0" ></label>&nbsp;件(出售&nbsp;<label id="saleNum0"></label>件)
							</div>
						</div>
						<div class="control-group">	
						    <label class="control-label">开始时间：</label>					
							<div class="controls">
								<label id="startTime0"></label>&nbsp;点
								<label style="margin-left:265px">结束时间：</label>
								<label id="endTime0"></label>&nbsp;点
							</div>
						</div>	
					</div>
				</td>
			</tr>
			<tr >
				<td>
					<div class="control-group">
						<label class="control-label">限购数量：</label>
						<div class="controls">
							${groupPurchase.restrictionNumber}&nbsp;件
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">商家支持：</label>
						<div class="controls">						
							<c:forEach items="${supportTypeList}" var="supportType">
								${fns:getDictLabel(supportType, 'supportType', '')}&nbsp;&nbsp;
							</c:forEach>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">团购详情：</label><br>
						<div class="controls">
							${groupPurchase.groupPurcDetail}
							
						</div>
					</div>
				</td>
			</tr>		
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">团购券有效期：</label>
						<div class="controls">
							<fmt:formatDate value="${groupPurchase.validityStartTime}" pattern="yyyy-MM-dd HH"/>&nbsp;点&nbsp;&nbsp;至&nbsp;&nbsp;
							<fmt:formatDate value="${groupPurchase.validityEndTime}" pattern="yyyy-MM-dd HH"/>&nbsp;点
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">使用时间：</label>
						<div class="controls">
							${groupPurchase.useTime}
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">使用规则：</label><br>
						<div class="controls">
							${groupPurchase.useRule}
						</div>
					</div>
				</td>
			</tr>
		</table>
		<div class="form-actions">
            <input id="btnCancel" class="btn btn-success" style="margin-left:550px" type="button" value="返 回" onclick="history.go(-1)" />
        </div>
	</form:form>
	<script src="${ctxStatic}/common/singlefileUpload.js"></script>

</body>
</html>