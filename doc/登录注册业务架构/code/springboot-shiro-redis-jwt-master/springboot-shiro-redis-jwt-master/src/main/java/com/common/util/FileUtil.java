package com.common.util;

import cn.hutool.core.codec.Base64Encoder;
import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FileUtil extends org.apache.tomcat.util.http.fileupload.FileUtils {
    /**
     * 将文件路径规则化，去掉其中多余的/和\，去掉可能造成文件信息泄漏的../
     */
    public static String normalizePath(String path) {
        path = path.replace('\\', '/');
        path = StringUtils.replaceEx(path, "../", "/");
        path = StringUtils.replaceEx(path, "./", "/");
        if (path.endsWith("..")) {
            path = path.substring(0, path.length() - 2);
        }
        path = path.replaceAll("/+", "/");
        return path;
    }

    public static File normalizeFile(File f) {
        String path = f.getAbsolutePath();
        path = normalizePath(path);
        return new File(path);
    }

    /**
     * 以全局编码将指定内容写入指定文件
     */
    public static boolean writeText(String fileName, String content) {
        fileName = normalizePath(fileName);
        return writeText(fileName, content, "utf-8");
    }

    /**
     * 以指定编码将指定内容写入指定文件
     */
    public static boolean writeText(String fileName, String content,
                                    String encoding) {
        fileName = normalizePath(fileName);
        return writeText(fileName, content, encoding, false);
    }

    /**
     * 以指定编码将指定内容写入指定文件，如果编码为utf-8且bomFlag为true,则在文件头部加入3字节的BOM
     */
    public static boolean writeText(String fileName, String content,
                                    String encoding, boolean bomFlag) {
        fileName = normalizePath(fileName);
        try {
            byte[] bs = content.getBytes(encoding);
            if (encoding.equalsIgnoreCase("utf-8") && bomFlag) {
                bs = ArrayUtils.addAll(StringUtils.BOM, bs);
            }
            writeByte(fileName, bs);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 以二进制方式读取文件
     */
    public static byte[] readByte(String fileName) {
        fileName = normalizePath(fileName);
        try {
            FileInputStream fis = new FileInputStream(fileName);
            byte[] r = new byte[fis.available()];
            fis.read(r);
            fis.close();
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以二进制方式读取文件
     */
    public static byte[] readByte(File f) {
        f = normalizeFile(f);
        try {

            FileInputStream fis = new FileInputStream(f);
            byte[] r = readByte(fis);
            fis.close();
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取指定流，并转换为二进制数组
     */
    public static byte[] readByte(InputStream is) {
        try {
            byte[] r = new byte[is.available()];
            is.read(r);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制数组写入指定文件
     */
    public static boolean writeByte(String fileName, byte[] b) {
        fileName = normalizePath(fileName);
        try {
            BufferedOutputStream fos = new BufferedOutputStream(
                    new FileOutputStream(fileName));
            fos.write(b);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将二进制数组写入指定文件
     */
    public static boolean writeByte(File f, byte[] b) {
        f = normalizeFile(f);
        try {
            BufferedOutputStream fos = new BufferedOutputStream(
                    new FileOutputStream(f));
            fos.write(b);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 以全局编码读取指定文件中的文本
     */
    public static String readText(File f) {
        f = normalizeFile(f);
        return readText(f, "utf-8");
    }

    /**
     * 以指定编码读取指定文件中的文本
     */
    public static String readText(File f, String encoding) {
        f = normalizeFile(f);
        try {
            InputStream is = new FileInputStream(f);
            String str = readText(is, encoding);
            is.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以指定编码读取流中的文本
     */
    public static String readText(InputStream is, String encoding) {
        try {
            byte[] bs = readByte(is);
            if (encoding.equalsIgnoreCase("utf-8")) {// 如果是UTF8则要判断有没有BOM
                if (StringUtils.hexEncode(ArrayUtils.subarray(bs, 0, 3)).equals(
                        "efbbbf")) {// BOM标志
                    bs = ArrayUtils.subarray(bs, 3, bs.length);
                }
            }
            return new String(bs, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以全局编码读取指定文件中的文本
     */
    public static String readText(String fileName) {
        fileName = normalizePath(fileName);
        return readText(fileName, "utf-8");
    }

    /**
     * 以指定编码读取指定文件中的文本
     */
    public static String readText(String fileName, String encoding) {
        fileName = normalizePath(fileName);
        try {
            InputStream is = new FileInputStream(fileName);
            String str = readText(is, encoding);
            is.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以全局编码读取指定URL中的文本
     */
    public static String readURLText(String urlPath) {
        return readURLText(urlPath, "utf-8");
    }

    /**
     * 以指定编码读取指定URL中的文本
     */
    public static String readURLText(String urlPath, String encoding) {
        try {
            URL url = new URL(urlPath);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream(), encoding));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line + "\n");
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件，不管路径是文件还是文件夹，都删掉。<br>
     * 删除文件夹时会自动删除子文件夹。
     */
    public static boolean delete(String path) {
        path = normalizePath(path);
        File file = new File(path);
        return delete(file);
    }

    /**
     * 删除文件，不管路径是文件还是文件夹，都删掉。<br>
     * 删除文件夹时会自动删除子文件夹。
     */
    public static boolean delete(File f) {
        f = normalizeFile(f);
        if (!f.exists()) {
//            LogUtils.warn("文件或文件夹不存在：" + f);
            return false;
        }
        if (f.isFile()) {
            return f.delete();
        } else {
            return FileUtil.deleteDir(f);
        }
    }

    /**
     * 删除文件夹及其子文件夹
     */
    private static boolean deleteDir(File dir) {
        dir = normalizeFile(dir);
        try {
            return deleteFromDir(dir) && dir.delete(); // 先删除完里面所有内容再删除空文件夹
        } catch (Exception e) {
//            LogUtils.warn("删除文件夹操作出错");
            return false;
        }
    }

    /**
     * 创建文件夹
     */
    public static boolean mkdir(String path) {
        path = normalizePath(path);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return true;
    }

    /**
     * 通配符方式删除指定目录下的文件或文件夹。<br>
     * 文件名支持使用正则表达式（文件路径不支持正则表达式）
     */
    public static boolean deleteEx(String fileName) {
        fileName = normalizePath(fileName);
        int index1 = fileName.lastIndexOf("\\");
        int index2 = fileName.lastIndexOf("/");
        index1 = index1 > index2 ? index1 : index2;
        String path = fileName.substring(0, index1);
        String name = fileName.substring(index1 + 1);
        File f = new File(path);
        if (f.exists() && f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (Pattern.matches(name, files[i].getName())) {
                    files[i].delete();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 删除文件夹里面的所有文件,但不删除自己本身
     */
    public static boolean deleteFromDir(String dirPath) {
        dirPath = normalizePath(dirPath);
        File file = new File(dirPath);
        return deleteFromDir(file);
    }

    /**
     * 删除文件夹里面的所有文件和子文件夹,但不删除自己本身
     *
     * @param dir
     * @return
     */
    public static boolean deleteFromDir(File dir) {
        dir = normalizeFile(dir);
        if (!dir.exists()) {
//            LogUtils.warn("文件夹不存在：" + dir);
            return false;
        }
        if (!dir.isDirectory()) {
//            LogUtils.warn(dir + "不是文件夹");
            return false;
        }
        File[] tempList = dir.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (!delete(tempList[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从指定位置复制文件到另一个文件夹，复制时不符合filter条件的不复制
     */
    public static boolean copy(String oldPath, String newPath, FileFilter filter) {
        oldPath = normalizePath(oldPath);
        newPath = normalizePath(newPath);
        File oldFile = new File(oldPath);
        File[] oldFiles = oldFile.listFiles(filter);
        boolean flag = true;
        if (oldFiles != null) {
            for (int i = 0; i < oldFiles.length; i++) {
                if (!copy(oldFiles[i], newPath + "/" + oldFiles[i].getName())) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * 从指定位置复制文件到另一个文件夹
     */
    public static boolean copy(String oldPath, String newPath) {
        oldPath = normalizePath(oldPath);
        newPath = normalizePath(newPath);
        File oldFile = new File(oldPath);
        return copy(oldFile, newPath);
    }

    public static boolean copy(File oldFile, String newPath) {
        oldFile = normalizeFile(oldFile);
        newPath = normalizePath(newPath);
        if (!oldFile.exists()) {
//            LogUtils.warn("文件或者文件夹不存在：" + oldFile);
            return false;
        }
        if (oldFile.isFile()) {
            return copyFile(oldFile, newPath);
        } else {
            return copyDir(oldFile, newPath);
        }
    }

    /**
     * 复制单个文件
     */
    private static boolean copyFile(File oldFile, String newPath) {
        oldFile = normalizeFile(oldFile);
        newPath = normalizePath(newPath);
        if (!oldFile.exists()) { // 文件存在时
//            LogUtils.warn("文件不存在：" + oldFile);
            return false;
        }
        if (!oldFile.isFile()) { // 文件存在时
//            LogUtils.warn(oldFile + "不是文件");
            return false;
        }
        if (oldFile.getName().equalsIgnoreCase("Thumbs.db")) {
//            LogUtils.warn(oldFile + "忽略此文件");
            return true;
        }

        try {
            int byteread = 0;
            InputStream inStream = new FileInputStream(oldFile); // 读入原文件
            File newFile = new File(newPath);
            // 如果新文件是一个目录，则创建新的File对象
            if (newFile.isDirectory()) {
                newFile = new File(newPath, oldFile.getName());
            }
            FileOutputStream fs = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fs.close();
            inStream.close();
        } catch (Exception e) {
//            LogUtils.warn(
//                    "复制单个文件" + oldFile.getPath() + "操作出错。错误原因:"
//                            + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 复制整个文件夹内容
     */
    private static boolean copyDir(File oldDir, String newPath) {
        oldDir = normalizeFile(oldDir);
        newPath = normalizePath(newPath);
        if (!oldDir.exists()) { // 文件存在时
//            LogUtils.info("文件夹不存在：" + oldDir);
            return false;
        }
        if (!oldDir.isDirectory()) { // 文件存在时
//            LogUtils.info(oldDir + "不是文件夹");
            return false;
        }
        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File[] files = oldDir.listFiles();
            File temp = null;
            for (int i = 0; i < files.length; i++) {
                temp = files[i];
                if (temp.isFile()) {
                    if (!FileUtil
                            .copyFile(temp, newPath + "/" + temp.getName())) {
                        return false;
                    }
                } else if (temp.isDirectory()) {// 如果是子文件夹
                    if (!FileUtil.copyDir(temp, newPath + "/" + temp.getName())) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
//            LogUtils.info("复制整个文件夹内容操作出错。错误原因:" + e.getMessage());
            // e.printStackTrace();
            return false;
        }
    }

    /**
     * 移动文件到指定目录
     */
    public static boolean move(String oldPath, String newPath) {
        oldPath = normalizePath(oldPath);
        newPath = normalizePath(newPath);
        return copy(oldPath, newPath) && delete(oldPath);
    }

    /**
     * 移动文件到指定目录
     */
    public static boolean move(File oldFile, String newPath) {
        oldFile = normalizeFile(oldFile);
        newPath = normalizePath(newPath);
        return copy(oldFile, newPath) && delete(oldFile);
    }

    /**
     * 将可序列化对象序列化并写入指定文件
     */
    public static void serialize(Serializable obj, String fileName) {
        fileName = normalizePath(fileName);
        try {
            FileOutputStream f = new FileOutputStream(fileName);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(obj);
            s.flush();
            s.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将可序列化对象序列化并返回二进制数组
     */
    public static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream s = new ObjectOutputStream(b);
            s.writeObject(obj);
            s.flush();
            s.close();
            return b.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定文件中反序列化对象
     */
    public static Object unSerialize(String fileName) {
        fileName = normalizePath(fileName);
        try {
            FileInputStream in = new FileInputStream(fileName);
            ObjectInputStream s = new ObjectInputStream(in);
            Object o = s.readObject();
            s.close();
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从二进制数组中反序列化对象
     */
    public static Object unSerialize(byte[] bs) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bs);
            ObjectInputStream s = new ObjectInputStream(in);
            Object o = s.readObject();
            s.close();
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将图片内容转换成Base64编码的字符串
     * @param imageFile 图片文件的全路径名称
     * @return 转换成Base64编码的图片内容字符串
     */
    public static Map<String,String> getImageBase64String(String imageFile,int value) {
        if (StringUtils.isEmpty(imageFile)) {
            return null;
        }
        File file = new File(imageFile);
        if (!file.exists()) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        try {
            is = new FileInputStream(file);
            data = new byte[is.available()];
            is.read(data);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = getImageWH(file, value);
        Base64Encoder encoder = new Base64Encoder();
        map.put("encode",encoder.encode(data));
        return map;
    }

    /**
     * 获取图片宽高，并缩小
     * @param file 图片
     * @param value 图片宽高超过指定值则缩小
     */
    public static Map<String,String> getImageWH(File file,int value){
        Map<String,String> map = new HashMap<>();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            //获取图片宽高
            BufferedImage image = ImageIO.read(is);
            int height = image.getHeight();
            int width  = image.getWidth();
            //宽或高大于500时，进行缩放
            if (width > value || height > value){
                int cWidth  = value;
                int cHeight  = value;
                int showWidth = cWidth;
                int showHeight = cHeight;
                //原图宽高太大进行等比缩放
                if(1.0 * width/height >= 1.0 * cWidth/cHeight){
                    //图片比较宽
                    showHeight = showWidth * height / width;
                }else {
                    //图片比较长
                    showWidth = showHeight * width / height;
                }
                map.put("height",Integer.toString(showHeight));
                map.put("width",Integer.toString(showWidth));
            }else {
                map.put("height",Integer.toString(height));
                map.put("width",Integer.toString(width));
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
