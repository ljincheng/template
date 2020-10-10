package cn.booktable.template;

public class ExpressionException extends TemplateException{
	
	
	public ExpressionException(String msg)
	{
		super(msg);
	}
	
	public ExpressionException(Throwable throwable)
	{
		super(throwable);
		
	}
	
	public ExpressionException(String msg,Throwable throwable)
	{
		super(msg,throwable);
		
	}

}
