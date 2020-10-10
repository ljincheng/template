package cn.booktable.template;

import cn.booktable.template.reader.BlockReader;

import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;

public class TemplateEngine {

	public static String process(String template, IContext context) {
	
		return process(new TemplateSpec(template), context);
	}

	public static String process(TemplateSpec templateSpec, IContext context) {
		//Writer stringWriter = new FastStringWriter(100);
		StringBuffer writer=new StringBuffer();
		if(context instanceof IEngineContext)
		{
			process((IEngineContext)context,templateSpec, writer);
		}else {
			IEngineContext engineContext=new EngineContext();
			if(context!=null)
			{
				engineContext.setVariables(context.getRoot());
			}
			process(engineContext,templateSpec, writer);
		}
		return writer.toString();
	}

	private static void process(  final IEngineContext context,TemplateSpec templateSpec,  StringBuffer writer)
	{
		TemplateConfig config=new TemplateConfig();
		config.setPrefix("<@");
		config.setSuffix("@>");
		BlockReader reader=new BlockReader(config,context);
		reader.read(templateSpec.getTemplate(),writer);
	}
	
	

}
