package mao.antisamy_demo.config;

import mao.antisamy_demo.converter.XssStringJsonDeserializer;
import mao.antisamy_demo.filter.XssFilter;
import mao.antisamy_demo.service.XssFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * Project name(项目名称)：antiSamy_demo
 * Package(包名): mao.antisamy_demo.config
 * Class(类名): AntiSamyConfig
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/10/30
 * Time(创建时间)： 13:53
 * Version(版本): 1.0
 * Description(描述)： 无
 */


@Configuration
public class AntiSamyConfig
{
    @Bean
    public FilterRegistrationBean<XssFilter> filterRegistrationBean()
    {
        FilterRegistrationBean<XssFilter> filterRegistration = new FilterRegistrationBean<>(new XssFilter());
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        filterRegistration.setName("xssFilter");
        filterRegistration.setOrder(1);

        return filterRegistration;
    }

    /**
     * 配置跨站攻击 反序列化处理器
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer2(@Autowired XssFilterService xssFilterService)
    {
        return builder -> builder.deserializerByType(String.class, new XssStringJsonDeserializer(xssFilterService));
    }

}
