一．语法
模板标签语法： @@模板语言@@

1.	Standard Expression Syntax
1.1	Variables

示例：
 {table.tableName}
 {table == null ? "table.tableName" : 'abc'+(table.javaType == 'string'? 123 : "#$") }
 {table == null ? "table.tableName" : ('abc'+ table.javaType == 'string'? 123 : "#$") }
 {table == null ? "table.tableName" : ( 123 + table.id == 200 ? 123 : "#$") }
 {table?.javaType == null ? "table.tableName" : 'abc'+(table.javaType == 'string'? 123 : "#$") }



1.2	Utility Objects

示例：
 {#texts.isNumber('123123')}
 {#texts.isNumber('123123')?"yes":"no"}
 
2.	Conditional Evaluation
2.1	Simple conditionals: if

示例：
    @if{when:"" then:"" else:""}

2.2	Switch statements

示例：
    @switch{case:"1<100" then:"abc" case:"'abc' == 123 " then:"" else:""}
3.	Iteration
3.1	Each

示例：
    @each{list:table?.columns item:it status:status separator:"," text:"{it?.columnName}" }

3.2	Iteration status
4.	Template Layout

模板解析示例：
<{table?.tableName}>
<{table == null ? "table.tableName" : 'abc'+(table.javaType == 'string'? 123 : "#$") }>
<{#texts.isNumber('123123')?"yes":"no"}>
<{@if{when:{table?.columns.length>20} then:{table?.tableName+"表名"} else:"abc,还有\"数字\":123"}}>
<{@each{list:{table?.columns} item:it status:"status" separator:"," text:"{it?.columnName}" }}>

<{@if{when:{table?.columns.length>20} then:{table?.tableName+"表名:"+@each{list:table?.columns item:it text:{"姓名:"+it.realName+",性别:"+it.gender}} } else:"abc,还有\"数字\":123"}}>