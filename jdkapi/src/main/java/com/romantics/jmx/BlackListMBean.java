package com.romantics.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;


class Main {
    public static void main(String[] args) throws Exception {
        // 获取 MBean Server
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();

// 创建 MBean 初始黑名单用户为 a 和 b
        BlackList blackList = new BlackList();
        blackList.addBlackItem("a");
        blackList.addBlackItem("b");
// 注册
        ObjectName objectName = new ObjectName("com.common.example.jmx:type=BlackList, name=BlackListMBean");
        platformMBeanServer.registerMBean(blackList, objectName);

        // 创建 MBean 初始黑名单用户为 a 和 b
        BlackList blackList1 = new BlackList();
        blackList1.addBlackItem("a");
        blackList1.addBlackItem("b");
// 注册
//        ObjectName objectName1 = new ObjectName("com.common.example.jmx:type=BlackList, name=BlackListMBean1");
//        platformMBeanServer.registerMBean(blackList1, objectName1);
//        Set<ObjectInstance> objectInstances = platformMBeanServer.queryMBeans(new ObjectName("com.common.example.*:type=BlackList, name=*"), null);
//        System.out.println(objectInstances);
        blackList.await();
    }
}


public interface BlackListMBean {
    // 获取黑名单列表
    public String[] getBlackList();

    // 在黑名单列表中添加一个用户
    public boolean addBlackItem(String uid);

    boolean removeBlackItem(String uid);

    // 判断某个用户是否在黑名单中
    public boolean contains(String uid);

    // 获取黑名单大小
    public int getBlackListSize();

    public void countDown();
}

class BlackList implements BlackListMBean {
    private CountDownLatch exit = new CountDownLatch(1);

    private Set<String> uidSet = new HashSet<>();

    @Override
    public String[] getBlackList() {
        return uidSet.toArray(new String[0]);
    }

    @Override
    public boolean addBlackItem(String uid) {
        return uidSet.add(uid);
    }

    @Override
    public boolean removeBlackItem(String uid) {
        return uidSet.remove(uid);
    }

    @Override
    public boolean contains(String uid) {
        return uidSet.contains(uid);
    }

    @Override
    public int getBlackListSize() {
        return uidSet.size();
    }

    //    @Override
    public void await() throws InterruptedException {
        exit.await();
    }

    @Override
    public void countDown() {
        exit.countDown();
    }
}


