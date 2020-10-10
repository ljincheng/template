package cn.booktable.template.reader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljc
 */
public class KeyValueReader {

    private  String template;
    private int cursor=0;
    private int size=0;
    private char tag=':';

    public KeyValueReader(String template)
    {
        super();
        this.template=template;
        cursor=0;
        size=0;
        if(template!=null && template.trim().length()>0)
        {
           this.size=template.length();
        }
    }

    public boolean hasNext(){
        if(cursor<size)
        {
            return template.substring(cursor).trim().length()>0;
        }
        return false;
    }

    public String nextKey(){
//        String key=null;
        StringBuffer key=new StringBuffer();
        skipSpace();

        char c=template.charAt(cursor);
        if(c=='\'' || c=='\"')
        {
           int endMatch=getMarkedSegment(key,c);
            if(endMatch!=1)
            {
                throw new IllegalArgumentException("未正常结束单或双引号");
            }
            cursor++;

        }else{
            getMarkedSegment(key,tag);
        }
        skipSpace();
        if(cursor>=size || template.charAt(cursor)!=tag)
        {
            throw new IllegalArgumentException("未找到键值分隔符");
        }
        cursor++;
        return key.toString().trim();
    }
    public String nextValue(){
        StringBuffer value=new StringBuffer();
        if (cursor < size) {

            skipSpace();
            char c=template.charAt(cursor);
            if(c=='\'' || c=='\"')
            {
                cursor++;
              int endMatch=  getMarkedSegment(value,c);

                if(endMatch!=1)
                {
                    throw new IllegalArgumentException("未正常结束单或双引号");
                }
               cursor++;
            }else{
                getMarkedSegmentEndBySpace(value);
            }
            skipSpace();
        }
        return value.toString();
    }

    public Map<String,String> toMap(){
        Map<String,String> map=new HashMap<>();
        cursor=0;
        while (hasNext())
        {
            map.put(nextKey(),nextValue());
        }
        return map;
    }

    public void skipSpace() {
        while (cursor < size) {
            char c=template.charAt(cursor);
            if(Character.isSpaceChar(c)) {
                cursor++;
            }else {
                break;
            }
        }

    }

    public int getMarkedSegment(StringBuffer out, char stopChar) {

        int successEnd=0;
        while (cursor < size) {
            char c=this.template.charAt(cursor);
            if (c == '\\') {
                cursor++;
                if (cursor ==  size)
                    throw new IllegalArgumentException("转换符未正确使用");
                c=template.charAt(cursor);
                out.append(c);
                cursor++;
            }else if(c==stopChar)
            {
                successEnd=1;
                break;
            }else {
                out.append(c);
            }
            cursor++;
        }
        return successEnd;

    }

    public int getMarkedSegmentEndBySpace(StringBuffer out) {
       int endSuccess=0;
        while (cursor < size) {
            char c=this.template.charAt(cursor);
            if (c == '\\') {
                cursor++;
                if (cursor ==  size)
                    throw new IllegalArgumentException("转换符未正确使用");
                c=template.charAt(cursor);
                out.append(c);
                cursor++;
            }else if(Character.isSpaceChar(c))
            {
                endSuccess=1;
                break;
            }else {
                out.append(c);
            }
            cursor++;
        }
        return endSuccess;
    }





}
