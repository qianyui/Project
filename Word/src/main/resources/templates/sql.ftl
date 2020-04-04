create table ${basic.className}(
<#list basic.entityClassList as entity >
    ${entity.dbFieldName} ${entity.dbFieldType} <#if entity.fieldNull == false >not null</#if> <#if entity_index == 0>primary key</#if><#if entity_has_next>,</#if>
</#list>
);

comment on table ${basic.className} is '${basic.remark}';

<#list basic.entityClassList as entity >
comment on column ${basic.className}.${entity.dbFieldName} is '${entity.fieldRemark}';
</#list>
