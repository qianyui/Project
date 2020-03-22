package utils;

/**
 * <p>
 * String字符串工具类
 * </p>
 *
 * @Authoe: diudiu
 * @Date: 2020/3/22
 **/
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     * @param str
     * @param strStart
     * @param strEnd
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {
        String result = null;
        if(isNotBlank(str)){
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = str.indexOf(strStart);
            int strEndIndex = str.indexOf(strEnd);

            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strStartIndex < 0) {
                System.out.println("字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串");
                return null;
            }
            if (strEndIndex < 0) {
                System.out.println("字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串");
                return null;
            }
            /* 开始截取 */
            result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        }
        return result;
    }
}
