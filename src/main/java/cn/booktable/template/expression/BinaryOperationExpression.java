package cn.booktable.template.expression;

import cn.booktable.template.IExpression;

public interface BinaryOperationExpression extends IExpression{
	
	
	public boolean comparison(Object left ,Object right);
	

}
