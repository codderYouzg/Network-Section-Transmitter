package edu.youzg.network_transmission.section;

/**
 * 文件片段读写 拦截器适配器
 */
public class FileReadWriteIntercepterAdapter implements IFileReadWriteIntercepter {

    public FileReadWriteIntercepterAdapter() {
    }

    @Override
    public void beforeRead(FileSectionInfo sectionInfo) {
    }

    @Override
    public FileSectionInfo afterRead(FileSectionInfo sectionInfo) {
        return sectionInfo;
    }

    @Override
    public void beforeWrite(String filePath, FileSectionInfo sectionInfo) {
    }

    @Override
    public void afterWritten(FileSectionInfo sectionInfo) {
    }
    
}