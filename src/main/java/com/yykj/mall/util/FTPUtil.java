package com.yykj.mall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Lee on 2017/8/18.
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUsername = PropertiesUtil.getProperty("ftp.username");
    private static String ftpPassword = PropertiesUtil.getProperty("ftp.password");
    private static int port = 21;

    private FTPClient ftpClient;

    private FTPUtil(){

    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil();
        logger.info("开始连接FTP服务器");
        boolean result = ftpUtil.uploadFile("ftpfile", fileList);
        logger.info("上传结束，上传结果：{}", result);
        return result;
    }

    public boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if (connectServer()){
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                logger.error("上传文件异常！", e);
                uploaded = false;
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        } else {
            uploaded = false;
        }
        return uploaded;
    }

    public boolean connectServer(){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpIp, port);
            isSuccess = ftpClient.login(ftpUsername, ftpPassword);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常！", e);

        }
        return isSuccess;
    }
}
