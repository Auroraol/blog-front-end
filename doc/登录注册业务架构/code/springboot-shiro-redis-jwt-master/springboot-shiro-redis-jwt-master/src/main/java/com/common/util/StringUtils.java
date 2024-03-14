package com.common.util;

import cn.hutool.core.util.NumberUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * UTF-8的三个字节的BOM
     */
    public static final byte[] BOM = new byte[] { (byte) 239, (byte) 187, (byte) 191 };

    /**
     * 十六进制字符
     */
    public static final char HexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };

    /**
     * 获取指定字符串的MD5摘要
     */
    public static byte[] md5(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md = md5.digest(src.getBytes());
            return md;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取指定二进制数组的MD5摘要
     */
    public static byte[] md5(byte[] src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md = md5.digest(src);
            return md;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串进行md5摘要，然后输出成十六进制形式
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md = md5.digest(src.getBytes());
            return hexEncode(md);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串进行sh1摘要，然后输出成十六进制形式
     */
    public static String sha1Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA1");
            byte[] md = md5.digest(src.getBytes());
            return hexEncode(md);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串进行MD5摘要，输出成BASE64形式
     */
    public static String md5Base64(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return base64Encode(md5.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将十六制进表示MD5摘要转换成BASE64格式
     */
    public static String md5Base64FromHex(String md5str) {
        char[] cs = md5str.toCharArray();
        byte[] bs = new byte[16];
        for (int i = 0; i < bs.length; i++) {
            char c1 = cs[i * 2];
            char c2 = cs[i * 2 + 1];
            byte m1 = 0;
            byte m2 = 0;
            for (byte k = 0; k < 16; k++) {
                if (HexDigits[k] == c1) {
                    m1 = k;
                }
                if (HexDigits[k] == c2) {
                    m2 = k;
                }
            }
            bs[i] = (byte) (m1 << 4 | 0x0 + m2);

        }
        String newstr = base64Encode(bs);
        return newstr;
    }

    /**
     * 将十六制进表示MD5摘要转换成BASE64格式
     */
    public static String md5HexFromBase64(String base64str) {
        return hexEncode(base64Decode(base64str));
    }

    /**
     * 将二进制数组转换成十六进制表示
     */
    public static String hexEncode(byte[] bs) {
        return new String(new Hex().encode(bs));
    }

    /**
     * 将字符串转换成十六进制表示
     */
    public static byte[] hexDecode(String str) {
        try {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            char[] cs = str.toCharArray();
            return Hex.decodeHex(cs);
        } catch (org.apache.commons.codec.DecoderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字节数组转换成二制形式字符串
     */
    public static String byteToBin(byte[] bs) {
        char[] cs = new char[bs.length * 9];
        for (int i = 0; i < bs.length; i++) {
            byte b = bs[i];
            int j = i * 9;
            cs[j] = (b >>> 7 & 1) == 1 ? '1' : '0';
            cs[j + 1] = (b >>> 6 & 1) == 1 ? '1' : '0';
            cs[j + 2] = (b >>> 5 & 1) == 1 ? '1' : '0';
            cs[j + 3] = (b >>> 4 & 1) == 1 ? '1' : '0';
            cs[j + 4] = (b >>> 3 & 1) == 1 ? '1' : '0';
            cs[j + 5] = (b >>> 2 & 1) == 1 ? '1' : '0';
            cs[j + 6] = (b >>> 1 & 1) == 1 ? '1' : '0';
            cs[j + 7] = (b & 1) == 1 ? '1' : '0';
            cs[j + 8] = ',';
        }
        return new String(cs);
    }

    /**
     * 转换字节数组为十六进制字串
     */

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
            resultSb.append(" ");
        }
        return resultSb.toString();
    }

    /**
     * 字节转换为十六进制字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HexDigits[d1] + "" + HexDigits[d2];
    }

    /**
     * 判断指定的二进制数组是否是一个UTF-8字符串
     */
    public static boolean isUTF8(byte[] bs) {
        if (StringUtils.hexEncode(ArrayUtils.subarray(bs, 0, 3)).equals("efbbbf")) {// BOM标志
            return true;
        }
        int encodingBytesCount = 0;
        for (int i = 0; i < bs.length; i++) {
            byte c = bs[i];
            if (encodingBytesCount == 0) {
                if ((c & 0x80) == 0) {// ASCII字符范围0x00-0x7F
                    continue;
                }
                if ((c & 0xC0) == 0xC0) {
                    encodingBytesCount = 1;
                    c <<= 2;
                    // 非ASCII第一字节用来存储长度
                    while ((c & 0x80) == 0x80) {
                        c <<= 1;
                        encodingBytesCount++;
                    }
                } else {
                    return false;// 不符合 UTF8规则
                }
            } else {
                // 后续字集必须以10开头
                if ((c & 0xC0) == 0x80) {
                    encodingBytesCount--;
                } else {
                    return false;// 不符合 UTF8规则
                }
            }
        }
        if (encodingBytesCount != 0) {
            return false;// 后续字节数不符合UTF8规则
        }
        return true;
    }

    /**
     * 返回二进制数组的BASE64编码结果
     */
    public static String base64Encode(byte[] b) {
        if (b == null) {
            return null;
        }
        String be = Base64.getEncoder().encodeToString(b);
        be = be.replaceAll("[\\s*\t\n\r]", "");
        return be;
    }

    /**
     * 返回二进制数组的BASE64编码结果
     */
    public static String base64Encode(String s) {
        if (s == null) {
            return null;
        }
        return base64Encode(s.getBytes());
    }

    /**
     * 将 BASE64 编码的字符串进行解码
     */
    public static byte[] base64Decode(String s) {
        if (s != null) {
            try {
                return Base64.getDecoder().decode(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将字符串转换成可以在JAVA表达式中直接使用的字符串，处理一些转义字符
     */
    public static String javaEncode(String txt) {
        if (txt == null || txt.length() == 0) {
            return txt;
        }
        txt = replaceEx(txt, "\\", "\\\\");
        txt = replaceEx(txt, "\r\n", "\n");
        txt = replaceEx(txt, "\r", "\\r");
        txt = replaceEx(txt, "\t", "\\t");
        txt = replaceEx(txt, "\n", "\\n");
        txt = replaceEx(txt, "\"", "\\\"");
        txt = replaceEx(txt, "\'", "\\\'");
        return txt;
    }

    /**
     * 将StringUtils.javaEncode()处理过的字符还原
     */
    public static String javaDecode(String txt) {
        if (txt == null || txt.length() == 0) {
            return txt;
        }
        StringBuffer sb = new StringBuffer();
        int lastIndex = 0;
        while (true) {
            int index = txt.indexOf("\\", lastIndex);
            if (index < 0) {
                break;
            }
            sb.append(txt.substring(lastIndex, index));
            if (index < txt.length() - 1) {
                char c = txt.charAt(index + 1);
                if (c == 'n') {
                    sb.append("\n");
                } else if (c == 'r') {
                    sb.append("\r");
                } else if (c == '\'') {
                    sb.append("\'");
                } else if (c == '\"') {
                    sb.append("\"");
                } else if (c == '\\') {
                    sb.append("\\");
                }
                lastIndex = index + 2;
                continue;
            } else {
                sb.append(txt.substring(index, index + 1));
            }
            lastIndex = index + 1;
        }
        sb.append(txt.substring(lastIndex));
        return sb.toString();
    }

    /**
     * 将一个字符串按照指下的分割字符串分割成数组。分割字符串不作正则表达式处理，<br>
     * String类的split方法要求以正则表达式分割字符串，有时较为不便，可以转为采用本方法。
     */
    public static String[] splitEx(String str, String spliter) {
        return splitEx(str, spliter, '\\');
    }

    public static String[] splitEx(String str, String spliter, char escapeChar) {
        if (str == null) {
            return null;
        }
        if (spliter == null || spliter.equals("") || str.length() < spliter.length()) {
            String[] t = { str };
            return t;
        }
        List<String> al = new ArrayList<String>();
        char[] cs = str.toCharArray();
        char[] ss = spliter.toCharArray();
        int length = spliter.length();
        int lastIndex = 0;
        for (int i = 0; i <= str.length() - length;) {
            if (cs[i] == escapeChar) {
                i++;
            }
            boolean notSuit = false;
            for (int j = 0; j < length; j++) {
                if (cs[i + j] != ss[j]) {
                    notSuit = true;
                    break;
                }
            }
            if (!notSuit) {
                al.add(str.substring(lastIndex, i));
                i += length;
                lastIndex = i;
            } else {
                i++;
            }
        }
        if (lastIndex <= str.length()) {
            al.add(str.substring(lastIndex, str.length()));
        }
        String[] t = new String[al.size()];
        for (int i = 0; i < al.size(); i++) {
            t[i] = (String) al.get(i);
        }
        return t;
    }

    /**
     * 将一个字符串中的指定片段全部替换，替换过程中不进行正则处理。<br>
     * 使用String类的replaceAll时要求片段以正则表达式形式给出，有时较为不便，可以转为采用本方法。
     */
    public static String replaceEx(String str, String subStr, String reStr) {
        if (str == null) {
            return null;
        }
        if (subStr == null || subStr.equals("") || subStr.length() > str.length() || reStr == null) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        int lastIndex = 0;
        while (true) {
            int index = str.indexOf(subStr, lastIndex);
            if (index < 0) {
                break;
            } else {
                sb.append(str.substring(lastIndex, index));
                sb.append(reStr);
            }
            lastIndex = index + subStr.length();
        }
        sb.append(str.substring(lastIndex));
        return sb.toString();
    }

    /**
     * 不区分大小写的全部替换，替换时使用了正则表达式。
     */
    public static String replaceAllIgnoreCase(String source, String oldstring, String newstring) {
        Pattern p = Pattern.compile(oldstring, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
        return m.replaceAll(newstring);
    }

    /**
     * 对字符串进行HTML编码
     */
    public static String htmlEncode(String txt) {
        return StringEscapeUtils.escapeJson(txt);
    }

    /**
     * 对字符串进行HTML解码
     */
    public static String htmlDecode(String txt) {
        txt = replaceEx(txt, "&#8226;", "·");
        return StringEscapeUtils.unescapeJson(txt);
    }

    /**
     * 替换字符串中的双引号
     */
    public static String quotEncode(String txt) {
        if (txt == null || txt.length() == 0) {
            return txt;
        }
        txt = replaceEx(txt, "&", "&amp;");
        txt = replaceEx(txt, "\"", "&quot;");
        return txt;
    }

    /**
     * 还原通过StringUtils.quotEncode()编码的字符串
     */
    public static String quotDecode(String txt) {
        if (txt == null || txt.length() == 0) {
            return txt;
        }
        txt = replaceEx(txt, "&quot;", "\"");
        txt = replaceEx(txt, "&amp;", "&");
        return txt;
    }

    /**
     * Javascript中escape的JAVA实现
     */
    public static String escape(String src) {
        char j;
        StringBuffer sb = new StringBuffer();
        sb.ensureCapacity(src.length() * 6);
        for (int i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
                sb.append(j);
            } else if (j < 256) {
                sb.append("%");
                if (j < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toString(j, 16));
            } else {
                sb.append("%u");
                sb.append(Integer.toString(j, 16));
            }
        }
        return sb.toString();
    }

    /**
     * Javascript中unescape的JAVA实现
     */
    public static String unescape(String src) {
        StringBuffer sb = new StringBuffer();
        sb.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    sb.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    sb.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    sb.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    sb.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 在一字符串左边填充若干指定字符，使其长度达到指定长度
     */
    public static String leftPad(String srcString, char c, int length) {
        if (srcString == null) {
            srcString = "";
        }
        int tLen = srcString.length();
        int i, iMax;
        if (tLen >= length)
            return srcString;
        iMax = length - tLen;
        StringBuffer sb = new StringBuffer();
        for (i = 0; i < iMax; i++) {
            sb.append(c);
        }
        sb.append(srcString);
        return sb.toString();
    }

    /**
     * 将长度超过length的字符串截取length长度，若不足，则返回原串
     */
    public static String subString(String src, int length) {
        if (src == null) {
            return null;
        }
        int i = src.length();
        if (i > length) {
            return src.substring(0, length);
        } else {
            return src;
        }
    }

    /**
     * 将长度超过length的字符串截取length长度，若不足，则返回原串。<br>
     * 其中ASCII字符只算半个长度单位。
     */
    public static String subStringEx(String src, int length) {
        length = length * 2;
        if (src == null) {
            return null;
        }
        int k = lengthEx(src);
        if (k > length) {
            int m = 0;
            boolean unixFlag = false;
            String osname = System.getProperty("os.name").toLowerCase();
            if (osname.indexOf("sunos") > 0 || osname.indexOf("solaris") > 0 || osname.indexOf("aix") > 0) {
                unixFlag = true;
            }
            try {
                byte[] b = src.getBytes("Unicode");
                for (int i = 2; i < b.length; i += 2) {
                    byte flag = b[i + 1];
                    if (unixFlag) {
                        flag = b[i];
                    }
                    if (flag == 0) {
                        m++;
                    } else {
                        m += 2;
                    }
                    if (m > length) {
                        return src.substring(0, (i - 2) / 2);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException("执行方法getBytes(\"Unicode\")时出错！");
            }
        }
        return src;
    }

    /**
     * 将长度超过length的字符串截取length长度，若不足，则返回原串。<br>
     * 其中ASCII字符只算半个长度单位。改进：当截取字符串后，发现截取位置恰巧为标点符号的处理
     */
    public static String subStringExToPunctuation(String src, int length) {
        if (src == null) {
            return null;
        }
        length = length * 2;
        int k = lengthEx(src);
        if (k > length) {
            int m = 0;
            boolean unixFlag = false;
            String osname = System.getProperty("os.name").toLowerCase();
            if (osname.indexOf("sunos") > 0 || osname.indexOf("solaris") > 0 || osname.indexOf("aix") > 0) {
                unixFlag = true;
            }
            try {
                byte[] b = src.getBytes("Unicode");
                for (int i = 2; i < b.length; i += 2) {
                    byte flag = b[i + 1];
                    if (unixFlag) {
                        flag = b[i];
                    }
                    if (flag == 0) {
                        m++;
                    } else {
                        m += 2;
                    }
                    if (m > length) {
                        return dealPunctuation(src.substring(0, (i - 2) / 2));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException("执行方法getBytes(\"Unicode\")时出错！");
            }
        }
        // 以下是截取字符串后，结尾恰巧是标点符号的情况的处理
        return dealPunctuation(src);
    }

    /**
     * 处理字符串结尾的字符串
     */
    public static String dealPunctuation(String src) {
        String tail = src.substring(src.length() - 1);
        if (tail.replaceAll("\\pP|\\pS|\\“|\\”", "").length() == 0) {
            String index1 = src.substring(0, src.length() - 2); // 考虑 。”
            String index2 = src.substring(src.length() - 2, src.length());
            index2 = index2.replaceAll("\\pP|\\pS|\\“|\\”", "");
            return index1 + index2;
        } else {
            return src;
        }
    }

    /**
     * 获得字符串的长度，其中ASCII字符算1个长度单位,非ASCII字符算两个长度单位
     */
    public static int lengthEx(String src) {
        int length = 0;
        boolean bigFlag = true;// Unicode字节序是否是从大到小，即前两字节是-1,-2，还是-2,-1
        try {
            byte[] b = src.getBytes("Unicode");
            if (b.length == 0) {
                return 0;
            }
            if (b[0] == -2) {
                bigFlag = false;
            }
            for (int i = 2; i < b.length; i += 2) {
                byte flag = b[i + 1];
                if (!bigFlag) {
                    flag = b[i];
                }
                if (flag == 0) {
                    length++;
                } else {
                    length += 2;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("执行方法getBytes(\"Unicode\")时出错！");
        }
        return length;
    }

    /**
     * 在一字符串右边填充若干指定字符，使其长度达到指定长度
     */
    public static String rightPad(String srcString, char c, int length) {
        if (srcString == null) {
            srcString = "";
        }
        int tLen = srcString.length();
        int i, iMax;
        if (tLen >= length)
            return srcString;
        iMax = length - tLen;
        StringBuffer sb = new StringBuffer();
        sb.append(srcString);
        for (i = 0; i < iMax; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 清除字符右边的空格
     */
    public static String rightTrim(String src) {
        if (src != null) {
            char[] chars = src.toCharArray();
            for (int i = chars.length - 1; i >= 0; i--) {
                if (chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\r') {
                    continue;
                } else {
                    return src.substring(0, i + 1);
                }
            }
            return "";// 说明全是空格
        }
        return src;
    }

    /**
     * 半角转全角，转除英文字母之外的字符
     */
    public static String toSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if ((c[i] > 64 && c[i] < 91) || (c[i] > 96 && c[i] < 123)) {
                continue;
            }

            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * 半角转全角，转所有能转为全角的字符，包括英文字母
     */
    public static String toNSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }

            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * 全角转半角的函数 全角空格为12288，半角空格为32 <br>
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换
     *
     * @param cnStr
     *            String 字符串
     * @return String 转换成全拼后的字符串
     */
    public static String getChineseFullSpell(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }
        return ChineseSpelling.convert(cnStr);
    }

    /**
     * 返回字符串的第一个汉字的全拼
     *
     * @param cnStr
     * @return
     */
    public static String getChineseFamilyNameSpell(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }
        return ChineseSpelling.convertName(cnStr);
    }

    public static String getChineseFirstAlpha(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }
        return ChineseSpelling.getFirstAlpha(cnStr);
    }

    public static final Pattern PTitle = Pattern.compile("<title>(.+?)</title>",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * 从一段html文本中提取出<title>标签内容
     */
    public static String getHtmlTitle(File f) {
        String html = FileUtil.readText(f);
        String title = getHtmlTitle(html);
        return title;
    }

    /**
     * 从一段html文本中提取出<title>标签内容
     */
    public static String getHtmlTitle(String html) {
        Matcher m = PTitle.matcher(html);
        if (m.find()) {
            return m.group(1).trim();
        }
        return null;
    }

    public static Pattern patternHtmlTag = Pattern.compile("<[^<>]+>", Pattern.DOTALL);

    /**
     * 清除HTML文本中所有标签
     */
    public static String clearHtmlTag(String html) {
        String text = patternHtmlTag.matcher(html).replaceAll("");
        if (isEmpty(text)) {
            return "";
        }
        text = StringUtils.htmlDecode(text);
        return text.replaceAll("[\\s　]{2,}", " ");
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1))
                .toString();
    }

    /**
     * 首字母大写
     */
    public static String lowerFirstCharacter(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen).append(str.substring(0, 1).toLowerCase()).append(str.substring(1)).toString();
    }

    /**
     * 字符串是否为空，null或空字符串时返回true,其他情况返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 字符串是否不为空，null或空字符串时返回false,其他情况返回true
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    /**
     * 字符串为空时返回defaultString，否则返回原串
     */
    public static final String noNull(String string, String defaultString) {
        return isEmpty(string) ? defaultString : string;
    }

    /**
     * 字符串为空时返回defaultString，否则返回空字符串
     */
    public static final String noNull(String string) {
        return noNull(string, "");
    }

    /**
     * 将一个数组拼成一个字符串，数组项之间以逗号分隔
     */
    public static String join(Object[] arr) {
        return join(arr, ",");
    }

    /**
     * 将一个数组拼成一个字符串，数组项之间以逗号分隔
     */
    public static String join(Collection<String> arr) {
        return join((List<String>) arr, ",");
    }

    /**
     * 将一个二维数组拼成一个字符串，第二维以逗号分隔，第一维以换行分隔
     */
    public static String join(Object[][] arr) {
        return join(arr, "\n", ",");
    }

    /**
     * 将一个数组以指定的分隔符拼成一个字符串
     */
    public static String join(Object[] arr, String spliter) {
        if (arr == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                sb.append(spliter);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 将一个二维数组拼成一个字符串，第二维以指定的spliter2参数分隔，第一维以换行spliter1分隔
     */
    public static String join(Object[][] arr, String spliter1, String spliter2) {
        if (arr == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                sb.append(spliter2);
            }
            sb.append(join(arr[i], spliter2));
        }
        return sb.toString();
    }

    /**
     * 将一个List拼成一个字符串，数据项之间以逗号分隔
     */
    public static String join(List<String> list) {
        return join(list, ",");
    }

    /**
     * 将一个List拼成一个字符串，数据项之间以指定的参数spliter分隔
     */
    public static String join(List<String> list, String spliter) {
        if (list == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(spliter);
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * 计算一个字符串中某一子串出现的次数
     */
    public static int count(String str, String findStr) {
        int lastIndex = 0;
        int length = findStr.length();
        int count = 0;
        int start = 0;
        while ((start = str.indexOf(findStr, lastIndex)) >= 0) {
            lastIndex = start + length;
            count++;
        }
        return count;
    }

    public static final Pattern PLetterOrDigit = Pattern.compile("^\\w*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static final Pattern PLetter = Pattern.compile("^[A-Za-z]*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static final Pattern PDigit = Pattern.compile("^\\d*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static final Pattern PNumberStr = Pattern.compile("^\\d+\\w*\\d+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * 判断字符串是否全部由数字或字母组成
     */
    public static boolean isLetterOrDigit(String str) {
        return PLetterOrDigit.matcher(str).find();
    }

    /**
     * 判断字符串是否全部字母组成
     */
    public static boolean isLetter(String str) {
        return PLetter.matcher(str).find();
    }

    /**
     * 判断字符串是否全部由数字组成
     */
    public static boolean isDigit(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return PDigit.matcher(str).find();
    }

    private static Pattern chinesePattern = Pattern.compile("[^\u4e00-\u9fa5]+",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * 判断字符串中是否含有中文字符
     */
    public static boolean containsChinese(String str) {
        if (!chinesePattern.matcher(str).matches()) {
            return true;
        }
        return false;
    }

    private static Pattern idPattern = Pattern.compile("[\\w\\s\\_\\.\\,]*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * 检查ID，防止SQL注入，主要是在删除时传入多个ID时使用
     */
    public static boolean checkId(String str) {
        if (StringUtils.isEmpty(str)) {
            return true;
        }
        if (idPattern.matcher(str).matches()) {
            return true;
        }
        return false;
    }

    private static Pattern uuidPattern = Pattern.compile("[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    /**
     * 检查ID，防止SQL注入，主要是在删除时传入多个ID时使用
     */
    public static boolean checkUuid(String str) {
        if (StringUtils.isEmpty(str)) {
            return true;
        }
        boolean matchAll = true;
        List<String> l = splitComma2List(str);
        for (String s : l) {
            if (!uuidPattern.matcher(s).matches()) {
                matchAll = false;
                break;
            }
        }
        return matchAll;
    }

    /**
     * 将一个类似于Name=John,Age=18,Gender=3的字符串拆成一个Map
     */
    public static Map<String, String> splitToMapx(String str, String entrySpliter, String keySpliter) {
        Map<String, String> map = new HashMap<String, String>();
        String[] arr = StringUtils.splitEx(str, entrySpliter);
        for (int i = 0; i < arr.length; i++) {
            String[] arr2 = StringUtils.splitEx(arr[i], keySpliter);
            String key = arr2[0];
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            key = key.trim();
            String value = null;
            if (arr2.length > 1) {
                value = arr2[1];
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * 将一个类似于Name=John,Age=18,Gender=3的字符串拆成一个List
     */
    public static List<String> split2List(String str, String spliter) {
        List<String> list = null;
        String[] arr = StringUtils.splitEx(str, spliter);
        if (arr != null && arr.length > 0) {
            list = new ArrayList<String>();
            for (int i = 0; i < arr.length; i++) {
                if (StringUtils.isEmpty(arr[i].trim())) {
                    continue;
                }
                list.add(arr[i]);
            }
        }
        return list;
    }

    /**
     */
    public static List<String> splitComma2List(String str) {
        return split2List(str, ",");
    }

    /**
     * 得到URL的文件扩展名
     */
    public static String getURLExtName(String url) {
        if (isEmpty(url)) {
            return null;
        }
        int index1 = url.indexOf('?');
        if (index1 == -1) {
            index1 = url.length();
        }
        int index2 = url.lastIndexOf('.', index1);
        if (index2 == -1) {
            return null;
        }
        int index3 = url.indexOf('/', 8);
        if (index3 == -1) {
            return null;
        }
        String ext = url.substring(index2 + 1, index1);
        if (ext.matches("[^\\/\\\\]*")) {
            return ext;
        }
        return null;
    }

    /**
     * 得到URL的文件名
     */
    public static String getURLFileName(String url) {
        if (isEmpty(url)) {
            return null;
        }
        int index1 = url.indexOf('?');
        if (index1 == -1) {
            index1 = url.length();
        }
        int index2 = url.lastIndexOf('/', index1);
        if (index2 == -1 || index2 < 8) {
            return null;
        }
        String ext = url.substring(index2 + 1, index1);
        return ext;
    }

    /**
     * 将一个GBK编码的字符串转成UTF-8编码的二进制数组，转换后没有BOM位
     */
    public static byte[] GBKToUTF8(String chinese) {
        return GBKToUTF8(chinese, false);
    }

    /**
     * 将一个GBK编码的字符串转成UTF-8编码的二进制数组，如果参数bomFlag为true，则转换后有BOM位
     */
    public static byte[] GBKToUTF8(String chinese, boolean bomFlag) {
        return CharsetConvert.GBKToUTF8(chinese, bomFlag);
    }

    /**
     * 将UTF-8编码的字符串转成GBK编码的字符串
     */
    public static byte[] UTF8ToGBK(String chinese) {
        return CharsetConvert.UTF8ToGBK(chinese);
    }

    /**
     * 将UTF-8编码的字符串转成ISO编码的字符串
     *
     * @param utf8Str
     * @return
     */
    public static String UTF8ToISO8859_1(String utf8Str) {
        String isoStr = null;
        try {
            isoStr = new String(utf8Str.getBytes("UTF-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        return isoStr;
    }

    /**
     * 去掉XML字符串中的非法字符, 在XML中0x00-0x20 都会引起一定的问题
     */
    public static String clearForXML(String str) {
        char[] cs = str.toCharArray();
        char[] ncs = new char[cs.length];
        int j = 0;
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] > 0xFFFD) {
                continue;
            } else if (cs[i] < 0x20 && cs[i] != '\t' & cs[i] != '\n' & cs[i] != '\r') {
                continue;
            }
            ncs[j++] = cs[i];
        }
        ncs = ArrayUtils.subarray(ncs, 0, j);
        return new String(ncs);
    }

    /**
     * 带逗号的字符转Long数组
     *
     * @param str
     * @return
     */
    public static Long[] toLongArray(String str) {
        Long[] ls = null;
        if (StringUtils.isNotEmpty(str)) {
            String[] strArray = str.split(",");
            if (strArray != null && strArray.length > 0) {
                ls = new Long[strArray.length];
                for (int i = 0; i < strArray.length; i++) {
                    ls[i] = Long.parseLong(strArray[i].trim());
                }
            }
        }
        return ls;
    }

    /**
     * 判断某一个字符串是否在数组里
     */
    public static boolean isInArray(String[] array, String value) {
        boolean checked = false;
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (array[i].equalsIgnoreCase(value)) {
                    checked = true;
                    break;
                }
            }
        }
        return checked;
    }

    /**
     * 判断某一个字符串是否在数组里
     * @return
     */
    public static boolean isInList(List<String> list, String value) {
        boolean checked = false;
        if (list != null && CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equalsIgnoreCase(value)) {
                    checked = true;
                    break;
                }
            }
        }
        return checked;
    }

    /**
     * 字符串分割
     *
     * @param str
     * @return
     */
    public static String[] commaDelimitedListToStringArray(String str) {
        return org.springframework.util.StringUtils.commaDelimitedListToStringArray(str);
    }

    /**
     * 去除末尾字符
     *
     * @param str
     * @return
     */
    public static String trimEndComma(String str) {
        return trimEndString(trimEndString(str, ","), "，");
    }

    /**
     * 去除末尾字符
     *
     * @param str
     * @return
     */
    public static String trimEndString(String str, String endStr) {
        if (StringUtils.isNotEmpty(str) && str.endsWith(endStr)) {
            str = str.substring(0, str.length() - endStr.length());
        }
        return str;
    }

    /**
     * 文件大小转可读字符串
     *
     * @param size
     * @return
     */
    public static String getFileSizeString(Long size) {
        String fileMaxSizeHuman = "";
        if (size < 1048576) {
            fileMaxSizeHuman = NumberUtil.round(size * 1.0 / 1024, 1) + "K";
        } else if (size < 1073741824) {
            fileMaxSizeHuman = NumberUtil.round(size * 1.0 / 1048576, 1) + "M";
        }
        return fileMaxSizeHuman;
    }

    /**
     * 字符串列表转
     *
     * @param l
     * @return
     */
    public static String list2String(List<String> l, String spliter) {
        String ret = null;
        if (CollectionUtils.isNotEmpty(l)) {
            ret = "";
            for (String s : l) {
                ret += s.trim() + spliter;
            }
            ret = trimEndComma(ret);
        }
        return ret;
    }

    public static String list2String(List<String> l) {
        return list2String(l, ",");
    }

    /**
     * 合并两个字符串
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String merge(String str1, String str2) {
        List<String> ls1 = splitComma2List(str1);
        List<String> ls2 = splitComma2List(str2);

        if (CollectionUtils.isEmpty(ls1) && CollectionUtils.isEmpty(ls2)) {
            return null;
        } else {
            if (CollectionUtils.isEmpty(ls1)) {
                return list2String(ls2);
            } else if (CollectionUtils.isEmpty(ls2)) {
                return list2String(ls1);
            } else {
                for (String s : ls2) {
                    if (!ls1.contains(s)) {
                        ls1.add(s);
                    }
                }
                return list2String(ls1);
            }
        }
    }

    /**
     * 去掉字符串前面和后面的所有空格（全角和半角）
     */
    public static String trimFullWidth(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        return str.replaceAll("^[　*| *]*", "").replaceAll("[　*| *]*$", "");
    }

    /**
     * 获取字符串中的数字
     *
     * @param str
     * @return
     */
    public static String getDigit(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        Matcher m = PNumberStr.matcher(str);
        if (m.find()) {
            return m.group(0);
        }
        return "";

    }

    /**
     * 验证字符串是否是一个合法的手机号码
     */
    public static boolean isPhone(String phone) {
        boolean isPhone = false;
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        if (m.matches()) {
            isPhone = true;
        }
        return isPhone;
    }

    /**
     *
     * @param source
     * @return
     */
    public static String htmlEscape(String source) {
        if (source == null) {
            return "";
        }
        String html = "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '"':
                    buffer.append("&quot;");
                    break;
                case '\'':
                    buffer.append("&#39;");
                    break;
                case '\\':
                    buffer.append("&#092;");
                    break;
                case 10:
                case 13:
                    break;
                default:
                    buffer.append(c);
            }
        }
        html = buffer.toString();
        return html;
    }
}
