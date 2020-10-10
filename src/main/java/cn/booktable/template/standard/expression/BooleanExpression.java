package cn.booktable.template.standard.expression;

import cn.booktable.template.IEngineContext;
import cn.booktable.template.TemplateConfig;
import cn.booktable.template.TemplateSpec;
import cn.booktable.template.expression.Expression;
import cn.booktable.template.reader.BlockReader;
import cn.booktable.template.reader.KeyValueReader;
import cn.booktable.template.standard.IStandardExpression;
import cn.booktable.template.utils.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ljc
 */
public class BooleanExpression implements IStandardExpression {
    private final char[] prefix={'<','@','i','f',':'};//<if when="" >
    private final char[] suffix={'<','@','e','n','d','I','f','@','>'};//</if>

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

        System.out.println("IF TAG:"+tagOut.toString());
        System.out.println("IF CONTNET:"+contentOut.toString());
        Boolean resultIf=false;// TODO  tag value
        if(tagOut.length()>0)
        {
            KeyValueReader keyValueReader=new KeyValueReader(tagOut.toString());
            Map<String, String> expMap=keyValueReader.toMap();
            String ifExp=expMap.get(BlockReader.HANDLE_IF);
                if(!TextUtils.isBlank(ifExp))
                {

                    System.out.println("IF EXP:"+ifExp);
                    Object value= Expression.getValue(ifExp, context);
                    if(value!=null && value instanceof Boolean)
                    {
                        resultIf=(Boolean)value;
                        System.out.println("IF EXP:"+ifExp+",value="+value);

                    }
                }
                System.out.println("IF EXP:"+ifExp);

        }
        if(resultIf!=null && resultIf.booleanValue() && contentOut.length()>0) {
            BlockReader reader = new BlockReader(config, context);
            reader.read(contentOut.toString(), out);
        }
       return contentOut.toString();
    }

    private void readKeyValue(String template,String prefix,String suffix,StringBuffer tagOut,StringBuffer contentOut)
    {
        int cursor=0;
        String ifSuffix=prefix.concat("endIf").concat(suffix);
        int suffixIndex=template.length()-ifSuffix.length();
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

