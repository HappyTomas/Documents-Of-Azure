var vm = new Vue({
	el:"#app",
	data:{
		criticism:{},
		commentContent:"",
		meanwhileIcon:"../../images/gx_01.png",
		meanwhileIcon2:"../../images/gx_02.png",
		ismeanwhile:0

	},
	mounted:function(){//页面加载之后自动调用，常用于页面渲染
		this.$nextTick(function(){//在2.0版本中，加mounted的$nextTick方法，才能使用vm
			this.cartView();
		});
	},
	computed:{
		textnum:function(){
			var _this = this;
			return 200-_this.commentContent.length
		}
	},
	methods:{
		// 渲染页面
		cartView:function(){
			var _this = this;
//			this.$http.get("/speak/toComment"{
//				speakId:123,
//			}).then(function(res){
//				_this.criticism = res.data;
//			});
		},
		meanwhile:function(){
			var _this = this;
			if(_this.ismeanwhile==0){
				_this.ismeanwhile=1;
			}else{
				_this.ismeanwhile=0;
			}
			
		},
		sendOut:function(){
			var _this = this;
			if($.trim(_this.commentContent)==""){
				layer.open({
					    content: '内容不能为空'
					    ,skin: 'msg'
					    ,time: 2 //2秒后自动关闭
				  });
			}
			
		}
	}
});