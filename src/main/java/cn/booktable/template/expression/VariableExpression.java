package cn.booktable.template.expression;

import cn.booktable.template.ExpressionException;
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
		 if(word.indexOf('{')>0 && word.indexOf('}')>0)
		 {
			word=readTemplate(word,context);
		 }
		return TemplateData.getVariable(word, context.getRoot());
	}
	private String readTemplate(String template,IEngineContext context)
	{
		StringBuffer result=new StringBuffer();
		 int cursor=0;
		 int size=template.length();
		 StringBuffer varKeySB=new StringBuffer();
		 int insideNum=0;
		 while(cursor<size){
		 	char c=template.charAt(cursor);
		 	if(c=='{'){
		 		if(insideNum>0){
					throw new ExpressionException("未正常结束 { 符号");
				}
				insideNum++;
				cursor++;
		 		continue;
			}else if(c=='}'){
		 		if(insideNum<=0){
					throw new ExpressionException("未正常结束 } 符号");
				}
		 		insideNum--;
				Object subVarValue=TemplateData.getVariable(varKeySB.toString(), context.getRoot());
				result.append(subVarValue);
				varKeySB=new StringBuffer();
				cursor++;
				continue;
			}else if (c == '\\') {
				 cursor++;
				 if (cursor == size)
					 throw new ExpressionException(
							 "表达式错误，注意\\后面丢失特殊字符");
				 c = template.charAt(cursor);
			 }
		 	if(insideNum>0){
				varKeySB.append(c);
			}else {
				result.append(c);
			}
		 	cursor++;
		 }
		 return result.toString();
	}
	
	

}
