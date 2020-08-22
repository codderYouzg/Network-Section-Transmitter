package edu.youzg.network_transmission.core;

import edu.youzg.network_transmission.section.FileReadWriteIntercepterAdapter;
import edu.youzg.network_transmission.section.FileSectionInfo;
import edu.youzg.network_transmission.section.IFileReadWriteIntercepter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 1. 操作单个文件<br/>
 * 2. FileSectionInfo的 读取、写入 操作(仅针对同一台主机而言)<br/>
 * 3. section层对外提供的类
 */
public class FileReadWrite {
    private int fileNo;     // 文件片段的序号
    private String filePath;    // 源文件 所在路径
    private RandomAccessFile raf;   // 随机访问流，用于 读写指定“偏移量”与“长度” 的文件片段
    private IFileReadWriteIntercepter fileReadWriteIntercepter;

    public FileReadWrite(int fileNo, String filePath) {
        this.fileNo = fileNo;
        this.filePath = filePath;
        this.fileReadWriteIntercepter = new FileReadWriteIntercepterAdapter();
    }

    public void setFileReadWriteIntercepter(IFileReadWriteIntercepter fileReadWriteIntercepter) {
        this.fileReadWriteIntercepter = fileReadWriteIntercepter;
    }

    public int getFileNo() {
        return fileNo;
    }

    /**
     * 根据所传sectionInfo参数 和 filePath属性，<br/>
     * 将指定文件的指定片段 读取并封装入sectionInfo中
     * @param sectionInfo 用于获知目标文件片段的 偏移量、长度，以及封装最终的读取结果
     * @return 目标 sectionInfo
     * @throws IOException
     */
    public FileSectionInfo readSection(FileSectionInfo sectionInfo) throws IOException {
        this.fileReadWriteIntercepter.beforeRead(sectionInfo);
        if (raf==null) {
            // 创建 只读形式 随机访问流
            raf = new RandomAccessFile(filePath, "r");
        }
        raf.seek(sectionInfo.getOffset());  // 定位
        int length = sectionInfo.getLength();
        byte[] buffer = new byte[length];   // 构建缓冲区
        raf.read(buffer);   // 读取数据至缓冲区中
        sectionInfo.setContent(buffer); // 将数据 设置进 sectionInfo中

        return fileReadWriteIntercepter.afterRead(sectionInfo);
    }

    /**
     * 根据所传sectionInfo参数 和 filePath属性，<br/>
     * 将指定文件的指定片段 写入到 当前程序运行主机 的 指定位置
     * @param sectionInfo 目标文件片段信息
     * @return 是否写入成功
     */
    public boolean writeSection(FileSectionInfo sectionInfo) {
        fileReadWriteIntercepter.beforeWrite(filePath, sectionInfo);
        if (this.raf == null) {
            synchronized (filePath) {
                try {
                    File file = new File(filePath);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                    this.raf = new RandomAccessFile(filePath, "rw");
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        try {
            synchronized (filePath) {
                this.raf.seek(sectionInfo.getOffset());
                this.raf.write(sectionInfo.getContent());
                this.fileReadWriteIntercepter.afterWritten(sectionInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 关闭当前 随机访问流
     */
    public void close() {
        try {
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}