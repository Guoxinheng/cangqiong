package com.controller.admin;

import com.result.Result;
import com.untils.AliOssUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    @ApiOperation("上传图片")
    public Result<String> upload(MultipartFile file)
    {
        log.info("文件上传:{}",file);

        try {
            String originalFilename = file.getOriginalFilename();//原始文件名
            //截取原始文件名的后缀,png,jpg 然后在和随机生成的uuid拼接起来
            String newname = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
            //将文件转换为字节码.把文件名字也传给aliOssUtil.upload方法
            String filepath = aliOssUtil.upload(file.getBytes(), newname);
            return Result.success(filepath);
        } catch (IOException e) {
            log.error("文件上传失败:{}",e);
        }
        return Result.error("文件上传失败");
    }
}
