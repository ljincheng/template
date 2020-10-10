package cn.booktable.template.expression;

import cn.booktable.template.IEngineContext;
import cn.booktable.template.IExpression;
import cn.booktable.template.TemplateData;

public class VariableExpression implements IExpression{

	@Override
	public Object execute(String template, IEngineContext context) {
		char first=template.charAt(0);
		if(first != '{') throw new IllegalArgumentException("表达式错误");
		  int size=template.length();
		  char last=template.charAt(size-1);
		  if(last != '}') throw new IllegalArgumentException("表达式错误");
		 String word=template.substring(1,size-1);
		return TemplateData.getVariable(word, context.getRoot());
	}
	
	

}
