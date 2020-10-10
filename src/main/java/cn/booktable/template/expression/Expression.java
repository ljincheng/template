package cn.booktable.template.expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.booktable.template.ExpressionException;
import cn.booktable.template.ExpresssionReader;
import cn.booktable.template.IEngineContext;
import cn.booktable.template.IExpression;
import cn.booktable.template.utils.TextUtils;

public class Expression implements IExpression{
	
  private String getExpression(String template)
  {
	  char fristChar=template.charAt(0);
	  if(fristChar=='(')
	  {
		  int size=template.length();
		  char lastChar=template.charAt(size-1);
		  if(lastChar!=')')
		  {
			  throw new ExpressionException("表达式错误");
		  }
		  return template.substring(1,size-1);
	  }
	  return template;
  }
  
  public static Object getValue(String template,IEngineContext context) 
  {
	  Expression expression=new Expression();
	  return expression.execute(template, context);
  }
	
	public Object execute(String template,IEngineContext context)
	{
		if(template==null)
		{
			return null;
		} 
		 ExpresssionReader read=new ExpresssionReader(getExpression(template));
		 List<Object>  expressionList=new ArrayList<Object>();
		 Object value=null;
		 while(read.hasNext())
		  {
			 char c=read.atNext();
			  if(c == '\'' || c == '\"')
			  {
				  String word=read.next();
				  System.out.println("TEXT ="+word);
				  IExpression nextExpression=new TextExpression();
				  value= nextExpression.execute(word, context);
				  expressionList.add(value);
				  
			  }else if(c == '(')
			  {
				  String word=read.next();
				  System.out.println("EXP ="+word);
				  IExpression nextExpression=new Expression();
				   value=nextExpression.execute(word, context);
				  expressionList.add(value);
			  }else if(c == '[')
			  {
				  String word=read.next();
				  System.out.println("ARRAY ="+word);
				  IExpression nextExpression=new ArrayExpression();
				  value=nextExpression.execute(word, context);
				  expressionList.add(value);
			  }else if(c == '{')
			  {
				  String word=read.next();
				  System.out.println("VAR ="+word);
				  IExpression nextExpression=new VariableExpression();
				  value=nextExpression.execute(word, context);
				  expressionList.add(value);
			  }else if(c == '=') {
				  read.getNext();
				  char nextc=read.atNext();
				  if(nextc == '=')
				  {
					  c=read.getNext();
					  System.out.println("EQUALS =");
					  Expression nextExpression=new Expression();
					  Object nextValue= nextExpression.execute(read.next(), context);
					  BinaryOperationExpression equalExpression=new EqualsExpression();
					  value= equalExpression.comparison(value, nextValue);
				  }else {
					  throw new ExpressionException("表达式错误");
				  }
			  }else if(c == '!') {
				   c=read.getNext();
				  char nextc=read.atNext();
				  if( nextc == '=')
				  {
					  c=read.getNext();
					  System.out.println("EXP !=");
					  Expression nextExpression=new Expression();
					  String word=read.next();
					  Object nextValue= nextExpression.execute(word, context);
					  BinaryOperationExpression equalExpression=new NotEqualsExpression();
					  value= equalExpression.comparison(value, nextValue);
				  }else {
					  throw new ExpressionException("表达式错误");
				  }
			  }else if(c == '<') {
				   c=read.getNext();
				  char nextc=read.atNext();
				  if(nextc == '=' )
				  {
					  nextc=read.getNext();
					  System.out.println("EXP <= ");
					  Expression nextExpression=new Expression();
					  Object nextValue= nextExpression.execute(read.next(), context);
					  BinaryOperationExpression bOExp=new LessOrEqualToExpression();
					  value= bOExp.comparison(value, nextValue);
				  }else if(nextc == '>' ) {
					  nextc=read.getNext();
					  System.out.println("EXP <> ");
					  Expression nextExpression=new Expression();
					  Object nextValue= nextExpression.execute(read.next(), context);
					  BinaryOperationExpression equalExpression=new NotEqualsExpression();
					  value= equalExpression.comparison(value, nextValue);
				  }else if(Character.isDigit(nextc) || nextc == '\'' || nextc == '\"'  || nextc == '(' ||nextc == '[' || nextc == '{' || read.isSpace()) {
					  System.out.println("EXP < ");
					  Expression nextExpression=new Expression();
					  Object nextValue= nextExpression.execute(read.next(), context);
					  BinaryOperationExpression equalExpression=new LessExpression();
					  value= equalExpression.comparison(value, nextValue);
				  }else {
					  throw new IllegalArgumentException("表达式错误");
				  }
			  }else if(c == '>') {
				   c=read.getNext();
				  char nextc=read.atNext();
				  if(nextc == '=' )
				  {
					  nextc=read.getNext();
					  System.out.println("EXP <= "); 
					  Expression nextExpression=new Expression();
					  Object nextValue= nextExpression.execute(read.next(), context);
					  BinaryOperationExpression equalExpression=new GreaterOrEqualToExpression();
					  value= equalExpression.comparison(value, nextValue);
				  }else if(Character.isDigit(nextc) || nextc == '\'' || nextc == '\"'  || nextc == '(' ||nextc == '[' || nextc == '{' || read.isSpace()) {
					  System.out.println("EXP > ");
					  Expression nextExpression=new Expression();
					  Object nextValue= nextExpression.execute(read.next(), context);
					  BinaryOperationExpression equalExpression=new GreaterExpression();
					  value= equalExpression.comparison(value, nextValue);
				  }else {
					  throw new IllegalArgumentException("表达式错误");
				  }
			  }else if(c == '?') {
				  c=read.getNext();
				  if(value!=null && (Boolean)value)
				  {
					  read.skipEmpty();
					  c=read.atNext();
					  if(c == '{')
					  {
						  IExpression nextExpression=new VariableExpression();
						  String word=read.next();
						  value= nextExpression.execute(word, context);
					  }else {
						  Expression nextExpression=new Expression();
						  String word=read.next();
						  value= nextExpression.execute(word, context);
					  }
					  read.skipEmpty();
					  char chooseChar=read.atNext(); 
					  if(chooseChar==':')
					  {
						  read.getNext();
						  if(read.hasNext())
						  {
							  read.next();
						  }
					  }
				  }else {
					 
					  read.skipNext();
					  read.skipEmpty();
					  char chooseChar=read.getNext();
					  System.out.println("chooseChar="+chooseChar); 
					  if(chooseChar!=':') throw new ExpressionException("表达式错误");

					 c= read.atNext();
//					  word=read.next();
//					  Expression nextExpression=new Expression(); 
//					  value= nextExpression.execute(word, context);
					 IExpression nextExpression=null;
					  if(c == '{')
					  {
						  nextExpression=new VariableExpression();
					  }else {
						  nextExpression=new Expression();
						  
					  }
					  value= nextExpression.execute(read.next(), context);
				  }
			  }else if('&'==c){
				  c=read.getNext();
				  char nextc=read.atNext();
				  if(nextc == '&' )
				  {
					  if(value!=null && value instanceof Boolean)
					  {
						  if((Boolean)value)
						  {
							  c=read.getNext();
							  Expression nextExpression=new Expression();
							  String word=read.next();
							  value= nextExpression.execute(word, context);
						  } else {
							  return false;
						  }
					  }else {
					  return false;
					  }
					  
				  }else {
					  throw new ExpressionException("表达式错误");
				  }
			  }else if('|'==c) {
				  c = read.getNext();
				  char nextc = read.atNext();
				  if (nextc == '|') {
					  if (value != null && value instanceof Boolean) {
						  if ((Boolean) value) {
							  return true;
						  }
					  }
					  c = read.getNext();
					  Expression nextExpression = new Expression();
					  String word = read.next();
					  value = nextExpression.execute(word, context);

				  } else {
					  throw new ExpressionException("表达式错误");
				  }
			  }else if('+' ==c ){
				  c = read.getNext();
				  Object part1Value=value;
				  Expression nextExpression = new Expression();
				  String word = read.next();
				  value = nextExpression.execute(word, context);
				  if(value==null || part1Value==null)
				  {
					  if(part1Value!=null ) {
						  value= part1Value;
					  }
				  }else if(TextUtils.isNumber(part1Value.toString()) && TextUtils.isNumber(value.toString()))
				  {
				  	value= new BigDecimal(part1Value.toString()).add(new BigDecimal(value.toString()));
				  }else {
				  	value = part1Value.toString().concat(value.toString());
				  }

			  }else {
				  String word=read.next();
				  IExpression nextExpression=new TextExpression();
				  value= nextExpression.execute(word, context);
//				  if("null".equals(word.toLowerCase()))
//				  {
//					  value=null;
//				  }else if(":".equals(word))
//				  { 
//					  read.getNext();
//					  if(read.hasNext())
//					  {
//						  value=read.next();
//					  }
//				  }else {
//					  value=word;
//				  }
				  System.out.println("ELSE ="+word);
			  }
		  }
		return value;
	}
	
	

}
