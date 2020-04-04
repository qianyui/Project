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
     * @param str   字符串str
     * @param strStart    截取开始字符
     * @param strEnd        截取完成字符
     * @return          截取完的字符串
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

    /**
     * 首字母大写
     *
     * @param srcStr  字符串srcStr
     * @return      转换为首字母大写的字符串
     */
    public static String firstCharacterToUpper(String srcStr) {
        return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
    }

    /**
     * 替换字符串并让它的下一个字母为大写
     *("ni_hao_abc", "_", "")
     * @param srcStr    字符串srcStr
     * @param org       需替换的字符串
     * @param ob        代替的字符串
     * @return             替换完的字符串
     */
    public static String replaceUnderlineAndfirstToUpper(String srcStr, String org, String ob) {
        StringBuilder newString = new StringBuilder();
        int first;
        while (srcStr.contains(org)) {
            first = srcStr.indexOf(org);
            if (first != srcStr.length()) {
                newString.append(srcStr, 0, first).append(ob);
                srcStr = srcStr.substring(first + org.length());
                srcStr = firstCharacterToUpper(srcStr);
            }
        }
        newString.append(srcStr);
        return newString.toString().trim();
    }

    /**
     * 截取在startStr之前的字符串
     * @param str       字符串str
     * @param startStr  需在该字符串之前截取
     * @return          返回截取之后的字符串
     */
    public static String subString(String str,String startStr){
        if (isBlank(str) || isBlank(startStr)) {
            System.out.println("字符串为空");
            return null;
        }
        int first = str.indexOf(startStr);
        if(first != -1){
            return str.substring(0, first);
        }else{
            System.out.println("在字符串："+str+"中找不到字符串："+startStr+"！！！");
            return null;
        }
    }

    /**
     * 数据库字段类型转换
     * @param fieldType     字符串类型
     * @return      返回Java类型
     */
    public static String convertFieldType(String fieldType){
        if(isBlank(fieldType)){
            return null;
        }
        String fieldTypeJava = "";
        //  转小写
        fieldType = fieldType.toLowerCase();
        if(contains(fieldType,"varchar") || contains(fieldType,"clob") || contains(fieldType,"blob")){
            fieldTypeJava = "String";
        }else if(contains(fieldType,"number") || contains(fieldType,"decimal") ){
            fieldTypeJava = "int";
        }else if(contains(fieldType,"date")){
            fieldTypeJava = "Date";
        }
        return fieldTypeJava;
    }

    /**
     * 首字母大写
     * @param str   需首大写的字符串
     * @return      首字母大写的字符串
     */
    public static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 测试
     * @param args 参数
     */
    public static void main(String[] args) {
        String str = "(adsf_asdfas（dfd45454515_fasd)";
        System.out.println(str.replaceAll("\\(","（"));
    }
}
