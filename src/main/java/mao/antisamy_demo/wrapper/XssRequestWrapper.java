package mao.antisamy_demo.wrapper;

import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;


/**
 * Project name(项目名称)：antiSamy_demo
 * Package(包名): mao.antisamy_demo.wrapper
 * Class(类名): XssRequestWrapper
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/10/30
 * Time(创建时间)： 13:46
 * Version(版本): 1.0
 * Description(描述)： 无
 */


public class XssRequestWrapper extends HttpServletRequestWrapper
{
    /**
     * 策略文件 需要将要使用的策略文件放到项目资源文件路径下
     */
    @SuppressWarnings("all")
    private static final String antiSamyPath = "antisamy-ebay.xml";

    public static Policy policy = null;

    private static final Logger log = LoggerFactory.getLogger(XssRequestWrapper.class);

    static
    {
        // 指定策略文件
        try
        {
            InputStream inputStream = XssRequestWrapper.class.getClassLoader().getResourceAsStream(antiSamyPath);
            assert inputStream != null;
            policy = Policy.getInstance(inputStream);
            log.info("加载antisamy-ebay.xml");
        }
        catch (PolicyException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * AntiSamy过滤数据
     *
     * @param taintedHTML 需要进行过滤的数据
     * @return 返回过滤后的数据
     */
    private String xssClean(String taintedHTML)
    {
        try
        {
            // 使用AntiSamy进行过滤
            AntiSamy antiSamy = new AntiSamy();
            CleanResults cleanResults = antiSamy.scan(taintedHTML, policy);
            taintedHTML = cleanResults.getCleanHTML();
        }
        catch (ScanException | PolicyException e)
        {
            e.printStackTrace();
        }
        return taintedHTML;
    }

    public XssRequestWrapper(HttpServletRequest request)
    {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name)
    {
        String[] values = super.getParameterValues(name);
        if (values == null)
        {
            return null;
        }
        int len = values.length;
        String[] newArray = new String[len];
        for (int j = 0; j < len; j++)
        {
            // 过滤清理
            newArray[j] = xssClean(values[j]);
            log.debug("Antisamy过滤清理，清理之前的参数值：" + values[j]);
            log.debug("Antisamy过滤清理，清理之后的参数值：" + newArray[j]);
        }
        return newArray;
    }

    @Override
    public String getParameter(String paramString)
    {
        String str = super.getParameter(paramString);
        if (str == null)
        {
            return null;
        }
        return xssClean(str);
    }


    @Override
    public String getHeader(String paramString)
    {
        String str = super.getHeader(paramString);
        if (str == null)
        {
            return null;
        }
        return xssClean(str);
    }

    @Override
    public Map<String, String[]> getParameterMap()
    {
        Map<String, String[]> requestMap = super.getParameterMap();
        for (Map.Entry<String, String[]> stringEntry : requestMap.entrySet())
        {
            String[] values = stringEntry.getValue();
            for (int i = 0; i < values.length; i++)
            {
                values[i] = xssClean(values[i]);
            }
        }
        return requestMap;
    }


}
