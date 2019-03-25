package com.hug.common.keygen;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class KeyGeneratorDemo {

    /**
     * 根据机器名最后的数字编号获取工作进程编号。
     * 如果线上机器命名有统一规范,建议使用此种方式。
     * 例如，机器的 HostName 为: dangdang-db-sharding-dev-01(公司名-部门名-服务名-环境名-编号)，会截取 HostName 最后的编号 01 作为工作进程编号( workId )。
     */
    public static void initWorkerIdOfHostName() {
        InetAddress address;
        Long workerId;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        String hostName = address.getHostName();
        try {
            workerId = Long.valueOf(hostName.replace(hostName.replaceAll("\\d+$", ""), ""));
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Wrong hostname:%s, hostname must be end with number!", hostName));
        }
        DefaultKeyGenerator.setWorkerId(workerId);
    }

    /**
     * 因为工作进程编号最大限制是 2^10，我们生成的工程进程编号只要满足小于 1024 即可
     * 1.针对IPV4:
     * ....IP最大 255.255.255.255。而（255+255+255+255) < 1024。
     * ....因此采用IP段数值相加即可生成唯一的workerId，不受IP位限制。
     * 2.针对IPV6:
     * ....IP最大 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
     * ....为了保证相加生成出的工程进程编号 < 1024,思路是将每个 Bit 位的后6位相加。这样在一定程度上也可以满足workerId不重复的问题。
     * 使用这种 IP 生成工作进程编号的方法,必须保证IP段相加不能重复
     */
    public static void initWorkerIdOfIP6AndIp4() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }

        byte[] ipAddressByteArray = address.getAddress();
        long workerId = 0L;
        // IPV4
        if (ipAddressByteArray.length == 4) {
            for (byte byteNum : ipAddressByteArray) {
                workerId += byteNum & 0xFF;
            }
        } else if (ipAddressByteArray.length == 16) {
            for (byte byteNum : ipAddressByteArray) {
                workerId += byteNum & 0B111111;
            }
        } else {
            throw new IllegalStateException("Bad LocalHost InetAddress, please check your network!");
        }

        DefaultKeyGenerator.setWorkerId(workerId);
    }

    /**
     * 根据机器IP获取工作进程编号。
     * 如果线上机器的IP二进制表示的最后10位不重复,建议使用此种方式。
     * 例如，机器的IP为192.168.1.108，二进制表示: 11000000101010000000000101101100，截取最后 10 位 0101101100，转为十进制 364，设置工作进程编号为 364。
     */
    public static void initWorkerIdOfIPKeyGenerator() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }

        byte[] ipAddressByteArray = address.getAddress();
        long workerId = (long) (((ipAddressByteArray[ipAddressByteArray.length - 2] & 0B11) << Byte.SIZE) + (ipAddressByteArray[ipAddressByteArray.length - 1] & 0xFF));

        DefaultKeyGenerator.setWorkerId(workerId);
    }

    public static void main(String[] args) {
//        KeyGeneratorDemo.initWorkerIdOfIPKeyGenerator();
        KeyGeneratorDemo.initWorkerIdOfIP6AndIp4();
//        KeyGeneratorDemo.initWorkerIdOfHostName();

        KeyGenerator keyGenerator = KeyGeneratorFactory.newInstance(DefaultKeyGenerator.class.getName());
        for (int i = 0; i < 5000; i++) {
            System.out.println(keyGenerator.generateKey());
        }


    }
}

