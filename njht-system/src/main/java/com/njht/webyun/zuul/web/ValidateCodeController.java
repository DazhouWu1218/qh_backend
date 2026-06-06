package com.njht.webyun.zuul.web;

import com.alibaba.fastjson.JSONObject;
import com.njht.webyun.zuul.access.config.RandomValidateCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author David
 * @date 2020/12/8
 * @description：生成验证码
 */
@Controller
@Api(tags = "验证码",value= "ValidateCode")
public class ValidateCodeController {

    @ApiOperation("获取登陆验证码")
    @GetMapping(value = "/system/random/image")
    public void newValidateCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setContentType("image/jpeg");
//        // 禁止图像缓存
//        response.setHeader("Pragma", "no-cache");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setDateHeader("Expires", 0);

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        HttpSession session = request.getSession();
        RandomValidateCode randomValidateCode = new RandomValidateCode(90, 28, 4, 40);

        JSONObject object = new JSONObject();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response.addHeader("code", randomValidateCode.getCode().toLowerCase());
        try {
            ImageIO.write(randomValidateCode.getBuffImg(), "png", outputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            String base64 = encoder.encodeBuffer(outputStream.toByteArray()).trim();
            base64 = base64.replaceAll("\n", "").replaceAll("\r", "");
            object.put("code", "data:image/jpg;base64," + base64);

            if (session.getAttribute(RandomValidateCode.RANDOMVALIDATECODE) != null) {
                session.removeAttribute(RandomValidateCode.RANDOMVALIDATECODE);
            }

            session.setAttribute(RandomValidateCode.RANDOMVALIDATECODE, randomValidateCode.getCode());

            response.getWriter().write(object.toString());
        } catch (IOException e) {
            response.getWriter().write("");
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
                response.getWriter().close();
            }
        }

    }
}
