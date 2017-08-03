var vm = new Vue({
	el:"#app",
	data:{
		urlList:{
			list:"commoditylist.html?id=",
			detail:"commoditydetail.html?id=",
			index:"businessindex.html?id="
		},
		businessList:[]
	},
	mounted:function(){//页面加载之后自动调用，常用于页面渲染
		this.$nextTick(function(){//在2.0版本中，加mounted的$nextTick方法，才能使用vm
			this.cartView();
		})					
	},
	methods:{
		// 渲染页面
		cartView:function(){
			var _this = this;
			this.$http.get(interfaceUrl + "/live/getBusinessList",
				{ buildingID:userInfo.buildingID, sort: 1 }).then(function(response){
					if(response.data.code == 1000){
						_this.businessList = response.data.data;
					}					
			});
		},
		sort:function(){
			$(".sort_xiala").stop().slideToggle(400);
			$("#bg").stop().fadeToggle(200);
		},
		changeSort:function(method){
			$(".sort_xiala > ul > li.selected span").remove();
			$(".sort_xiala > ul > li.selected").removeClass("selected");
				
			$(event.target).append("<span class='green_dh'></span>");
			$(event.target).addClass("selected");
				
			this.sort();
				
			var _this = this;
			this.$http.get(interfaceUrl + "/live/getBusinessList", 
			{ buildingID:userInfo.buildingID, sort: method }).then(function(response){
				if(response.data.code == 1000){
					_this.businessList = response.data.data;	
				}						
			});
		}
    }
});