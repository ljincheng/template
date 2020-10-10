package cn.booktable.template;


public interface ITemplateEngine {
	
	
	public  String process(final String template,  IContext context);
	
	
	public String process(final TemplateSpec templateSpec,IContext context);

}
