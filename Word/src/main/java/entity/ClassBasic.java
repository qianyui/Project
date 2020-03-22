package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @Authoe: diudiu
 * @Date: 2020/3/15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassBasic {

    //  表名
    private String tblName;

    //  字段
    private List<EntityClass> entityClassList;
}
