package cn.booktable.template.reader;

import cn.booktable.template.IEngineContext;
import cn.booktable.template.TemplateConfig;
import cn.booktable.template.TemplateException;
import cn.booktable.template.TemplateSpec;
import cn.booktable.template.standard.IStandardExpression;

import java.util.Map;

/**
 * @author ljc
 */
public class BlockReader {


    private final char[] mPrefix;
    private final char[] mSuffix;
    private final char mP0;
    private final char mS0;
    private final char[] mPrefix_if;
    private final char[] mSuffix_if;
    private final char[] mPrefix_each;
    private final char[] mSuffix_each;
    public static final String HANDLE_IF="if";
    public static final String HANDLE_EACH="each";
    public static final String HANDLE_DEFAULT="default";
    private IEngineContext mContext;
    private TemplateConfig mConfig;

    public BlockReader(TemplateConfig config, IEngineContext context) {
        super();
        this.mConfig=config;
        this.mContext=context;
        this.mPrefix=config.getPrefix().toCharArray();
        this.mSuffix=config.getSuffix().toCharArray();
        this.mP0=mPrefix[0];
        this.mS0=mSuffix[0];
        this.mPrefix_if=config.getPrefix().concat(HANDLE_IF).concat(":").toCharArray();
        this.mPrefix_each=config.getPrefix().concat(HANDLE_EACH).concat(":").toCharArray();
        this.mSuffix_if=config.getPrefix().concat("endIf").concat(config.getSuffix()).toCharArray();
        this.mSuffix_each=config.getPrefix().concat("endEach").concat(config.getSuffix()).toCharArray();
    }


    public void read(final String template,StringBuffer out)
    {
        int cursor=0;
        int size=template.length();
        while(cursor<size){
            char c=template.charAt(cursor);
            if(c==mP0 && matchPrefix(template,mPrefix,cursor)) {
                int endIndex=read(template,mPrefix,mSuffix,cursor+mPrefix.length,size);
                if(endIndex>=0)
                {
                    String context = template.substring(cursor, endIndex);
                    if(matchPrefix(context,mPrefix_if,0))
                    {
                        endIndex=read(template,mPrefix_if,mSuffix_if,cursor+mPrefix_if.length,size);
                        context = template.substring(cursor, endIndex);
                        IStandardExpression expression= mConfig.getExpressionHandle().get(HANDLE_IF);
                        expression.execute(mConfig,out,new TemplateSpec(context),mContext);

                        System.out.println("找到模板参数：" + context);
                    }else if(matchPrefix(context,mPrefix_each,0))
                    {
                        endIndex=read(template,mPrefix_each,mSuffix_each,cursor+mPrefix_each.length,size);
                        context = template.substring(cursor, endIndex);
                        IStandardExpression expression=  mConfig.getExpressionHandle().get(HANDLE_EACH);
                        expression.execute(mConfig,out,new TemplateSpec(context),mContext);
                        System.out.println("找到模板参数：" + context);
                    }else{
                        IStandardExpression expression=  mConfig.getExpressionHandle().get(HANDLE_DEFAULT);
                        expression.execute(mConfig,out,new TemplateSpec(context),mContext);
                        System.out.println("找到模板参数：" + context);
                    }
                    cursor = endIndex;
                }else {
                    out.append(c);
                    cursor++;
                }
            }else{
                out.append(c);
                cursor++;
            }
        }
    }

    public void read(final String template,final char[] prefix,final char[] suffix,StringBuffer tagOut,StringBuffer contentOut)
    {

    }

    private int read(String template,final char[] prefix,final char[] suffix, final int off, final int len){
        int index=off;
        int insideMatch=0;
        while (index<len) {
            char c=template.charAt(index);
            if(c==prefix[0]) {
                if(matchPrefix(template,prefix,index)){
                  insideMatch++;
                }else if(prefix[0]==suffix[0]){
                    if(matchSuffix(template,suffix,index)) {
                        if (insideMatch > 0) {
                            insideMatch--;
                        } else {
                            return index + suffix.length;
                        }
                    }
                }
            }else if(c==suffix[0]) {
                if(matchSuffix(template,suffix,index)) {
                    if (insideMatch > 0) {
                        insideMatch--;
                    } else {
                        return index + suffix.length;
                    }
                }
            }
            index++;
        }
        return -1;
    }

    public static boolean matchPrefix(String template,final char[] prefix,int startIndex){
        int nextLength=startIndex+prefix.length;
        if(nextLength>template.length())
        {
            return false;
        }
        int index=1;
        while(index<prefix.length) {
            char prefix_c=template.charAt(startIndex+index);
            if(prefix_c!=prefix[index]) {
                return false;
            }
            index++;
        }
        return true;
    }

    public static boolean matchSuffix(String template,final char[] suffix,int startIndex){
        int nextLength=startIndex+suffix.length;
        if(nextLength>template.length())
        {
            return false;
        }
        int index=1;
        while(index<suffix.length) {
            char prefix_c=template.charAt(startIndex+index);
                if (prefix_c != suffix[index]) {
                    return false;
                }
            index++;
        }
        return true;
    }

}
