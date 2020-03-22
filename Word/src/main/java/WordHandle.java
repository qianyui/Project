import com.google.common.collect.Lists;
import entity.ClassBasic;
import entity.EntityClass;
import enums.DbFieldType;
import org.apache.commons.collections4.CollectionUtils;
import utils.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import utils.ForEachUtils;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.Iterator;
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

    public void wordRead(String path) {
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
                            basic.setTblName(getParagraphText((XWPFParagraph) iBodyElement));
                            List<EntityClass> entityClassList = Lists.newArrayList();

                            getXWPFTable((XWPFTable) newElement);
                            basic.setEntityClassList(entityClassList);
                        }
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(basicList)){
                basicList.forEach(classBasic -> {
                    System.out.println(classBasic.getTblName());
                    classBasic.getEntityClassList().forEach(entityClass -> {
                        System.out.print(entityClass.getFieldName() + "\t");
                        System.out.print(entityClass.getFieldType() + "\t");
                        System.out.print(entityClass.getFieldLength() + "\t");
                        System.out.print(entityClass.getDbFieldType() + "\t");
                        System.out.print(entityClass.getFieldRemark() + "\t");
                        System.out.print(entityClass.isNull() + "\t");
                        System.out.println(entityClass.getDbFieldType());
                    });
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getParagraphText(XWPFParagraph xwpfParagraph) {
        //  获取段落中的内容
        StringBuffer sb = new StringBuffer();
        List<XWPFRun> runs = xwpfParagraph.getRuns();
        if (CollectionUtils.isNotEmpty(runs)) {
            runs.forEach(r -> sb.append(r));
        }
        //System.out.println( sb.toString());
        return sb.toString();
    }

    private List<EntityClass> getXWPFTable(XWPFTable xwpfTable) {
        List<EntityClass> entityClassList = Lists.newArrayList();
        StringBuffer sb = new StringBuffer();
        List<XWPFTableRow> rows = xwpfTable.getRows();
        rows.forEach(
                (row) -> {
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
                                        entityClass.setFieldRemark(xwpfTableCell.getText());
                                        return;
                                    case 2:
                                        //  字段名称
                                        entityClass.setFieldName(xwpfTableCell.getText());
                                        return;
                                    case 3:
                                        //  字段长度跟类型
                                        isContainLength(xwpfTableCell.getText(),entityClass);
                                        return;
                                    case 4:
                                        //  是否非空
                                        entityClass.setNull(true);
                                        if (StringUtils.equals("非空", xwpfTableCell.getText())) {
                                            entityClass.setNull(false);
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
                }
        );
        return entityClassList;
    }

    public void testWordRead(String path) {
        if (StringUtils.isNotBlank(path)) {
            try {
                FileInputStream in = new FileInputStream(path);
                if (path.toLowerCase().endsWith(".docx")) {
                    XWPFDocument xwpf = new XWPFDocument(in);
                    xwpf.getTablesIterator().forEachRemaining(
                            (it) -> {
                                it.getRows().forEach((row) -> {
                                    row.getTableCells().forEach(cell -> System.out.print(cell.getText() + "\t"));
                                });
                            }
                    );
                    in.close();
                } else if (path.toLowerCase().endsWith(".doc")) {

                }
            } catch (Exception e) {

            }
        }
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
     * @param str
     * @param entityClass
     */
    private void isContainLength(String str, EntityClass entityClass) {
        List<String> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            //  判断是否包含括号
            boolean flag = false;
            String length = "";
            String fieldType = "";
            if (StringUtils.contains(str, "(") && StringUtils.contains(str, ")")) {
                flag = true;
                fieldType = str.substring(0, str.indexOf("("));
                //  截取（）之间的值
                length = StringUtils.subString(str, "(", ")");
                //  判断长度之间是否包含  ，   有，则代表者该类型可能是number
                if (length.indexOf(",") > 0) {
                    entityClass.setDbFieldType(DbFieldType.NUMBER.name());
                    length = StringUtils.subString(str, "(", ",");
                }
            } else {
                fieldType = str;
            }
            entityClass.setDbFieldType(str);
            //  如果varchar2没有赋值，那就默认长度为200
            if(StringUtils.equals(fieldType.toLowerCase(),DbFieldType.VARCHAR2.name())){
                if (StringUtils.isBlank(length)) {
                    //  默认200
                    length = "200";
                    entityClass.setDbFieldType(str + "(" + length + ")");
                }
            }
            entityClass.setFieldType(fieldType);
            entityClass.setFieldLength(length);
        }
    }

}
