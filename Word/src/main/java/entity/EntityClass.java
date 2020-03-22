package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class EntityClass {

    //  实体类字段名
    private String fieldName;
    //  实体类字段类型
    private String fieldType;
    //  实体类字段长度
    private String fieldLength;
    //  实体类字段备注
    private String fieldRemark;

    //  数据库字段类型跟类型
    private String dbFieldType;

    //  是否非空
    private boolean isNull;
}
