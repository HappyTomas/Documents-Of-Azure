$(function() {
    var delParent;
    var defaults = {
        fileType : [ "jpg", "png", "bmp", "jpeg" ], // 上传文件的类型
        fileSize : 1024 * 1024 * 1
    // 上传文件的大小 1M
    };
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    if (userAgent.indexOf("Chrome") > -1){
        $(".file").addClass("fileForChrome");
        $(".file").removeClass("file");
    }
    /* 点击图片的文本框 */
    $(".fileForChrome").change(function() {
        var idFile = $(this).attr("id");
        var file = document.getElementById(idFile);
        var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
        var fileList = file.files; // 获取的图片文件
        var input = $(this).parent();// 文本框的父亲元素
        // 遍历得到的图片文件
        var numUp = imgContainer.find(".up-section").length;
        var totalNum = numUp + fileList.length; // 总的数量
        if (fileList.length = 0) {
            alertx("未选择任何文件；");
            return;
        } else if (numUp < 1) {
            fileList = validateUp(fileList);
            for (var i = 0; i < fileList.length; i++) {
                var $section = $("<section class='up-section fl loading'>");
                imgContainer.prepend($section);
                var $span = $("<span class='up-span'>");
                $span.appendTo($section);
                var $img0 = $("<img class='close-upimg'>").on("click", function(event) {
                    event.preventDefault();
                    event.stopPropagation();
                    $(".works-mask").show();
                    delParent = $(this).parent();
                });
                $img0.attr("src", "/ehomemanagement/static/images/a7.png").appendTo($section);
                var $img = $("<img class='up-img up-opcity' + name='imgName" + i + "'>");
                $img.attr("src", "");
                $img.appendTo($section);
                var reader = new FileReader();
                reader.readAsDataURL(fileList[i]);
                console.log();
                reader.onload = function(e) {
                    document.getElementsByName("imgName0")[0].src = this.result;
                }
            }
        }
        setTimeout(function() {
            $(".up-section").removeClass("loading");
            $(".up-img").removeClass("up-opcity");
        }, 450);
        numUp = imgContainer.find(".up-section").length;
        if (numUp >= 1) {
            $(this).parent().hide();
        }
    });
    /* 点击图片的文本框 */
    $(".file").change(function() {
        var idFile = $(this).attr("id");
        var file = document.getElementById(idFile);
        var imgContainer = $(this).parents(".z_photo"); // 存放图片的父亲元素
        var fileList = file.files; // 获取的图片文件
        var input = $(this).parent();// 文本框的父亲元素
        // 遍历得到的图片文件
        var numUp = imgContainer.find(".up-section").length;
        var totalNum = numUp + fileList.length; // 总的数量
        if (fileList.length = 0) {
            alertx("未选择任何文件；");
            return;
        } else if (numUp < 1) {
            fileList = validateUp(fileList);
            for (var i = 0; i < fileList.length; i++) {
                var $section = $("<section class='up-section fl loading'>");
                imgContainer.prepend($section);
                var $span = $("<span class='up-span'>");
                $span.appendTo($section);
                var $img0 = $("<img class='close-upimg'>").on("click", function(event) {
                    event.preventDefault();
                    event.stopPropagation();
                    $(".works-mask").show();
                    delParent = $(this).parent();
                });
                $img0.attr("src", "/ehomemanagement/static/images/a7.png").appendTo($section);
                var $img = $("<img class='up-img up-opcity' + name='imgName" + i + "'>");
                $img.attr("src", "");
                $img.appendTo($section);
                var reader = new FileReader();
                reader.readAsDataURL(fileList[i]);
                console.log();
                reader.onload = function(e) {
                    document.getElementsByName("imgName0")[0].src = this.result;
                }
            }
        }
        setTimeout(function() {
            $(".up-section").removeClass("loading");
            $(".up-img").removeClass("up-opcity");
        }, 450);
        numUp = imgContainer.find(".up-section").length;
        if (numUp >= 1) {
            $(this).parent().hide();
        }
    });

    $(".z_photo").delegate(".close-upimg", "click", function() {
        $(".works-mask").show();
        delParent = $(this).parent();
    });

    $(".wsdel-ok").click(function() {
        $(".works-mask").hide();
        var numUp = delParent.siblings().length;
        if (numUp < 2) {
            delParent.parent().find(".z_file").css('display', 'inline-block').css('vertical-align', 'top');
            delParent.parent().find(".z_file").show();
        }
        delParent.remove();
        var file = document.getElementById(idFile);
        file.value = "";
    });

    $(".wsdel-no").click(function() {
        $(".works-mask").hide();
    });

    function validateUp(files) {
        var arrFiles = [];// 替换的文件数组
        // 在这里需要判断当前所有文件中
        for (var i = 0, file; file = files[i]; i++) {
            // 获取文件上传的后缀名
            var newStr = file.name.split("").reverse().join("");
            if (newStr.split(".")[0] != null) {
                var type = newStr.split(".")[0].split("").reverse().join("");
                console.log(type + "===type===");
                if (jQuery.inArray(type, defaults.fileType) > -1) {

                    var img = new Image;
                    var imgUrl = window.URL.createObjectURL(files[i]);
                    img.src = imgUrl;
                    // 类型符合，可以上传
                    if (file.size >= defaults.fileSize) {
                        alertx('您这个"' + file.name + '"图片大小超过1M。');
                    } else if (img.height > 563) {
                        alertx('您这个"' + file.name + '"图片高度超过563像素。');
                    } else if (img.width > 750) {
                        alertx('您这个"' + file.name + '"图片宽度超过750像素。');
                    } else {
                        arrFiles.push(file);
                    }

                } else {
                    alertx('您这个"' + file.name + '"上传类型不符合');
                }
            } else {
                alertx('您这个"' + file.name + '"没有类型, 无法识别');
            }
        }
        return arrFiles;
    }

})
