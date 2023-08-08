package com.atguigu.crowd;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import com.aliyuncs.dysmsapi.model.v20170525.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.atguigu.crowd.util.CrowdUtil.uploadFileToOss;

/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.6.0</version>
</dependency>
*/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TT {
    @Test
    public void test1() {
        System.out.println(CrowdUtil.sendCodeByShortMessage("15505480391", "阿里云短信测试", "SMS_154950909"));
    }

    @Test
    public void test2() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("favicon.ico");
        ResultEntity<String> resultEntity = uploadFileToOss("http://oss-cn-qingdao.aliyuncs.com", "LTAI5tJdzYRmZcQVPoJAwB5d",
                "8gMgGvDjAXimVepotyWwrYQpjFKRKe",
                fileInputStream,
                "zzh-001",
                "http://zzh-001.oss-cn-qingdao.aliyuncs.com",
                "favicon.ico");
        System.out.println(resultEntity);
    }
}