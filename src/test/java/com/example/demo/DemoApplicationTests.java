package com.example.demo;

import com.example.demo.dto.CompareDto;
import com.example.demo.rpc.AutoWiredFactory;
import com.example.demo.rpc.FreamWork;
import com.example.demo.service.UserInfoService;
import com.example.demo.util.IoUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DemoApplicationTests {
    @Autowired
    private FreamWork freamWork;

    @Autowired
    private AutoWiredFactory autoWiredFactory;

    @Autowired
    private ApplicationContext applicationContext;


    @Test
    public void contextLoads()throws Exception{
        CompareDto compareDto = new CompareDto();
        compareDto.setType("2222");
        Object invoke = freamWork.methodInvoke("com.example.demo.rpc.FreamWork", "testRpc", compareDto);
        byte[] objectByte = IoUtils.getObjectByte(invoke);
        Object objectByByte = IoUtils.getObjectByByte(objectByte);
        System.out.println(objectByByte);
    }

    @Test
    public void cxt()throws Exception{
        UserInfoService bean = applicationContext.getBean(UserInfoService.class);
        log.info(bean.getCompareDto("2333").toString());
        log.info(bean.toString());


    }

}
