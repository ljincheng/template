package cn.booktable.template.standard.expression;

import cn.booktable.template.*;
import cn.booktable.template.expression.Expression;
import cn.booktable.template.expression.VariableExpression;
import cn.booktable.template.reader.BlockReader;
import cn.booktable.template.reader.KeyValueReader;
import cn.booktable.template.standard.IStandardExpression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ljc
 */
public class EachExpression implements IStandardExpression {

    private final char[] prefix={'<','e','a','c','h'};
    private final char[] suffix={'<','/','e','a','c','h','>'};

    @Override
    public char[] prefix() {
        return prefix;
    }

    @Override
    public char[] suffix() {
        return suffix;
    }
    @Override
    public Object execute(TemplateConfig config, StringBuffer out, TemplateSpec templateSpec, IEngineContext context) {
        String template=templateSpec.getTemplate();
        StringBuffer tagOut=new StringBuffer();
        StringBuffer contentOut=new StringBuffer();
        readKeyValue(template,config.getPrefix(),config.getSuffix(),tagOut,contentOut);
//以下过滤掉起始和尾部的换行符
while (contentOut.length()>0 && (contentOut.charAt(0)=='\r'|| contentOut.charAt(0)=='\n') )
{
    contentOut.deleteCharAt(0);
}
char lastChar=contentOut.length()>0 ? contentOut.charAt(contentOut.length()-1):'a';
while (contentOut.length()>0 && (lastChar=='\r' || lastChar=='\n'))
{
    contentOut.deleteCharAt(contentOut.length()-1);
    lastChar=contentOut.length()>0 ? contentOut.charAt(contentOut.length()-1):'a';
}
        //System.out.println("EACH TAG:"+tagOut.toString());
        //System.out.println("EACH CONTNET:"+contentOut.toString());

        KeyValueReader keyValueReader=new KeyValueReader(tagOut.toString());
        Map<String, String> expMap=keyValueReader.toMap();
        String eachValue= expMap.get(BlockReader.HANDLE_EACH);
        VariableExpression variableExp=new VariableExpression();
        Object iterableValue=variableExp.execute(eachValue,context);
        if(iterableValue!=null && iterableValue instanceof List) {
            List items=(List) iterableValue;
            String itemValue=expMap.get("item");
            String separator=expMap.get("separator");
            String filter=expMap.get("filter");
            //System.out.println("filter="+filter);
            boolean appendSeparator=false;
            boolean hasSeparator=false;
            if(separator!=null && separator.length()>0)
            {
                hasSeparator=true;
            }
            for(int i=0,k=items.size();i<k;i++) {
                context.setVariable(itemValue,items.get(i));
                if(hasSeparator)
                {
                    if(appendSeparator==false)
                    {
                        appendSeparator=true;
                    }else {
                        out.append(separator);
                    }

                }
                if (contentOut.length() > 0) {
                    if (filter != null && filter.length() > 0) {//有过滤条件情况下
                        Object value= Expression.getValue(filter, context);
                        if (value!=null && value instanceof Boolean ) {
                            if((Boolean)value) {
                                BlockReader reader = new BlockReader(config, context);
                                reader.read(contentOut.toString(), out);
                            }else{
                                appendSeparator=false;
                            }
                        }else{
                            appendSeparator=false;
                        }
                    }else{//没有过滤条件下
                        BlockReader reader = new BlockReader(config, context);
                        reader.read(contentOut.toString(), out);
                    }


                }
            }
            context.setVariable(itemValue,null);
        }
        return contentOut.toString();
    }



    private void readKeyValue(String template,String prefix,String suffix,StringBuffer tagOut,StringBuffer contentOut)
    {
        int cursor=0;
        String ifSuffix=prefix.concat("endEach").concat(suffix);
        int suffixIndex=template.length()-ifSuffix.length();
        char[] prefixArray=prefix.concat("each:").toCharArray();
        char[] suffixArray=suffix.toCharArray();
        String content=template.substring(prefix.length(),suffixIndex);
        int size=content.length();
        boolean goTag=true;
        while(cursor<size){
            char c=content.charAt(cursor);
            if(goTag)
            {
                if(c==suffix.charAt(0))
                {
                    if (BlockReader.matchSuffix(content, suffixArray, cursor)) {
                        goTag=false;
                        cursor+=suffixArray.length-1;
                    }else {
                        tagOut.append(c);
                    }
                }else{
                    tagOut.append(c);
                }
            }else {
                contentOut.append(c);
            }
            cursor++;
        }

    }
}
