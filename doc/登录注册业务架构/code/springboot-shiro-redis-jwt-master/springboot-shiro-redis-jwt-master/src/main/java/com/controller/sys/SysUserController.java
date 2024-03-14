package com.controller.sys;

import com.common.basic.annotate.Log;
import com.common.util.*;
import com.common.vo.ResultVo;
import com.entity.sys.SysUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.sys.SysUserService;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("/${api.url.prefix}/user")
public class SysUserController {

    private SysUserService userService;

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user() {
        return "/user";
    }

    /**
     * 用户列表
     */
    @PostMapping("/list")
    @RequiresPermissions("system:user:list")
    @Log(method = "用户列表")
    public ResultVo page(Integer pageSize, Integer pageNum, SysUser user){
        int offset = ConvertUtil.getOffset(pageNum, pageSize);
        PageHelper.startPage(pageNum,pageSize);
        List<SysUser> list = userService.list(offset,pageSize,user);
        PageInfo page = new PageInfo(list);
        return ResultUtil.success(page);
    }

    /**
     * 当前用户信息
     */


    /**
     * 测试导出word或pdf
     */
    @ResponseBody
    @GetMapping("/exportWord")
    public ResultVo exportWord( boolean pdf, HttpServletResponse response){
        try {

            List<Map<String,Object>> userList = new ArrayList<>();
            Map<String,Object> user1 = new HashMap<>();
            user1.put("name","admin");
            user1.put("phone","12345678910");
            user1.put("email","123@qq.com");
            user1.put("photo", FileUtil.getImageBase64String("E:\\document\\图片\\贴吧表情\\图片60x60\\image_emoticon16.png",50));
            userList.add(user1);

            Map<String,Object> user2 = new HashMap<>();
            user2.put("name","藏剑");
            user2.put("phone","13252101111");
            user2.put("email","cj@163.com");
            user2.put("photo",FileUtil.getImageBase64String("E:\\document\\图片\\贴吧表情\\图片60x60\\image_emoticon31.png",50));
            userList.add(user2);

            Map<String,Object> user3 = new HashMap<>();
            user3.put("name","霸刀");
            user3.put("phone","15244447777");
            user3.put("email","bd@111.com");
            user3.put("photo",FileUtil.getImageBase64String("E:\\document\\图片\\贴吧表情\\图片60x60\\image_emoticon6.png",50));
            userList.add(user3);

            //模板函数数据
            Map<String,Object> dataMap = new HashMap<>();
            List<String> picList1 = new ArrayList<>();
            picList1.add("E:\\document\\图片\\剑网三\\驰冥.jpg");
            picList1.add("E:\\document\\图片\\剑网三\\儒风.jpg");
            List<String> picList2 = new ArrayList<>();
            picList2.add("E:\\document\\图片\\剑网三\\风雷.jpg");
            picList2.add("E:\\document\\图片\\剑网三\\破虏.jpg");

            List<Map<String,String>> pictureList1 = new ArrayList<>();
            List<Map<String,String>> pictureList2 = new ArrayList<>();
            for (String s : picList1) {
                pictureList1.add(FileUtil.getImageBase64String(s,500));
            }
            for (String s : picList2) {
                pictureList2.add(FileUtil.getImageBase64String(s,500));
            }
            dataMap.put("pictureList1",pictureList1);
            dataMap.put("pictureList2",pictureList2);
            dataMap.put("userList",userList);
            dataMap.put("code","TEST_CODE001");

            //导出位置
            String fullPath = "C:\\Users\\Admin\\Downloads";
            File file = new File(fullPath);
            String wordName = file.getPath()+"/test.doc";
            //导出
            WordUtil.exportWord(dataMap,"word模板.ftl",wordName);
            //生成pdf
            if (pdf){
                String pdfName = file.getPath()+"/test.pdf";
                //将word转成pdf
                PdfUtil.wordConvertPdf(wordName,pdfName);
                //将word文件删除
//                cn.hutool.core.io.FileUtil.del(wordName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error("导出失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            return ResultUtil.error("导出失败");
        }
        return ResultUtil.success();
    }

}
