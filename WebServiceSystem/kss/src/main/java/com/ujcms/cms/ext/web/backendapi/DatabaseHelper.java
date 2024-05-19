package com.ujcms.cms.ext.web.backendapi;

import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DatabaseHelper {
    final public static String BACKUP_DIR = "src/main/webapp/backup/";
    final public static String HOST = "localhost";
    final public static String PORT = "3306";
    final public static String DATABASE = "museum";
    final public static String USER = "admin";
    final public static String PASSWORD = "admin123";

    private String getNowTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return now.format(formatter);
    }   

    @Async
    public void quickBackup() throws Exception {
        String backupFileName = "mysql-" + getNowTime() + ".sql";
        backupDatabase(HOST, PORT, DATABASE, USER, PASSWORD, BACKUP_DIR + backupFileName);
    }

    @Async
    public void quickRestore(String backupFileName) throws Exception {
        restoreDatabase(HOST, PORT, DATABASE, USER, PASSWORD, BACKUP_DIR + backupFileName);
    }

    /**
     * 备份数据库到指定路径
     * 
     * @param host       数据库主机
     * @param port       数据库端口
     * @param database   要备份的数据库名
     * @param user       数据库用户名
     * @param password   数据库密码
     * @param backupPath 备份文件存放的路径，包括文件名
     * @throws Exception 如果备份失败
     */
    @Async
    public void backupDatabase(String host, String port, String database, String user, String password,
            String backupPath) throws Exception {
        Process process = null;
        try {
            String command = String.format("mysqldump -h%s -P%s -u%s -p%s %s -r \"%s\"",
                    host, port, user, password, database, backupPath);

            process = Runtime.getRuntime().exec(new String[] { "sh", "-c", command });
            int processComplete = process.waitFor();
            if (processComplete != 0) {
                throw new Exception("Could not create the database backup");
            }
        } catch (IOException | InterruptedException e) {
            throw new Exception("Failed to execute backup command: " + e.getMessage(), e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * 从指定路径恢复数据库
     * 
     * @param host       数据库主机
     * @param port       数据库端口
     * @param database   要恢复的数据库名
     * @param user       数据库用户名
     * @param password   数据库密码
     * @param sourcePath 恢复文件的路径
     * @throws Exception 如果恢复失败
     */
    @Async
    public void restoreDatabase(String host, String port, String database, String user, String password,
            String sourcePath) throws Exception {
        Process process = null;
        try {
            String command = String.format("mysql -h%s -P%s -u%s -p%s %s < \"%s\"",
                    host, port, user, password, database, sourcePath);

            process = Runtime.getRuntime().exec(new String[] { "sh", "-c", command });
            int processComplete = process.waitFor();
            if (processComplete != 0) {
                throw new Exception("Could not restore the database");
            }
        } catch (IOException | InterruptedException e) {
            throw new Exception("Failed to execute restore command: " + e.getMessage(), e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
}
