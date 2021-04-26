package com.sh.orderapp.utils;

import android.text.TextUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.StringTokenizer;

public class Common {

    public static String userLogin;
    public static boolean flagTableList = false; //false: order;   true:invoice
    public static String tableOrder;
    public static final String PASS_DEFAULT = "123456aA@";
    public static final String STATUS_FINISH = " READY !!!";

    public static String getDecimalFormattedString(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3) {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }
    }

    //region MÃ HÓA PASSWORD
    // Mã hóa password
    public static String getASCIICodeFromText(String text, int iDecode) {
        try {
            StringBuilder res = new StringBuilder();
            if (!TextUtils.isEmpty(text)) {
                // Tạo 1 mảng kiểu char, và gán chuỗi truyền vào
                char[] chars = text.toCharArray();
                for (char ascii : chars) {
                    // Duyệt mảng char
                    // Ép kiểu int theo phần tử mảng char
                    int convert = (int) ascii + iDecode;
                    // chuyển về kiểu String, lấy 3 ký tự bên trái, thiếu thì thêm số 0. Trả về kết quả
                    res.append(StringUtils.leftPad(String.valueOf(convert), 3, "0"));
                }
            }
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Giải mã password
    public static String getTextFromASCIICode(String ascii, int iDecode) {
        try {
            String res = "";
            if (!TextUtils.isEmpty(ascii)) {
                int charCount = ascii.length() / 3;
                for (int i = 0; i < charCount; i++) {
                    // Cắt chuỗi
                    String sub = ascii.substring(0, 3); // có vấn đề với substring rồi :((
                    // Chuyển về kiểu số
                    int temp = Integer.parseInt(sub) - iDecode;
                    // Chuyển về kiểu char
                    char ch = (char) (temp);
                    // Gán kết quả
                    res += ch;
                    ascii = StringUtils.right(ascii, (ascii.length() - 3));
                }
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //endregion
}
