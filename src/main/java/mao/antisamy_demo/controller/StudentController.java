package mao.antisamy_demo.controller;

import mao.antisamy_demo.entity.Student;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Project name(项目名称)：antiSamy_demo
 * Package(包名): mao.antisamy_demo.controller
 * Class(类名): StudentController
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/10/29
 * Time(创建时间)： 20:05
 * Version(版本): 1.0
 * Description(描述)： 无
 */

@RestController
@RequestMapping("/student")
public class StudentController
{
    private static final List<Student> list = Collections.synchronizedList(new ArrayList<>());

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @PostMapping("/init")
    public synchronized void init()
    {
        Student student1 = new Student(10001, "张三", "男", 18);
        Student student2 = new Student(10002, "李四", "女", 16);
        Student student3 = new Student(10003, "王五", "男", 20);
        list.clear();
        list.add(student1);
        list.add(student2);
        list.add(student3);
        log.info("初始化完成");
    }

    @PostMapping
    public boolean save(@RequestBody Student student)
    {
        list.add(student);
        log.info("添加成功：\n" + student);
        return true;
    }

    @GetMapping
    public List<Student> getAll()
    {
        log.info("查询所有：\n" + list);
        return list;
    }

}
