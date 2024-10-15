package com.shenzhen.dai.wemedia.service;

import com.shenzhen.dai.common.tess4j.Tess4jClient;
import com.shenzhen.dai.service.FileStorageService;
import com.shenzhen.dai.wemedia.BlackNewsWeMediaApplication;
import net.sourceforge.tess4j.TesseractException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @description: 文字识别测试
 * @author: daiyifan
 * @create: 2024-10-15 20:49
 */
@SpringBootTest(classes = BlackNewsWeMediaApplication.class)
@RunWith(SpringRunner.class)
public class Tess4jTest {

    @Autowired
    private Tess4jClient tess4jClient;

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void test() throws IOException, TesseractException {
        byte[] bytes = fileStorageService.downLoadFile("http://localhost:9000/black/2024/10/14/222.png");
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));

        String text = tess4jClient.doOCR(bufferedImage);
        System.out.println("text = " + text);
    }
}
