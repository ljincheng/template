package cn.booktable.template.standard.expression;

import cn.booktable.template.IEngineContext;
import cn.booktable.template.TemplateConfig;
import cn.booktable.template.TemplateSpec;
import cn.booktable.template.expression.Expression;
import cn.booktable.template.standard.IStandardExpression;
import cn.booktable.template.utils.TextUtils;

/**
 * @author ljc
 */
public class DefaultExpression implements IStandardExpression {
    private final char[] prefix={'<','{'};
    private final char[] suffix={'}','>'};

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
        //System.out.println("DEFAULT EXPRESSION:"+templateSpec.getTemplate());
        String template=templateSpec.getTemplate();
        if(!TextUtils.isBlank(template)){
            String content=template.substring(config.getPrefix().length(),template.length()-config.getSuffix().length());
            Object value= Expression.getValue(content, context);
            if(value!=null)
            {
                out.append(value);
            }
        }
        return null;
    }
}
