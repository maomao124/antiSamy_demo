package mao.antisamy_demo;

import mao.antisamy_demo.wrapper.XssRequestWrapper;
import org.junit.jupiter.api.Test;
import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

@SpringBootTest
class AntiSamyDemoApplicationTests
{

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
        }
        catch (PolicyException e)
        {
            e.printStackTrace();
        }
    }


    @Test
    void contextLoads() throws ScanException, PolicyException
    {
        String xsshtml = "hyf<script>alert(1)</script>";
        AntiSamy antiSamy = new AntiSamy();
        CleanResults cleanResults = antiSamy.scan(xsshtml, policy);
        xsshtml = cleanResults.getCleanHTML(); //清洗完的
        System.out.println(xsshtml);
    }

}
