package com.premiersolutionshi.common.util;

import java.text.DecimalFormat;
import java.util.List;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public final class StringUtils {
    private static final double KB_SIZE = 1024.0;
    private static final double MB_SIZE = KB_SIZE * 1024;

    public static boolean safeEquals(String s1, String s2) {
        return s1 == null ? s2 == null : s1.equals(s2);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    /**
     * The start and end times of the "issue" scheduled visit time is stored as
     * an integer. Also, the time is stored in this unusual way. It does not equate to actual minutes.
     * 
     * That is the only reason for this method.
     * @param time
     * @return Formatted time as a string.
     */
    public static String convertToTime(Integer time) {
        if (time == null || time <= 0 || time >= 2400) {
            return "00:00";
        }
        String timeStr = padLeft(time.toString(), "0", 4);
        return timeStr.substring(0, 2) + ":" + timeStr.substring(2);
    }

    public static Integer parseInt(String str) {
        if (!StringUtils.isEmpty(str)) {
            try {
                return new Integer(str);
            }
            catch (NumberFormatException e) {
                // do nothing, we don't care
            }
        }
        return null;
    }

    public static Integer parseInt(Object obj) {
        String str = obj == null ? null : obj.toString();
        return parseInt(str);
    }

    public static String padLeft(Object input, String padStr, int maxSize) {
        return padLeft(input.toString(), padStr, maxSize);
    }

    public static String padLeft(String input, String padStr, int maxSize) {
        StringBuilder output = new StringBuilder();
        if (input == null) {
            input = "";
        }
        int length = input.length();
        if (length < maxSize) {
            int sizeDiff = maxSize - length;
            generatePad(padStr, output, sizeDiff);
        }
        output.append(input);
        return output.toString();
    }

    private static void generatePad(String padStr, StringBuilder output, int size) {
        for (int i = 0; i < size; i++) {
            output.append(padStr);
        }
    }

    /**
     * Removes all non-URL-valid text.
     * @param url
     * @return
     */
    public static String sanitizeUrl(String url) {
        if (isEmpty(url)) {
            return "";
        }
        return url.replaceAll("[^A-Za-z0-9/:\\.\\s\\$=&;@\\-%\\?_.\\+\\!\\*'\\(\\),]", "");
    }

    public static String removeXss(String str) {
        if (!isEmpty(str)) {
            str = str.replaceAll("&", "")
                .replaceAll("<", "")
                .replaceAll(">", "")
                .replaceAll("\"", "") // double-quote
                .replaceAll("'", "")
                .trim()
            ;
        }
        return str;
    }

    /**
     * This should support text for "QuillJs" or whatever Rich Text Editor we intend to use.
     * @param untrustedHtml
     * @return
     */
    public static String sanitizeHtml(String untrustedHtml) {
        if (isEmpty(untrustedHtml)) {
            return untrustedHtml;
        }
        PolicyFactory policy = new HtmlPolicyBuilder()
            //.allowAttributes("src").onElements("img")
            .allowAttributes("href", "target").onElements("a")
            .allowStandardUrlProtocols()
            .allowElements(
                "a", "b", "i", "p", "u", "li", "ol", "ul", "br", "img"
                , "h1", "h2", "h3"
                , "div", "span", "strong"
            ).toFactory();
        return policy.sanitize(untrustedHtml); 
    }

    public static String removeAllButDashesAndNumbers(String str) {
        return isEmpty(str) ? null : str.replaceAll("[^0-9\\-]", "").trim();
    }

    /**
     * Prevent cross-site scripting.
     * https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet
     * @param str
     * @return
     */
    public static String escapeHtml(String str) {
        if (!isEmpty(str)) {
            str = str.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("[″“”\"]", "&quot;")// double-quote
                .replaceAll("[′'’]", "&#x27;")
                //.replaceAll("/", "&#x2F;") //this is causing problems
                .trim()
            ;
        }
        return str;
    }

    public static String delimitList(List<String> strList, String delimiter) {
        if (strList == null || strList.isEmpty()) {
            return null;
        }
        StringBuilder str = new StringBuilder();
        int size = strList.size();
        str.append(strList.get(0));
        for (int i = 1; i < size; i++) {
            str.append(delimiter).append(strList.get(i));
        }
        return str.toString();
    }

    public static String formatBytes(Integer bytes) {
        if (bytes == null) {
            bytes = 0;
        }
        DecimalFormat decimalFormat = new DecimalFormat(".#");
        if (bytes > MB_SIZE) {
            return decimalFormat.format((double) bytes / MB_SIZE) + " MB";
        }
        else if (bytes > KB_SIZE) {
            return decimalFormat.format((double) bytes / KB_SIZE) + " KB";
        }
        return bytes + " bytes";
    }

    public static void main(String[] args) {
        System.out.println(formatBytes(1500));
        System.out.println(formatBytes(15000000));
//        System.out.println(convertToTime(1));
//        System.out.println(convertToTime(500));
//        System.out.println(convertToTime(1200));
//        System.out.println(convertToTime(2300));
//        System.out.println(convertToTime(50000));
//        String url = "http://local-host.com.hi:8081/codegen2/php/?asdf=asdf;&adsf$";
//        System.out.println(url);
//        System.out.println(sanitizeUrl(url));
//        System.out.println(removeAllButDashesAndNumbers("69832-6351asdf323a\\//asdf]a]s|}!@#$^%&*"));
        System.out.println(escapeHtml("[”″]\"[′'32-6351asdf323a\\//asdf]a]s|}!@#$^%&*"));
    }

}
