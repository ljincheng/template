package cn.booktable.template;

public interface IExpression {

	public Object execute(String template,IEngineContext context);
}
