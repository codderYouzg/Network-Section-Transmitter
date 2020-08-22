package edu.youzg.network_transmission.section;

import edu.youzg.util.ByteConverter;

/**
 * 封装 文件片段的信息
 */
public class FileSectionInfo {
    private int fileNo; // 当前文件片段 编号
    private long offset;    // 当前文件片段所处 偏移量
    private int length; // 当前文件片段长度
    private byte[] content; // 文件片段内容

    public FileSectionInfo() {
    }

    /**
     * 初始化 fileNo、offset、length
     * @param head
     */
    public FileSectionInfo(byte[] head) {
        this.fileNo = ByteConverter.bytesToInt(head);
        this.offset = ByteConverter.bytesToLong(head, 4);
        this.length = ByteConverter.bytesToInt(head, 12);
    }

    public FileSectionInfo(int fileNo, long offset, int length) {
        this.fileNo = fileNo;
        this.offset = offset;
        this.length = length;
    }

    /**
     * 将 fileNo、offset、length 信息转换为 字节数组
     * @return
     */
    public byte[] toBytes() {
        byte[] res = new byte[16];
        ByteConverter.intToBytes(res, 0, fileNo);
        ByteConverter.longToBytes(res, 4, offset);
        ByteConverter.intToBytes(res, 12, length);

        return res;
    }

    public int getFileNo() {
        return fileNo;
    }

    public void setFileNo(int fileNo) {
        this.fileNo = fileNo;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int len) {
        this.length = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return fileNo + " : " + offset + ", " + length;
    }

}