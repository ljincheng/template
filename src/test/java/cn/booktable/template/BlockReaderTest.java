package cn.booktable.template;

import cn.booktable.template.reader.BlockReader;
import cn.booktable.template.standard.IStandardExpression;
import cn.booktable.template.standard.expression.BooleanExpression;
import cn.booktable.template.standard.expression.DefaultExpression;
import cn.booktable.template.standard.expression.EachExpression;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljc
 */
public class BlockReaderTest {
    private final char[] prefix={'<','@'};
    private final char[] suffix={'@','>'};
//    private final char[] prefix={'<','i','f'};
//    private final char[] suffix={'<','/','i','f','>'};

    public Map<String, IStandardExpression> getExpressionMap(){
        Map<String, IStandardExpression> map=new HashMap<>();
        map.put(BlockReader.HANDLE_IF,new BooleanExpression());
        map.put(BlockReader.HANDLE_EACH,new EachExpression());
        map.put(BlockReader.HANDLE_DEFAULT,new DefaultExpression());
        return map;
    }

    @Test
    public void testRead()
    {
        try {
            ContextTest contextTest = new ContextTest();
            TemplateConfig config=new TemplateConfig();
            config.setPrefix("<@");
            config.setSuffix("@>");
            String template = ContextTest.loadResource("example1");

            IEngineContext context=ContextTest.testContext();
            BlockReader reader=new BlockReader(config,context);
            StringBuffer html=new StringBuffer();
            reader.read(template,html);
            //System.out.println("HTML:"+html);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
