package edu.youzg.network_transmission.core;

import edu.youzg.network_transmission.net.*;
import edu.youzg.network_transmission.section.FileSectionInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * 通过 网络 传输 文件片段<br/>
 * net层向外提供的类<br/>
 * 主要提供的方法有：<br/>
 * sendSection、sendLastSection、<br/>
 * receiveSection
 */
public class FileSectionSendReceive {
    private ISendReceive sendReceive;
    private INetSendReceiveSpeed speed;

    public FileSectionSendReceive() {
        this.sendReceive = new NetSendReceive();
        this.speed = new NetSpeed();
    }

    public FileSectionSendReceive(ISendReceive sendReceive) {
        this.sendReceive = sendReceive;
    }

    public void setSendReceive(ISendReceive sendReceive) {
        this.sendReceive = sendReceive;
    }

    public void setSpeed(INetSendReceiveSpeed speed) {
        this.speed = speed;
    }

    /**
     * 发送 “整个文件发送完毕”信号
     * @param dos 操作的流
     * @throws Exception
     */
    public void sendLastSection(DataOutputStream dos) throws Exception {
        FileSectionInfo sectionInfo = new FileSectionInfo(0, 0, 0);
        sectionInfo.setContent(new byte[0]);
        sendReceive.send(dos, sectionInfo.toBytes());   // 发送一个空信息(全为0和null)，标记发送结束
    }

    /**
     * 发送文件片段
     * @param dos 操作的流
     * @param sectionInfo 要发送的文件片段
     * @throws Exception
     */
    public void sendSection(DataOutputStream dos, FileSectionInfo sectionInfo) throws Exception {
        if (sectionInfo == null || sectionInfo.getLength() <= 0) {
            return;
        }
        // 发送 “文件片段头部”
        sendReceive.send(dos, sectionInfo.toBytes());
        // 发送 “文件片段 全部内容”
        sendReceive.send(dos, sectionInfo.getContent());

        this.speed.afterSend(sectionInfo.getLength());
    }

    /**
     * 接收文件片段：<br/>
     * 1. 接收头部<br/>
     * 2. 接收文件片段内容
     * @param dis 操作的流
     * @return 读取到的文件片段信息
     * @throws Exception
     */
    public FileSectionInfo receiveSection(DataInputStream dis) throws Exception {
        // 文件片段头组成：fileNo(int) + offset(long) + length(int)
        byte[] head = sendReceive.receive(dis, 16);
        FileSectionInfo sectionInfo = new FileSectionInfo(head);

        int sectionLength = sectionInfo.getLength();
        if (sectionLength > 0) {
            byte[] receive = this.sendReceive.receive(dis, sectionLength);
            sectionInfo.setContent(receive);    // 再次收取 对端发来的 指定长度的 信息
            this.speed.afterReceive(sectionLength);
        }
        return sectionInfo;
    }

}