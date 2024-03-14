package com.common.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.*;
import java.util.Map;

public class WordUtil {

    /**
     * @param dataMap 导出数据
     * @param templateName 模板名称
     * @param path 导出word的路径以及文件名称
     * 通过模板导出word格式文件
     * */
    public static void exportWord(Map<String,Object> dataMap,String templateName,String path) throws IOException, TemplateException {
        //Configuration 用于读取ftl文件
        Configuration configuration = new Configuration(new Version("2.3.0"));
        configuration.setDefaultEncoding("utf-8");
        //指定路径（根据某个类的相对路径指定）
        configuration.setClassForTemplateLoading(WordUtil.class,"/template");
        //输出文档路径及名称
        File outFile = new File(path);
        //以utf-8的编码读取ftl文件
        Template template = configuration.getTemplate(templateName, "utf-8");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
        template.process(dataMap, out);
        out.close();
    }
}
