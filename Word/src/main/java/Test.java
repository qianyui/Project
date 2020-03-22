import com.google.common.collect.Lists;
import utils.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
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
public class Test {

    public static void main(String[] args) {
       /* Test test = new Test();
        String text = test.readWord("F:\\qq文档\\1194854511\\FileRecv\\三库一平台对接.docx");
        System.out.println(text);*/
        WordHandle word = new WordHandle();
        word.wordRead("F:\\qq文档\\1194854511\\FileRecv\\三库一平台对接.docx");

       // System.out.println(result);


    }

    private String readWord(String path) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(path)) {
            try {
                if (path.endsWith(".doc")) {
                    FileInputStream is = new FileInputStream(path);
                    WordExtractor ex = new WordExtractor(is);
                    sb.append(ex.getText());
                    is.close();
                } else if (path.endsWith(".docx")) {
                    OPCPackage opcPackage = POIXMLDocument.openPackage(path);
                    POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                    sb.append(extractor.getText());
                    opcPackage.close();
                } else {
                    System.out.println("不是word文件!!!");
                }
            } catch (Exception e) {

            }
        } else {
            System.out.println("地址为空");
        }
        return sb.toString();
    }
}
