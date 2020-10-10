package cn.booktable.template;

import cn.booktable.template.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;


public class EngineContext implements IEngineContext{
	
	
	private HashMap<String,Object> maps;
	private Map<String, String> cacheTemplateFileMap;
	
	 

	
	public  EngineContext() {
		super();
		this.maps=new HashMap<String, Object>();
		this.cacheTemplateFileMap=new HashMap<String, String>();
		Map<String,String> systemMap=new HashMap<String, String>();
//		systemMap.put("lf", GeneralUtils.getDefaultLineSeparator());
		setVariable("system", systemMap);
		setVariable("text", new TextUtils());
	} 
 

	
	@Override
	public Object getVariable(String name) {
		
		return this.maps.get(name);
	}

	@Override
	public void setVariable(String name, Object value) {
		this.maps.put(name, value);
		
	}



	@Override
	public Map<String, Object> getRoot() {
		return this.maps;
	}



	@Override
	public void setVariables(Map<String, Object> variables) {
		if(variables!=null)
		{
			this.maps.putAll(variables);
		}
	}
	


	
	

}
