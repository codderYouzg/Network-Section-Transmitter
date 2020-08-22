package edu.youzg.network_transmission.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * 通过网络，收发字节数组
 */
public class NetSendReceive implements ISendReceive {
    public static final int DEFAULT_SECTION_LEN = 1 << 15;
    private int bufferSize; // 设置的缓冲区大小
    private INetSendReceiveSpeed speed; // 用于监控网速

    public NetSendReceive() {
        speed = new NetSendReceiveSpeedAdapter();
        bufferSize = DEFAULT_SECTION_LEN;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setSpeed(INetSendReceiveSpeed speed) {
        this.speed = speed;
    }

    /**
     * 通过网络，发送字节数组
     * @param dos 操作的流
     * @param content 目标字节数组
     * @throws Exception
     */
    @Override
    public void send(DataOutputStream dos, byte[] content) throws Exception {
        int length = content.length;
        int offset = 0;
        int curLen = 0;

        while (length>0) {
            curLen = length>bufferSize ? bufferSize : length;   // 使得当前长度为两者中最小者(防止每次发送大小超出限制)
            dos.write(content, offset, curLen);
            offset += curLen;
            length -= curLen;
        }
        this.speed.afterSend(length);
    }

    /**
     * 通过网络，接收字节数组，<br/>
     * 并作为返回值 返回
     * @param dis 操作的流
     * @param length 字节数组的大小
     * @return 读取到的 字节数组
     * @throws Exception
     */
    @Override
    public byte[] receive(DataInputStream dis, int length) throws Exception {
        byte[] buffer = new byte[length];

        int curLen = 0;
        int factLen = 0;    // 真实的读取长度
        int offset = 0;
        while (length > 0) {
            curLen = length>bufferSize ? this.bufferSize : length;
            factLen = dis.read(buffer, offset, curLen);
            length -= factLen;
            offset += factLen;
        }
        this.speed.afterReceive(length);

        return buffer;
    }

}