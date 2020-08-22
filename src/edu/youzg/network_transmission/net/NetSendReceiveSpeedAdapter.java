package edu.youzg.network_transmission.net;

/**
 * 网络文件片段收发 善后功能 适配器
 */
public class NetSendReceiveSpeedAdapter implements INetSendReceiveSpeed {

    @Override
    public void afterSend(int sendBytes) {
    }

    /**
     * 在本框架中，主要用于 计算平均/瞬时速率
     * @param receiveBytes 接收的字节数
     */
    @Override
    public void afterReceive(int receiveBytes) {
    }

}