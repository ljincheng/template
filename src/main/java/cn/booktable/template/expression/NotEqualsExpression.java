package cn.booktable.template.expression;

import java.math.BigDecimal;
import java.util.Date;

import cn.booktable.template.IEngineContext;
import cn.booktable.template.utils.DateUtils;
import cn.booktable.template.utils.TextUtils;

public class NotEqualsExpression implements BinaryOperationExpression{

	@Override
	public Object execute(String template, IEngineContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean comparison(Object left, Object right) {
		return executeNotEquals(left,right);
	}
	
	public boolean executeNotEquals(Object left,Object right)
	{
		if(left==null ){
			if(right==null){
				return false;
			}
			return true;
		}else {
			if(right==null) {
				return true;
			}
			if(left instanceof BigDecimal )
			{
				BigDecimal left_decimal=(BigDecimal)left;
				BigDecimal right_decimal=BigDecimal.ZERO;
				if(right instanceof BigDecimal)
				{
					right_decimal=(BigDecimal)right;
				}else {
					right_decimal=new BigDecimal(right.toString());
				}
				return left_decimal.compareTo(right_decimal)!=0;
				
			}else if(left instanceof Date)
			{
				Date left_date=(Date)left;
				Date right_date=null;
				if(right instanceof Date)
				{
					right_date=(Date)right;
				}else {
					right_date= DateUtils.convert(right.toString());
				}
				return left_date.compareTo(right_date)!=0;
			}else {
				String left_string=String.valueOf(left);
				String right_string=right.toString();
				if(TextUtils.isNumber(left_string) && TextUtils.isNumber(right_string))
				{
					BigDecimal left_double=new BigDecimal(left_string);
					BigDecimal right_double=new BigDecimal(right_string);
					return left_double.compareTo(right_double)!=0;
				}else {
					return left_string.compareTo(right_string)!=0;
				}
				 
			}
		}
		 
	}
	
	

}
