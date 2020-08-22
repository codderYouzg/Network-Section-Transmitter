package edu.youzg.network_transmission.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * 收发字节数组 功能接口
 */
public interface ISendReceive {

    /**
     * 通过网络，发送字节数组
     * @param dos 操作的流
     * @param content 目标字节数组
     * @throws Exception
     */
    void send(DataOutputStream dos, byte[] content) throws Exception;

    /**
     * 通过网络，接收字节数组
     * @param dis 操作的流
     * @param len 读取的长度
     * @return 读取到的数组
     * @throws Exception
     */
    byte[] receive(DataInputStream dis, int len) throws Exception;

}