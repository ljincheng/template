package cn.booktable.template;

/**
 * @author ljc
 */
public class TemplateException extends RuntimeException{


    public TemplateException(String msg)
    {
        super(msg);
    }

    public TemplateException(Throwable throwable)
    {
        super(throwable);

    }

    public TemplateException(String msg,Throwable throwable)
    {
        super(msg,throwable);

    }

}
