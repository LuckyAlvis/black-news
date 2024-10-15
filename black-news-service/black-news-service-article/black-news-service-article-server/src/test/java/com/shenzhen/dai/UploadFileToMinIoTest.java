package com.shenzhen.dai;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shenzhen.dai.mapper.ApArticleContentMapper;
import com.shenzhen.dai.mapper.ApArticleMapper;
import com.shenzhen.dai.model.article.pojos.ApArticle;
import com.shenzhen.dai.model.article.pojos.ApArticleContent;
import com.shenzhen.dai.service.FileStorageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 上传文件到MinIO
 * @author: daiyifan
 * @create: 2024-10-12 10:49
 */
@SpringBootTest(classes = BlackNewsArticleApplication.class)
@RunWith(SpringRunner.class)
public class UploadFileToMinIoTest {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private Configuration configuration;

    /**
     * 上传css文件到MinIO
     */
    @Test
    public void uploadArticleCss() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:index.css");  // 根据文件路径
        InputStream inputStream = new FileInputStream(resource.getFile());
        fileStorageService.uploadFile("plugins/css/index.css", "index.css", "text/css", inputStream);
    }

    /**
     * 上传js文件到MinIO
     */
    @Test
    public void uploadArticleJs() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:index.js");  // 根据文件路径
        InputStream inputStream = new FileInputStream(resource.getFile());
        fileStorageService.uploadFile("plugins/js/index.js", "index.js", "text/js", inputStream);
    }

    /**
     * 上传axios.min.js文件到MinIO
     */
    @Test
    public void uploadAxiosMinJs() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:axios.min.js");  // 根据文件路径
        InputStream inputStream = new FileInputStream(resource.getFile());
        fileStorageService.uploadFile("plugins/js/axios.min.js", "axios.min.js", "text/js", inputStream);
    }

    /**
     * 上传生成的Html文件到MinIO
     */
    @Test
    public void uploadArticleHtml() throws TemplateException, IOException {
        // 获取文章详情
        List<ApArticleContent> apArticleContentList = apArticleContentMapper.selectList(Wrappers.lambdaQuery());
        for (ApArticleContent apArticleContent : apArticleContentList) {
            // 拿到freemarker模版
            Template template = configuration.getTemplate("article.ftl");
            // 数据模型
            Map<String, Object> content = new HashMap<>();
            content.put("content", JSONObject.parseArray(apArticleContent.getContent()));
            StringWriter stringWriter = new StringWriter();
            // 合成html文件
            template.process(content, stringWriter);
            InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", inputStream);
            System.out.println("path = " + path);
            // 把path落库
            apArticleMapper.update(Wrappers.<ApArticle>lambdaUpdate()
                    .eq(ApArticle::getId, apArticleContent.getArticleId())
                    .set(ApArticle::getStaticUrl, path));
        }
    }
}
