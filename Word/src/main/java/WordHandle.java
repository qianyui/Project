import com.google.common.collect.Lists;
import entity.ClassBasic;
import entity.EntityClass;
import org.apache.commons.collections4.CollectionUtils;
import utils.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import utils.ForEachUtils;

import java.io.FileInputStream;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @Authoe: diudiu
 * @Date: 2020/3/14
 **/
public class WordHandle {

    /**
     * 根据路径地址读取word数据
     *
     * @param path
     * @return
     */
    public List<ClassBasic> wordRead(String path) {
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(path));
            List<IBodyElement> elements = document.getBodyElements();
            List<ClassBasic> basicList = Lists.newArrayList();
            for (int i = 0; i < elements.size(); i++) {
                IBodyElement iBodyElement = elements.get(i);
                if (iBodyElement instanceof XWPFParagraph) {
                    if (i + 1 < elements.size()) {
                        IBodyElement newElement = elements.get(i + 1);
                        if (newElement instanceof XWPFTable) {
                            ClassBasic basic = new ClassBasic();
                            basic = getParagraphText((XWPFParagraph) iBodyElement);
                            List<EntityClass> entityClassList = Lists.newArrayList();
                            entityClassList = getXWPFTable((XWPFTable) newElement);
                            basic.setEntityClassList(entityClassList);
                            basicList.add(basic);
                        }
                    }
                }
            }
            return basicList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 表名
     *
     * @param xwpfParagraph
     * @return
     */
    private ClassBasic getParagraphText(XWPFParagraph xwpfParagraph) {
        ClassBasic basic = new ClassBasic();
        //  获取段落中的内容
        StringBuffer sb = new StringBuffer();
        List<XWPFRun> runs = xwpfParagraph.getRuns();
        if (CollectionUtils.isNotEmpty(runs)) {
            runs.forEach(r -> sb.append(r));
        }
        String str = sb.toString().trim();
        String className = "";
        String tblName = "";
        //  不管是什么括号，统一转换成中文括号
        str = str.replaceAll("\\(", "（");
        str = str.replaceAll("\\)", "）");
        if (StringUtils.contains(str, "（") && StringUtils.contains(str, "）")) {
            //  截取（）之间的值  表名
            tblName = StringUtils.subString(str, "（", "）").trim();
            //  类名  表名一般都是tbl_xxxxx ,所以要先去掉tbl_
            className = tblName.toLowerCase().replace("tbl", "").trim();
            className = StringUtils.replaceUnderlineAndfirstToUpper(className, "_", "");
            basic.setTblName(tblName.trim());
            basic.setClassName(className.trim());
            basic.setRemark(StringUtils.subString(str, "（"));
        }
        return basic;
    }

    /**
     * 读取表格数据
     *
     * @param xwpfTable
     * @return
     */
    private List<EntityClass> getXWPFTable(XWPFTable xwpfTable) {
        List<EntityClass> entityClassList = Lists.newArrayList();
        StringBuffer sb = new StringBuffer();
        List<XWPFTableRow> rows = xwpfTable.getRows();
        rows.forEach(ForEachUtils.consumerWithIndex((row, rowIndex) -> {
            //  第一行默认是说明，需跳过
            if (rowIndex == 0) {
                return;
            }
            if (CollectionUtils.isNotEmpty(row.getTableCells())) {
                EntityClass entityClass = new EntityClass();
                if (row.getTableCells().size() <= 6) {
                    row.getTableCells().forEach(ForEachUtils.consumerWithIndex((xwpfTableCell, index) -> {
                        if (isNumeric(xwpfTableCell.getText())) {
                            return;
                        }
                        switch (index) {
                            case 0:
                                //  序号
                                return;
                            case 1:
                                //  备注
                                entityClass.setFieldRemark(xwpfTableCell.getText().trim());
                                return;
                            case 2:
                                //  字段名称
                                String fieldName = StringUtils.replaceUnderlineAndfirstToUpper(xwpfTableCell.getText().trim().toLowerCase(), "_", "");
                                entityClass.setFieldName(fieldName);
                                entityClass.setFieldNames(StringUtils.upperCase(fieldName));
                                entityClass.setDbFieldName(xwpfTableCell.getText().trim());
                                return;
                            case 3:
                                //  字段长度跟类型
                                isContainLength(xwpfTableCell.getText(), entityClass);
                                return;
                            case 4:
                                //  是否非空
                                entityClass.setFieldNull(true);
                                if (StringUtils.equals("非空", xwpfTableCell.getText())) {
                                    entityClass.setFieldNull(false);
                                }
                                return;
                            case 5:
                                //  说明
                                return;
                            default:
                                return;
                        }
                    }));
                    entityClassList.add(entityClass);
                }
            }
        }));
        return entityClassList;
    }

    /**
     * 判断传入的字符串是否是数字
     * false 否
     * true 是
     *
     * @param str
     * @return
     */
    private boolean isNumeric(String str) {
        if (StringUtils.isNotBlank(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断字符串类型
     *
     * @param str
     * @param entityClass
     */
    private void isContainLength(String str, EntityClass entityClass) {
        List<String> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            entityClass.setDbFieldType(str.trim());
            //  判断是否包含括号  判断字段长度
            boolean flag = false;
            String length = "";
            if (StringUtils.contains(str, "(") && StringUtils.contains(str, ")")) {
                flag = true;
                //  截取（）之间的值
                length = StringUtils.subString(str, "(", ")");
                //str = str.substring(0, str.indexOf("("));
                //  判断长度之间是否包含  ，   有，则代表者该类型可能是number 或者decimal
                if (length.indexOf(",") > 0) {
                    length = StringUtils.subString(str, "(", ",");
                }
                entityClass.setFieldLength(length.trim());
            } else if (str.toLowerCase().contains("varchar")) {
                //  默认200
                length = "200";
                entityClass.setDbFieldType(str.trim() + "(" + length + ")");
                entityClass.setFieldLength(length.trim());
            }
            //  字段类型转换成java类型
            entityClass.setFieldType(StringUtils.convertFieldType(str));
        }
    }

}
