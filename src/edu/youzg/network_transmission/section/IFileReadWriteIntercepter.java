package edu.youzg.network_transmission.section;

/**
 * 文件片段 读/写 拦截器 功能接口
 */
public interface IFileReadWriteIntercepter {
    void beforeRead(FileSectionInfo sectionInfo);
    FileSectionInfo afterRead(FileSectionInfo sectionInfo);
    void beforeWrite(String filePath, FileSectionInfo sectionInfo);
    void afterWritten(FileSectionInfo sectionInfo);
}