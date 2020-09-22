package com.yk.concurrency.base.chapter1;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-22 22:22
 **/
public class Common {

    public static void writeDataToFile() {
        // 从数据库读取数据并处理
        try {
            System.out.println("将数据写入文件");
            Thread.sleep(1000 * 30L);
            System.out.println("数据写入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readFromDataBase() {
        // 从数据库读取数据并处理
        try {
            System.out.println("开始读取数据库数据");
            Thread.sleep(1000 * 30L);
            System.out.println("读取数据成功, 并且处理完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}