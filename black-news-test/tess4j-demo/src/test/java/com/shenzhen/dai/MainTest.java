package com.shenzhen.dai;

import com.shenzhen.dai.service.FileStorageService;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@SpringBootTest(classes = Main.class)
@RunWith(SpringRunner.class)
public class MainTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 识别本地图片中的文字
     */
    @Test
    public void test1() throws IOException, TesseractException {
        String tessdata = Main.class.getClassLoader().getResource("tessdata").getPath();
        ITesseract instance = new Tesseract();
        instance.setDatapath(tessdata);
        instance.setLanguage("chi_sim");

        // 本地文件识别
        Resource resource = resourceLoader.getResource("classpath:ocr_img/test1.png");
        File file = resource.getFile();
        String text = instance.doOCR(file);
        System.out.println("text = " + text);
    }


    /**
     * 识别MinIO图片中的文字
     */
    @Test
    public void test2() throws IOException, TesseractException {
        String tessdata = Main.class.getClassLoader().getResource("tessdata").getPath();
        ITesseract instance = new Tesseract();
        instance.setDatapath(tessdata);
        instance.setLanguage("chi_sim");

        // 本地文件识别
//        Resource resource = resourceLoader.getResource("classpath:ocr_img/img1.jpg");
        // MinIO中图片识别
        byte[] bytes = fileStorageService.downLoadFile("http://localhost:9000/black/2024/10/14/222.png");
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        String text = instance.doOCR(bufferedImage);
        System.out.println("text = " + text);
    }
}
