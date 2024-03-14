package com.common.util;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.MalformedURLException;


/**
 * Pdf处理工具类
 *
 * @author
 * @create 2017-12-18 21:25
 **/
public class PdfUtil {

    protected static Logger logger = LoggerFactory.getLogger(PdfUtil.class);


    /**
     * 获取破解码文件内容
     * @return
     */
    public static boolean getLicense() throws IOException {
        boolean result = false;
        InputStream is = null;
        try {
            is = PdfUtil.class.getClassLoader().getResourceAsStream("config/license.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License asposeLic = new License();
            asposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is)
                is.close();
        }
        return result;
    }

    /**
     * word转pdf文件
     * @param Address 原文件地址
     * @param pdfAddress 保存的pdf文件地址
     */
    public static void wordConvertPdf(String Address, String pdfAddress) throws IOException {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        FileOutputStream os = null;
        try {
            File file = new File(pdfAddress);  // 新建一个空白pdf文档
            os = new FileOutputStream(file);
            Document doc = new Document(Address); // Address是将要被转化的word文档
            doc.save(os, SaveFormat.PDF); // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != os)
                os.close();
        }
    }



    /**
     *
     * @param htmlString html字符串
     * @param path  生成pdf文件存储路径
     * @param chineseFontPath 中文字体存储路径
     */
    public static void htmlpdf(String htmlString, String path, String chineseFontPath)  {

        OutputStream os = null;
        try {
            os = new FileOutputStream(path);

            ITextRenderer renderer = new ITextRenderer();
//            renderer.setDocument(url);//url转换模式
            renderer.setDocumentFromString(htmlString); //html字符串转换模式
            // 解决中文不显示问题
            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(chineseFontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.layout();
            renderer.createPDF(os);

        } catch (MalformedURLException e) {
            logger.warn(e.toString(), e);
        } catch (FileNotFoundException e) {
            logger.warn(e.toString(), e);
        } catch (DocumentException e) {
            logger.warn(e.toString(), e);
        } catch (IOException e) {
            logger.warn(e.toString(), e);
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.warn(e.toString(), e);
                }
            }
        }
    }

    /**
     * pdf文件添加图片水印
     * @param InPdfFile 要添加水印的pdf路径
     * @param outPdfFile 添加水印完成的pdf输入路径
     * @param markImagePath  添加图片水印的路径
     * @param imgWidth 添加水印X坐标：文件的四个角，左下方的角坐标为（0,0）
     * @param imgHeight 添加水印的Y坐标
     * @throws Exception
     */
    public static void addPDFLogo(String InPdfFile, String outPdfFile, String markImagePath, int imgWidth, int imgHeight) throws IOException, DocumentException {

        System.out.println("========开始生成水印========>>>>>>"+InPdfFile);
        PdfReader reader = new PdfReader(InPdfFile, "PDF".getBytes());
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(new File(outPdfFile)));
        PdfContentByte under;
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.8f);// 透明度设置
        Image img = Image.getInstance(markImagePath);// 插入图片水印
        img.setAbsolutePosition(imgWidth, imgHeight); // 坐标
        img.setRotation(0);// 旋转 弧度
        img.setRotationDegrees(0);// 旋转 角度
        img.scaleAbsolute(595, 842);// 自定义大小
        // img.scalePercent(50);//依照比例缩放
        int pageSize = reader.getNumberOfPages();// 原pdf文件的总页数
        for (int i = 1; i <= pageSize; i++) {
            under = stamp.getUnderContent(i);// 水印在之前文本下
            // under = stamp.getOverContent(i);//水印在之前文本上
            under.setGState(gs1);// 图片水印 透明度
            under.addImage(img);// 图片水印
        }
        System.out.println("========完成水印生成========>>>>>>"+outPdfFile);

        stamp.close();// 关闭
        reader.close();

    }
}
