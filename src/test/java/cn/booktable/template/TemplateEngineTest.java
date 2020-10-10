package cn.booktable.template;

import org.junit.Test;

/**
 * @author ljc
 */
public class TemplateEngineTest {


    @Test
    public void testTemplateReader()
    {
        try {
            ContextTest contextTest = new ContextTest();

            String template = ContextTest.loadResource("example1");
            String result = TemplateEngine.process(template, contextTest.personContext());
            //System.out.println("RESULT:" + result);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
