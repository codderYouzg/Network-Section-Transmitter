package edu.youzg.network_transmission.net;

/**
 * 网络文件片段收发 善后功能 接口
 */
public interface INetSendReceiveSpeed {

    /**
     * 发送后
     * @param sendBytes 要发送的 字节数
     */
    void afterSend(int sendBytes);

    /**
     * 接收后<br/>
     * 在本框架中，主要用于 计算平均/瞬时速率
     * @param receiveBytes 接收的字节数
     */
    void afterReceive(int receiveBytes);

}