package cn.booktable.template;

import java.util.HashMap;
import java.util.Map;

import cn.booktable.template.expression.Expression;

public class ExpresssionReader {
	private String template;
	
	private int cursor=0;
	private int size=0;
	

	public ExpresssionReader(String template) {
		super();
		this.template = template;
		if(this.template!=null)
		{
			this.size=this.template.length();
		}
		this.cursor=0;
	}
	
	public boolean hasNext()
	{
		if(template==null || template.trim().length()==0)
		{
			return false;
		}
		skipEmpty();
		 if (cursor >=  size) {
			 return false;
		 }
	            
		return cursor<size;
	}
	
	public char atNext()
	{
		char c=this.template.charAt(cursor);
		return c;
	}
	
	public char getNext() {
		char c=this.template.charAt(cursor);
		if(cursor <  size)
		{
			cursor++;
		}
		return c;
	}
	
	public String next() {
		skipEmpty();
		StringBuilder result = new StringBuilder();
		char firstChar=getNext();
		if(firstChar == '"' || firstChar == '\''){  // '3232dsisf' or "232323sodwe" or "(232323)" or "aaa(23)bbb"
			result.append(firstChar);
			nextMatcher(firstChar,result);
		}else if(firstChar == '('){	// (23232323)
			result.append(firstChar);
			nextMatcher(')',result);
		}else if(firstChar == '{') {  // {123abc}
			result.append(firstChar);
			nextMatcher('}',result);
		}else if(firstChar == '[') {	//	[123,10,24]  ['1212','1212','1121212']
			result.append(firstChar);
			nextMatcher(']',result);
		}else if(firstChar==':' || firstChar== '?' || firstChar =='>' || firstChar=='=' || firstChar=='<' || firstChar=='>' || firstChar=='!' ) {
			result.append(firstChar);
			cursor++;
		}else {
			result.append(firstChar);
			nextTextMatcher(result);
		} 
        return result.toString();
	}
	
	
	
	private void nextMatcher(char endTag,StringBuilder result)
	{ 
		if (cursor ==  size)
            return;
		 while (cursor < size) {
	            char nextChar = this.template.charAt(cursor);
	            if (nextChar == '\\') {
	                cursor++;
	                if (cursor ==  size)
	                    throw new ExpressionException(
	                        "表达式错误，注意\\\\后面丢失特殊字符");
	                nextChar = this.template.charAt(cursor);
	                result.append(nextChar);
	                cursor++;
	            }else if(nextChar == endTag){
	            	if(' '!=endTag)
	            	{
	            		result.append(nextChar);
	            	}
	            	cursor++;
	            	break;
	            }else {
	            	result.append(nextChar);
	            	 cursor++;
	            }
	        }
	}
	
	private void nextTextMatcher(StringBuilder result)
	{ 
		if (cursor ==  size)
            return;
		 while (cursor < size) {
	            char nextChar = this.template.charAt(cursor);
	            if (nextChar == '\\') {
	                cursor++;
	                if (cursor ==  size)
	                    throw new ExpressionException(
	                        "表达式错误，注意\\后面丢失特殊字符");
	                nextChar = this.template.charAt(cursor);
	                result.append(nextChar);
	                cursor++;
	            }else if( !Character.isLetterOrDigit(nextChar)){
	            	break;
	            }else {
	            	result.append(nextChar);
	            	 cursor++;
	            }
	        }
	}
	
	  public boolean isSpace()
	    {
	    	if(cursor < size)
	    	{
	    		int c=this.template.charAt(cursor);
	    		return   32 == c;
	    	}
	    	return false;
	    }
	  
	  public void skipEmpty() {
	        while (isSpace()) {
	        	cursor++;
	        }
	       
	    }
	  public void skipNext() {
		if(hasNext())
		{
		   next();
		}
	  }
	  
	  
	  public static void main2(String[] args)
	  {
		  try {
			  //String template="'abc\"123(abc)'  ('123','abc') [1231] {abc.a} [121]";
			  String template="'abc'  != {abc.a}";
			  ExpresssionReader read=new ExpresssionReader(template);
			  while(read.hasNext())
			  {
				  char c=read.atNext();
				  if(c == '\'' || c == '\"')
				  {
					  String word=read.next();
					  //System.out.println("TEXT ="+word);
				  }else if(c == '(')
				  {
					  String word=read.next();
					  //System.out.println("EXP ="+word);
				  }else if(c == '[')
				  {
					  String word=read.next();
					  //System.out.println("ARRAY ="+word);
				  }else if(c == '{')
				  {
					  String word=read.next();
					  //System.out.println("VAR ="+word);
				  }else if(c == '=') {
					  read.getNext();
					  char nextc=read.getNext();
					  if(nextc == '=')
					  {
						  //System.out.println("EQUALS =");
					  }else {
						  throw new ExpressionException("表达式错误,注意 = 符号后面的字符");
					  }
				  }else if(c == '!') {
					   c=read.getNext();
					  char nextc=read.getNext();
					  if( nextc == '=')
					  {
						  //System.out.println("EXP !=");
					  }else {
						  throw new ExpressionException("表达式错误，注意 ! 符号后面的字符");
					  }
				  }else if(c == '<') {
					   c=read.getNext();
					  char nextc=read.atNext();
					  if(nextc == '=' )
					  {
						  nextc=read.getNext();
						  //System.out.println("EXP <= ");
					  }else if(nextc == '>' ) {
						  nextc=read.getNext();
						  //System.out.println("EXP <> ");
					  }else if(Character.isDigit(nextc) || nextc == '\'' || nextc == '\"'  || nextc == '(' ||nextc == '[' || nextc == '{' || read.isSpace()) {
						  //System.out.println("EXP < ");
					  }else {
						  throw new ExpressionException("表达式错误,注意 < 符号后面的字符");
					  }
				  }else if(c == '>') {
					   c=read.getNext();
					  char nextc=read.atNext();
					  if(nextc == '=' )
					  {
						  nextc=read.getNext();
						  //System.out.println("EXP <= ");
					  }else if(Character.isDigit(nextc) || nextc == '\'' || nextc == '\"'  || nextc == '(' ||nextc == '[' || nextc == '{' || read.isSpace()) {
						  //System.out.println("EXP > ");
					  }else {
						  throw new ExpressionException("表达式错误,注意 > 符号后面的字符");
					  }
				  }else {
					  String word=read.next();
					  //System.out.println("else ="+word);
				  }
				 
			  }
		  }catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  public static void main(String[] args)
	  {
		  try {
			  String template=" ({table.tableName}  != 'sys_user' ||  100==100 )?'abc':'123' " ;
			  Expression expression=new Expression();
			  EngineContext context=new EngineContext();
			  Map<String, Object> testMap=new HashMap<String, Object>();
			  testMap.put("tableName", "sys_user");
			  testMap.put("id", 200L);
			  context.setVariable("table", testMap);
			  context.setVariable("pid", 300L);
			  Object value=expression.execute(template, context);
			  //System.out.println("Last Value="+value);
			  
		  }catch (Exception e) {
			e.printStackTrace();
		}
	  }

}
