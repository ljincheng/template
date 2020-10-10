package cn.booktable.template;

import cn.booktable.template.data.PersonDo;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ljc
 */
public class ContextTest {

    private static final String TEMPLATE_DIR = "/template/";


    public static PersonDo newPersion()
    {
        PersonDo personDo=new PersonDo();
        personDo.setAge(30);
        personDo.setName("Ann");
        personDo.setGender("Male");
        personDo.setPhone("086+139091085990");
        return personDo;
    }

    public static List<PersonDo> personList()
    {
        List<PersonDo> list=new ArrayList<PersonDo>();
        for(int i=0,k=10;i<k;i++)
        {
            PersonDo personDo=newPersion();
            personDo.setName(personDo.getName()+":"+i);
            personDo.setAge(personDo.getAge()+i);
            list.add(personDo);
        }
        return list;
    }

    public static String readToString(Reader is) throws IOException {
        StringBuilder result = new StringBuilder(4000);
        char[] buffer = new char[4000];
        for (; ; ) {
            int count = is.read(buffer);
            if (count <= 0) {
                break;
            }
            result.append(buffer, 0, count);
        }
        return result.toString();
    }
    public static String loadResource(String templateName) {
        String resourceName = TEMPLATE_DIR + templateName + ".txt";

        System.out.println("###resourceName="+resourceName);

        InputStream in = ContextTest.class.getResourceAsStream(resourceName);

        if (in == null) {
            return null;
        }
        try {
            try (InputStreamReader isr = new InputStreamReader(in)) {
                String viewTemplate = readToString(isr);
                return viewTemplate;
            } finally {
                try {
                    in.close();
                }
                catch (IOException e) {
                  e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;

    }

    @Test
    public void loadTemplateFile()
    {

        String context=loadResource("example1");
        System.out.println("CONTENT:"+context);
    }

    public static IContext personContext()
    {
        TemplateContext templateContext=new TemplateContext();
        templateContext.setVariable("me",newPersion());
        templateContext.setVariable("team",personList());
        return templateContext;

    }

    public static IEngineContext testContext()
    {
        TemplateContext templateContext=new TemplateContext();
        templateContext.setVariable("me",newPersion());
        templateContext.setVariable("team",personList());
        return templateContext;

    }
}
