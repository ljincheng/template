package cn.booktable.template.standard.expression;

import cn.booktable.template.*;
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

        System.out.println("EACH TAG:"+tagOut.toString());
        System.out.println("EACH CONTNET:"+contentOut.toString());

        KeyValueReader keyValueReader=new KeyValueReader(tagOut.toString());
        Map<String, String> expMap=keyValueReader.toMap();
        String eachValue= expMap.get(BlockReader.HANDLE_EACH);
        VariableExpression variableExp=new VariableExpression();
        Object iterableValue=variableExp.execute(eachValue,context);
        if(iterableValue!=null && iterableValue instanceof List) {
            List items=(List) iterableValue;
            String itemValue=expMap.get("item");
            for(int i=0,k=items.size();i<k;i++) {
                context.setVariable(itemValue,items.get(i));
                Boolean resultIf = true;// TODO  tag value
                if (resultIf != null && resultIf.booleanValue() && contentOut.length() > 0) {
                    BlockReader reader = new BlockReader(config, context);
                    reader.read(contentOut.toString(), out);
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
