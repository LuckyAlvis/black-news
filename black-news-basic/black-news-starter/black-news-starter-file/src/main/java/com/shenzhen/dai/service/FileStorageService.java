package com.shenzhen.dai.service;

import java.io.InputStream;

/**
 * @author itheima
 */
public interface FileStorageService {

    /**
     * 上传自定义格式文件
     *
     * @param filePath    文件路径
     * @param contentType 文件类型
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    String uploadFile(String filePath, String filename, String contentType, InputStream inputStream);

    /**
     * 上传图片文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    String uploadImgFile(String prefix, String filename, InputStream inputStream);

    /**
     * 上传html文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    String uploadHtmlFile(String prefix, String filename, InputStream inputStream);

    /**
     * 删除文件
     *
     * @param pathUrl 文件全路径
     */
    void delete(String pathUrl);

    /**
     * 下载文件
     *
     * @param pathUrl 文件全路径
     * @return
     */
    byte[] downLoadFile(String pathUrl);

}
