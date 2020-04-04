<#if basic??>
    package ${basic.classPath!''}


    import javax.persistence.Column;
    import javax.persistence.Entity;
    import javax.persistence.Table;

    import org.hibernate.annotations.DynamicInsert;
    import org.hibernate.annotations.DynamicUpdate;
    import org.hibernate.validator.constraints.Length;

    /**
    * <p>
    * ${basic.remark}
    * </p>
    *
    * @author ${name}
    * CreateDate:${date}
    */

    @Entity
    @DynamicInsert
    @DynamicUpdate
    @Table(name = "${basic.tblName}")
    public class ${basic.className} {

 <#list basic.entityClassList as entity >
     private ${entity.fieldType} ${entity.fieldName};

 </#list>

    <#list basic.entityClassList as entity >

        /**
        * 获取 ${entity.fieldRemark}
        *
        * @return ${entity.fieldName} ${entity.fieldRemark}
        */
        @Column(name = "${entity.dbFieldName}" <#if (entity.fieldLength)??> , length = ${entity.fieldLength} </#if>)
        <#if (entity.fieldType) == 'Date'>
        @Temporal(TemporalType.TIMESTAMP)
        </#if>
        public ${entity.fieldType} get${entity.fieldNames}(){
            return this.${entity.fieldName};
        }

        /**
        * 设置 ${entity.fieldRemark}
        *
        * @param ${entity.fieldName} ${entity.fieldRemark}
        */
        public void set${entity.fieldNames}(${entity.fieldType} ${entity.fieldName}) {
            this.${entity.fieldName} = ${entity.fieldName};
        }
    </#list>

    }
</#if>