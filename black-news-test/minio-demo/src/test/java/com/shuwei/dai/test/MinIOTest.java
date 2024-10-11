package com.shuwei.dai.test;

import com.shenzhen.dai.MinIOApplication;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @description:
 * @author: daiyifan
 * @create: 2024-10-11 15:43
 */
@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void test() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        Resource resource = resourceLoader.getResource("classpath:list.html");  // 根据文件路径
        InputStream inputStream = new FileInputStream(resource.getFile());
        // 1.获取MINIO连接信息，创建客户端
        MinioClient minioClient = MinioClient.builder()
                .credentials("Kjcacew7Jq5x5vk0UfEY", "UHrHzJADSIqZcj4eATNFMIkl2yA9grXsuXm3j8DA")
                .endpoint("http://localhost:9000").build();
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .object("list.html")
                .contentType("text/html; charset=utf-8")
                .bucket("black")
                .stream(inputStream, inputStream.available(), -1)
                .build();
        minioClient.putObject(putObjectArgs);
    }


}
