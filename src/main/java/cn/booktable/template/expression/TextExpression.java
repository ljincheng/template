package cn.booktable.template.expression;

import cn.booktable.template.IEngineContext;
import cn.booktable.template.IExpression;

public class TextExpression implements IExpression{
	 

	@Override
	public Object execute(String template, IEngineContext context) {
	 
		if(template!=null)
		{
			int size=template.length();
			if(size>0)
			{
				
				if("null".equals(template.toLowerCase()))
				{
					return null;
				}else{
					char first=template.charAt(0);
					char last=template.charAt(size-1);
					if((first == '\'' || first== '\"') && first==last)
					{
						String text=template.substring(1,size-1);
						return stringMatcher(text);
					}else {
						return template;
					}
					
				}
			}
		}
			
		return null;
	} 
	
	private String stringMatcher(String template)
	{
		int cursor=0;
		int size=template.length();
		StringBuilder result=new StringBuilder();
		 while (cursor < size) {
	            char nextChar = template.charAt(cursor);
	            if (nextChar == '\\') {
	                cursor++;
	                if (cursor ==  size)
	                    throw new IllegalArgumentException(
	                        "character to be escaped is missing");
	                nextChar = template.charAt(cursor);
	                result.append(nextChar);
	                cursor++;
	            
	            }else {
	            	result.append(nextChar);
	            	 cursor++;
	            }
	        }
		 return result.toString();
	}

}
