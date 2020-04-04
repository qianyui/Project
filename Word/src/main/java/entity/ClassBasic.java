package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 类
 * </p>
 *
 * @Authoe: diudiu
 * @Date: 2020/3/15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassBasic {

    //  类地址
    private String classPath;

    //  表名
    private String tblName;

    //  类名
    private String className;

    //  备注
    private String remark;

    //  字段
    private List<EntityClass> entityClassList;


}
