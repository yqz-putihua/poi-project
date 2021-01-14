package com.yqz.zzh.qrcode.controller;

import com.lowagie.text.Image;
import com.yqz.zzh.qrcode.test.ZXingCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("test")
public class TestController {

    @GetMapping("test")
    public void test(){
        HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        try {
            Image qrCode = ZXingCode.getQRCode("傻呗呗", "扫一扫有惊喜");
            List<Image> imageList = new ArrayList<>();
            imageList.add(qrCode);
            ZXingCode.createDocContext(imageList, httpServletResponse,"C:\\Users\\zhuzhenhua\\Desktop\\test.docx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
