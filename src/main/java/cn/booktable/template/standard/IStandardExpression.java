package cn.booktable.template.standard;

import cn.booktable.template.IEngineContext;
import cn.booktable.template.TemplateConfig;
import cn.booktable.template.TemplateSpec;

/**
 * @author ljc
 */
public interface IStandardExpression {

    char[] prefix();
    char[] suffix();

    Object execute(TemplateConfig config, StringBuffer out, TemplateSpec templateSpec, IEngineContext context);
}
