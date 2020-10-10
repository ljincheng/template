package cn.booktable.template;

import cn.booktable.template.reader.BlockReader;
import cn.booktable.template.standard.IStandardExpression;
import cn.booktable.template.standard.expression.BooleanExpression;
import cn.booktable.template.standard.expression.DefaultExpression;
import cn.booktable.template.standard.expression.EachExpression;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljc
 */
public class TemplateConfig {
    private  String prefix=null;
    private  String suffix=null;
    private Map<String, IStandardExpression> expressionHandle;

    public TemplateConfig() {
        Map<String, IStandardExpression> map=new HashMap<>();
        map.put(BlockReader.HANDLE_IF,new BooleanExpression());
        map.put(BlockReader.HANDLE_EACH,new EachExpression());
        map.put(BlockReader.HANDLE_DEFAULT,new DefaultExpression());
        expressionHandle=map;
    }

    public String getPrefix() {
        if(prefix!=null) {
            return prefix;
        }else{
            return "<@";
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        if(suffix!=null) {
            return suffix;
        }else {
            return "@>";
        }
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Map<String, IStandardExpression> getExpressionHandle() {
        return expressionHandle;
    }

    public void setExpressionHandle(Map<String, IStandardExpression> expressionHandle) {
        this.expressionHandle = expressionHandle;
    }
}
