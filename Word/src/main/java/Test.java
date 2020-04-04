import com.google.common.collect.Maps;
import entity.ClassBasic;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @Authoe: diudiu
 * @Date: 2020/3/14
 **/
public class Test {

    public static void main(String[] args) throws IOException {
        WordHandle word = new WordHandle();
        //  word模型地址
        String wordPath = "xxxx";
        //  freemarker 模板地址
        String freemarkerPath = "xxx";
        //  生成文件地址
        String filePath = "xxx";
        //  先获取到所有的表数据
        List<ClassBasic> list = word.wordRead(wordPath);
        //  使用freemark模板生成数据
        //  创建数据模型
        Map<String, Object> map = Maps.newHashMap();
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        map.put("date", format.format(new Date()));
        map.put("name", "diudiu");
        // step2 获取模版路径
        configuration.setDirectoryForTemplateLoading(new File(freemarkerPath));
        list.forEach(basic -> {
            map.put("basic", basic);
            File javaFile = new File(filePath + "\\" + basic.getClassName() + ".java");
            Writer out = null;
            try {
                // step3 加载模版文件
                Template template = configuration.getTemplate("Class.ftl");
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javaFile)));
                // step4 生成数据
                template.process(map, out);
                // step3 加载模版文件
                javaFile = new File(filePath + "\\" + basic.getClassName() + ".sql");
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javaFile)));
                template = configuration.getTemplate("sql.ftl");
                // step4 生成数据
                template.process(map, out);
                System.out.println(basic.getClassName() + "  文件创建成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
