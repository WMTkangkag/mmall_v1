package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by txk on 2018/6/5.
 */
public class FTPUtil {

    private static final Logger logger=LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp=PropertiesUtil.getProperty("ftp.server.ip");

    private static String ftpUser=PropertiesUtil.getProperty("ftp.user");

    private static String ftpPass=PropertiesUtil.getProperty("ftp.pass");

    private String ip;

    private int port;

    private String user;

    private String pwd;

    private  FTPClient ftpClient;

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {

        FTPUtil ftpUtil=new FTPUtil(ftpIp,21,ftpUser,ftpPass);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接ftp服务器，结束文件上传，上传结果:{}",result);
        return result;
    }


    /**
     * 上传文件方法
     * @param remotePath
     * @param fileList
     * @return
     * @throws IOException
     */
    private  boolean uploadFile(String remotePath,List<File> fileList) throws IOException {

        boolean uploadFile=true;
        FileInputStream in=null;
        //判断服务器是否连接正常
        if(connectServer(this.ip,this.port,this.user,this.pwd)){
            //开始上传文件准备
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                //开始上传文件
                for (File fileItem:fileList) {
                    in= new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),in);
                }
            } catch (IOException e) {

                logger.info("上传文件异常",e);
                uploadFile=false;
                e.printStackTrace();

            }finally {
                in.close();
                ftpClient.disconnect();
            }
        }else {
            uploadFile=false;
        }
        return uploadFile;


    }

    /**
     *判断能否连接上服务器
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     */
    private  boolean connectServer(String ip,int port,String user,String pwd){

        boolean isSuccess=true;
        ftpClient=new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            isSuccess=false;
            logger.info("连接服务器异常",e);
        }
        return isSuccess;
    }


}
