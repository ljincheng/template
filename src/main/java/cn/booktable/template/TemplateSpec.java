package cn.booktable.template;


import cn.booktable.template.standard.IStandardExpression;
import cn.booktable.template.standard.expression.BooleanExpression;
import cn.booktable.template.standard.expression.EachExpression;

import java.util.*;

public final class TemplateSpec {
	
	 private  String template;

	 private List<IStandardExpression> expressionList;
	 private  Set<String> templateSelectors;
	 private Map<String,Object> templateVariable;
	 
	  
	 public TemplateSpec()
	 {
		 super();
		 templateVariable=new HashMap<String, Object>();
		 initExpression();
	 }
	 
	public TemplateSpec(String template) {
		this();
		this.template = template;
		
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}



	public Map<String, Object> getTemplateVariable() {
		return templateVariable;
	}

	public void setTemplateVariable(Map<String, Object> templateVariable) {
		this.templateVariable = templateVariable;
	}

	public List<IStandardExpression> getExpressionList() {
		return expressionList;
	}

	public void setExpressionList(List<IStandardExpression> expressionList) {
		this.expressionList = expressionList;
	}
	private void initExpression()
	{
		this.expressionList=new ArrayList<>();
		expressionList.add(new BooleanExpression());
		expressionList.add(new EachExpression());
	}
}
