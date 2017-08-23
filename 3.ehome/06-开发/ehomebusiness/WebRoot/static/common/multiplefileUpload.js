var tempInterval;
var img_index_arr = [0,1,2,3,4];

Array.prototype.remove = function(val) {
	var index = this.indexOf(parseInt(val));
	if (index > -1) {
	    this.splice(index, 1);
	}
};
$(function() {
    var delParent;
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    if (userAgent.indexOf("Chrome") > -1){
        $(".file").addClass("fileForChrome");
        $(".file").removeClass("file");
    }
    var defaults = {
//        fileType : [ "jpg", "png", "bmp", "jpeg" ], // 上传文件的类型
        fileType : [ "jpg", "png"], // 上传文件的类型
        fileSize : 1024 * 1024 * 1
    // 上传文件的大小 1M
    };
    $(".picindex").each(function() {
    	img_index_arr.remove($(this).val());
    });
    
    /**
     * 判定选中的图片的合法性并添加到页面
     */ 
    function addPic(fileList, tempInterval, arrImgs, numUp, imgContainer){
        var flag = true;
        for(var i = 0 ; i < arrImgs.length ; i++){
            if (arrImgs[i].height <= 0 || arrImgs[i].width <= 0) {
                flag = false;
                break;
            }
        }
        if (flag) {
            if (fileList.length = 0) {
                alertx("未选择任何文件；");
                return;
            } else if (numUp < 5) {
//         停止定时任务
        clearInterval(tempInterval);
                
                fileList = validateUpOnlyOne(fileList, arrImgs);
                for (var i = 0; i < fileList.length; i++) {
                    var $section = $("<section class='up-section fl loading'>");
                    $(".z_file").before($section);
                    var $span = $("<span class='up-span'>");
                    $span.appendTo($section);
                    var $img0 = $("<img class='close-upimg'>").on(
                            "click", function(event) {
                                event.preventDefault();
                                event.stopPropagation();
                                $(".works-mask").show();
                                delParent = $(this).parent();
                            });
                    $img0.attr("src",
                    "/ehomebusiness/static/images/a7.png")
                    .appendTo($section);
                    var img_index = img_index_arr[0];
                    var $img = $("<img class='up-img up-opcity' name='imgName"
                            + img_index + "'>");
                    $img.attr("src", "");
                    $img.appendTo($section);
                    var $textarea = $("<textarea id='imgBase64"
                            + img_index
                            + "'name='picList["
                            + img_index
                            + "].imgBase64' rows=30 cols=300 class='hide'></textarea>");
                    $textarea.appendTo($section);
                    var $input3 = $("<input name='picList["
                            + img_index + "].type' value='"
                            + fileList[i].type
                            + "' type='hidden'/>");
                    $input3.appendTo($section);
                    var $input4 = $("<input class='picindex' name='picList["
                            + img_index + "].index' value='"
                            + img_index
                            + "' type='hidden'/>");
                    $input4.appendTo($section);
                    var reader = new FileReader();
                    reader.readAsDataURL(fileList[i]);
                    reader.onload = function(e) {
                        if (document
                                .getElementsByName("picList[0].imgBase64").length != 0
                                && document
                                .getElementsByName("picList[0].imgBase64")[0].innerHTML == "") {
                            document
                            .getElementsByName("picList[0].imgBase64")[0].innerHTML = this.result;
                            document.getElementsByName("imgName0")[0].src = this.result;
                        } else if (document
                                .getElementsByName("picList[1].imgBase64").length != 0
                                && document
                                .getElementsByName("picList[1].imgBase64")[0].innerHTML == "") {
                            document
                            .getElementsByName("picList[1].imgBase64")[0].innerHTML = this.result;
                            document.getElementsByName("imgName1")[0].src = this.result;
                        } else if (document
                                .getElementsByName("picList[2].imgBase64").length != 0
                                && document
                                .getElementsByName("picList[2].imgBase64")[0].innerHTML == "") {
                            document
                            .getElementsByName("picList[2].imgBase64")[0].innerHTML = this.result;
                            document.getElementsByName("imgName2")[0].src = this.result;
                        } else if (document
                                .getElementsByName("picList[3].imgBase64").length != 0
                                && document
                                .getElementsByName("picList[3].imgBase64")[0].innerHTML == "") {
                            document
                            .getElementsByName("picList[3].imgBase64")[0].innerHTML = this.result;
                            document.getElementsByName("imgName3")[0].src = this.result;
                        } else if (document
                                .getElementsByName("picList[4].imgBase64").length != 0
                                && document
                                .getElementsByName("picList[4].imgBase64")[0].innerHTML == "") {
                            document
                            .getElementsByName("picList[4].imgBase64")[0].innerHTML = this.result;
                            document.getElementsByName("imgName4")[0].src = this.result;
                        }
                    }
                    img_index_arr.remove(img_index);
                }
            }
            setTimeout(function() {
                $(".up-section").removeClass("loading");
                $(".up-img").removeClass("up-opcity");
            }, 450);
            numUp = imgContainer.find(".up-section").length;
            if (numUp >= 5) {
                $(".fileForChrome").parent().hide();
                $(".file").parent().hide();
            }
        }
    }
    
    /* 点击图片的文本框 */
    $(".file")
            .change(
                    function() {
                        var idFile = $(this).attr("id");
                        var file = document.getElementById(idFile);
                        var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                        var fileList = file.files; // 获取的图片文件
                        var input = $(this).parent();// 文本框的父亲元素
                        // 遍历得到的图片文件
                        var numUp = imgContainer.find(".up-section").length;
                        var totalNum = numUp + fileList.length; // 总的数量
                        if (fileList.length > 5 || totalNum > 5) {
                            alertx("上传图片数目不可以超过5个，请重新选择"); // 一次选择上传超过5个
                            // 或者是已经上传和这次上传的到的总数也不可以超过5个
                            return;
                        }
                        
                        // 获取所有图片
                        var arrImgs = [];// 所有图片的集合
                        for (var i = 0, file; file = fileList[i]; i++) {
                            // 获取文件上传的后缀名
                            var newStr = file.name.split("").reverse().join("");
                            if (newStr.split(".")[0] != null) {
                                var type = newStr.split(".")[0].split("").reverse().join("");
                                if (jQuery.inArray(type, defaults.fileType) > -1) {

                                    var img = new Image;
                                    var imgUrl = window.URL.createObjectURL(fileList[i]);
                                    img.src = imgUrl;
                                    arrImgs.push(img);
                                } else {
                                    alertx('您这个"' + file.name + '"上传类型不符合');
                                }
                            } else {
                                alertx('您这个"' + file.name + '"没有类型, 无法识别');
                            }
                        }
                        
                        tempInterval = self.setInterval(function(){addPic(fileList, tempInterval, arrImgs, numUp, imgContainer)},200);
                    });
    /* 点击图片的文本框 */
    $(".fileForChrome")
            .change(
                    function() {
                        var idFile = $(this).attr("id");
                        var file = document.getElementById(idFile);
                        var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                        var fileList = file.files; // 获取的图片文件
                        var input = $(this).parent();// 文本框的父亲元素
                        // 遍历得到的图片文件
                        var numUp = imgContainer.find(".up-section").length;
                        var totalNum = numUp + fileList.length; // 总的数量
                        if (fileList.length > 5 || totalNum > 5) {
                            alertx("上传图片数目不可以超过5个，请重新选择"); // 一次选择上传超过5个
                            // 或者是已经上传和这次上传的到的总数也不可以超过5个
                            return;
                        }
                        
                        // 获取所有图片
                        var arrImgs = [];// 所有图片的集合
                        for (var i = 0, file; file = fileList[i]; i++) {
                            // 获取文件上传的后缀名
                            var newStr = file.name.split("").reverse().join("");
                            if (newStr.split(".")[0] != null) {
                                var type = newStr.split(".")[0].split("").reverse().join("");
                                if (jQuery.inArray(type, defaults.fileType) > -1) {

                                    var img = new Image;
                                    var imgUrl = window.URL.createObjectURL(fileList[i]);
                                    img.src = imgUrl;
                                    arrImgs.push(img);
                                } else {
                                    alertx('您这个"' + file.name + '"上传类型不符合');
                                }
                            } else {
                                alertx('您这个"' + file.name + '"没有类型, 无法识别');
                            }
                        }
                        
                        tempInterval = self.setInterval(function(){addPic(fileList, tempInterval, arrImgs, numUp, imgContainer)},200);
                    });
    $(".z_photo").delegate(".close-upimg", "click", function() {
        $(".works-mask").show();
        delParent = $(this).parent();
    });

    $(".wsdel-ok")
            .click(
                    function() {
                        $(".works-mask").hide();
                        var numUp = delParent.siblings().length;
                        if (numUp < 6) {
                            delParent.parent().find(".z_file").css('display',
                                    'inline-block')
                                    .css('vertical-align', 'top');
                            delParent.parent().find(".z_file").show();
                        }
                        if (typeof (delParent.find("textarea")[0]) == "undefined"
                                || delParent.find("textarea")[0].innerHTML == "") {
                            // 第一个是图片叉号
                            var path = delParent.find("img")[1].src;
                            if (path.indexOf("/") > 0)// 如果包含有"/"号
                           // 从最后一个"/"号+1的位置开始截取字符串
                            {
                                filename = delParent.find(".filename").val();
                                document.getElementById("delImageName").value = document
                                        .getElementById("delImageName").value
                                        + filename + ",";
                            }
                        }
                        var del_index  = delParent.find(".picindex").val();
                        var temp_arr = [];
                        temp_arr.push(parseInt(del_index));
                        img_index_arr = temp_arr.concat(img_index_arr);
                        img_index_arr = img_index_arr.sort();
                        delParent.remove();
                    });

    $(".wsdel-no").click(function() {
        $(".works-mask").hide();
    });

    function validateUpOnlyOne(files, arrImgs) {
        var arrFiles = [];// 替换的文件数组
        // 在这里需要判断当前所有文件中
        // 类型符合，可以上传
        for(var i = 0 ; i < arrImgs.length ; i++){
            // file个数应与arrImgs中元素数相等
            var file = files[i];
            if (file.size >= defaults.fileSize) {
                alertx('您这个"' + file.name + '"图片大小超过1M。');
                var file = $(".file");
                file.after(file.clone().val(""));
                file.remove();
                var fileForChrome = $(".fileForChrome");
                fileForChrome.after(fileForChrome.clone().val(""));
                fileForChrome.remove(); 
                /* 点击图片的文本框 */
                $(".file")
                        .change(
                                function() {
                                    var idFile = $(this).attr("id");
                                    var file = document.getElementById(idFile);
                                    var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                                    var fileList = file.files; // 获取的图片文件
                                    var input = $(this).parent();// 文本框的父亲元素
                                    // 遍历得到的图片文件
                                    var numUp = imgContainer.find(".up-section").length;
                                    var totalNum = numUp + fileList.length; // 总的数量
                                    if (fileList.length > 5 || totalNum > 5) {
                                        alertx("上传图片数目不可以超过5个，请重新选择"); // 一次选择上传超过5个
                                        // 或者是已经上传和这次上传的到的总数也不可以超过5个
                                        return;
                                    }
                                    
                                    // 获取所有图片
                                    var arrImgs = [];// 所有图片的集合
                                    for (var i = 0, file; file = fileList[i]; i++) {
                                        // 获取文件上传的后缀名
                                        var newStr = file.name.split("").reverse().join("");
                                        if (newStr.split(".")[0] != null) {
                                            var type = newStr.split(".")[0].split("").reverse().join("");
                                            if (jQuery.inArray(type, defaults.fileType) > -1) {

                                                var img = new Image;
                                                var imgUrl = window.URL.createObjectURL(fileList[i]);
                                                img.src = imgUrl;
                                                arrImgs.push(img);
                                            } else {
                                                alertx('您这个"' + file.name + '"上传类型不符合');
                                            }
                                        } else {
                                            alertx('您这个"' + file.name + '"没有类型, 无法识别');
                                        }
                                    }
                                    
                                    tempInterval = self.setInterval(function(){addPic(fileList, tempInterval, arrImgs, numUp, imgContainer)},200);
                                });
                /* 点击图片的文本框 */
                $(".fileForChrome")
                        .change(
                                function() {
                                    var idFile = $(this).attr("id");
                                    var file = document.getElementById(idFile);
                                    var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                                    var fileList = file.files; // 获取的图片文件
                                    var input = $(this).parent();// 文本框的父亲元素
                                    // 遍历得到的图片文件
                                    var numUp = imgContainer.find(".up-section").length;
                                    var totalNum = numUp + fileList.length; // 总的数量
                                    if (fileList.length > 5 || totalNum > 5) {
                                        alertx("上传图片数目不可以超过5个，请重新选择"); // 一次选择上传超过5个
                                        // 或者是已经上传和这次上传的到的总数也不可以超过5个
                                        return;
                                    }
                                    
                                    // 获取所有图片
                                    var arrImgs = [];// 所有图片的集合
                                    for (var i = 0, file; file = fileList[i]; i++) {
                                        // 获取文件上传的后缀名
                                        var newStr = file.name.split("").reverse().join("");
                                        if (newStr.split(".")[0] != null) {
                                            var type = newStr.split(".")[0].split("").reverse().join("");
                                            if (jQuery.inArray(type, defaults.fileType) > -1) {

                                                var img = new Image;
                                                var imgUrl = window.URL.createObjectURL(fileList[i]);
                                                img.src = imgUrl;
                                                arrImgs.push(img);
                                            } else {
                                                alertx('您这个"' + file.name + '"上传类型不符合');
                                            }
                                        } else {
                                            alertx('您这个"' + file.name + '"没有类型, 无法识别');
                                        }
                                    }
                                    
                                    tempInterval = self.setInterval(function(){addPic(fileList, tempInterval, arrImgs, numUp, imgContainer)},200);
                                });
            } else if (arrImgs[i].height > 400) {
                alertx('您这个"' + file.name + '"图片高度超过400像素。');
                var file = $(".file");
                file.after(file.clone().val(""));
                file.remove();
                var fileForChrome = $(".fileForChrome");
                fileForChrome.after(fileForChrome.clone().val(""));
                fileForChrome.remove(); 
                /* 点击图片的文本框 */
                $(".file")
                        .change(
                                function() {
                                    var idFile = $(this).attr("id");
                                    var file = document.getElementById(idFile);
                                    var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                                    var fileList = file.files; // 获取的图片文件
                                    var input = $(this).parent();// 文本框的父亲元素
                                    // 遍历得到的图片文件
                                    var numUp = imgContainer.find(".up-section").length;
                                    var totalNum = numUp + fileList.length; // 总的数量
                                    if (fileList.length > 5 || totalNum > 5) {
                                        alertx("上传图片数目不可以超过5个，请重新选择"); // 一次选择上传超过5个
                                        // 或者是已经上传和这次上传的到的总数也不可以超过5个
                                        return;
                                    }
                                    
                                    // 获取所有图片
                                    var arrImgs = [];// 所有图片的集合
                                    for (var i = 0, file; file = fileList[i]; i++) {
                                        // 获取文件上传的后缀名
                                        var newStr = file.name.split("").reverse().join("");
                                        if (newStr.split(".")[0] != null) {
                                            var type = newStr.split(".")[0].split("").reverse().join("");
                                            if (jQuery.inArray(type, defaults.fileType) > -1) {

                                                var img = new Image;
                                                var imgUrl = window.URL.createObjectURL(fileList[i]);
                                                img.src = imgUrl;
                                                arrImgs.push(img);
                                            } else {
                                                alertx('您这个"' + file.name + '"上传类型不符合');
                                            }
                                        } else {
                                            alertx('您这个"' + file.name + '"没有类型, 无法识别');
                                        }
                                    }
                                    
                                    tempInterval = self.setInterval(function(){addPic(fileList, tempInterval, arrImgs, numUp, imgContainer)},200);
                                });
                /* 点击图片的文本框 */
                $(".fileForChrome")
                        .change(
                                function() {
                                    var idFile = $(this).attr("id");
                                    var file = document.getElementById(idFile);
                                    var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                                    var fileList = file.files; // 获取的图片文件
                                    var input = $(this).parent();// 文本框的父亲元素
                                    // 遍历得到的图片文件
                                    var numUp = imgContainer.find(".up-section").length;
                                    var totalNum = numUp + fileList.length; // 总的数量
                                    if (fileList.length > 5 || totalNum > 5) {
                                        alertx("上传图片数目不可以超过5个，请重新选择"); // 一次选择上传超过5个
                                        // 或者是已经上传和这次上传的到的总数也不可以超过5个
                                        return;
                                    }
                                    
                                    // 获取所有图片
                                    var arrImgs = [];// 所有图片的集合
                                    for (var i = 0, file; file = fileList[i]; i++) {
                                        // 获取文件上传的后缀名
                                        var newStr = file.name.split("").reverse().join("");
                                        if (newStr.split(".")[0] != null) {
                                            var type = newStr.split(".")[0].split("").reverse().join("");
                                            if (jQuery.inArray(type, defaults.fileType) > -1) {

                                                var img = new Image;
                                                var imgUrl = window.URL.createObjectURL(fileList[i]);
                                                img.src = imgUrl;
                                                arrImgs.push(img);
                                            } else {
                                                alertx('您这个"' + file.name + '"上传类型不符合');
                                            }
                                        } else {
                                            alertx('您这个"' + file.name + '"没有类型, 无法识别');
                                        }
                                    }
                                    
                                    tempInterval = self.setInterval(function(){addPic(fileList, tempInterval, arrImgs, numUp, imgContainer)},200);
                                });
            } else if (arrImgs[i].width > 640) {
                alertx('您这个"' + file.name + '"图片宽度超过640像素。');
                var file = $(".file");
                file.after(file.clone().val(""));
                file.remove();
                var fileForChrome = $(".fileForChrome");
                fileForChrome.after(fileForChrome.clone().val(""));
                fileForChrome.remove(); 
                /* 点击图片的文本框 */
                $(".file")
                        .change(
                                function() {
                                    var idFile = $(this).attr("id");
                                    var file = document.getElementById(idFile);
                                    var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                                    var fileList = file.files; // 获取的图片文件
                                    var input = $(this).parent();// 文本框的父亲元素
                                    // 遍历得到的图片文件
                                    var numUp = imgContainer.find(".up-section").length;
                                    var totalNum = numUp + fileList.length; // 总的数量
                                    if (fileList.length > 5 || totalNum > 5) {
                                        alertx("上传图片数目不可以超过5个，请重新选择"); // 一次选择上传超过5个
                                        // 或者是已经上传和这次上传的到的总数也不可以超过5个
                                        return;
                                    }
                                    
                                    // 获取所有图片
                                    var arrImgs = [];// 所有图片的集合
                                    for (var i = 0, file; file = fileList[i]; i++) {
                                        // 获取文件上传的后缀名
                                        var newStr = file.name.split("").reverse().join("");
                                        if (newStr.split(".")[0] != null) {
                                            var type = newStr.split(".")[0].split("").reverse().join("");
                                            if (jQuery.inArray(type, defaults.fileType) > -1) {

                                                var img = new Image;
                                                var imgUrl = window.URL.createObjectURL(fileList[i]);
                                                img.src = imgUrl;
                                                arrImgs.push(img);
                                            } else {
                                                alertx('您这个"' + file.name + '"上传类型不符合');
                                            }
                                        } else {
                                            alertx('您这个"' + file.name + '"没有类型, 无法识别');
                                        }
                                    }
                                    
                                    tempInterval = self.setInterval(function(){addPic(fileList, tempInterval, arrImgs, numUp, imgContainer)},200);
                                });
                /* 点击图片的文本框 */
                $(".fileForChrome")
                        .change(
                                function() {
                                    var idFile = $(this).attr("id");
                                    var file = document.getElementById(idFile);
                                    var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
                                    var fileList = file.files; // 获取的图片文件
                                    var input = $(this).parent();// 文本框的父亲元素
                                    // 遍历得到的图片文件
                                    var numUp = imgContainer.find(".up-section").length;
                                    var totalNum = numUp + fileList.length; // 总的数量
                                    if (fileList.length > 5 || totalNum > 5) {
                                        alertx("上传图片数目不可以超过5个，请重新选择"); // 一次选择上传超过5个
                                        // 或者是已经上传和这次上传的到的总数也不可以超过5个
                                        return;
                                    }
                                    
                                    // 获取所有图片
                                    var arrImgs = [];// 所有图片的集合
                                    for (var i = 0, file; file = fileList[i]; i++) {
                                        // 获取文件上传的后缀名
                                        var newStr = file.name.split("").reverse().join("");
                                        if (newStr.split(".")[0] != null) {
                                            var type = newStr.split(".")[0].split("").reverse().join("");
                                            if (jQuery.inArray(type, defaults.fileType) > -1) {

                                                var img = new Image;
                                                var imgUrl = window.URL.createObjectURL(fileList[i]);
                                                img.src = imgUrl;
                                                arrImgs.push(img);
                                            } else {
                                                alertx('您这个"' + file.name + '"上传类型不符合');
                                            }
                                        } else {
                                            alertx('您这个"' + file.name + '"没有类型, 无法识别');
                                        }
                                    }
                                    
                                    tempInterval = self.setInterval(function(){addPic(fileList, tempInterval, arrImgs, numUp, imgContainer)},200);
                                });
            } else {
                arrFiles.push(file);
            }
        }
        return arrFiles;
    }
});

