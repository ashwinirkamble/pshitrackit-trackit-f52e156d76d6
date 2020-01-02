package com.premiersolutionshi.common.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.common.dao.FileInfoDao;
import com.premiersolutionshi.common.domain.FileInfo;
import com.premiersolutionshi.common.util.FileUtils;
import com.premiersolutionshi.common.util.StringUtils;

public class FileInfoService extends ModifiedService<FileInfo> {
    public FileInfoService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, FileInfoDao.class, userService);
    }

    @Override
    public boolean deleteById(Integer id) {
        FileUtils.deleteFile(id);
        return super.deleteById(id);
    }

    public ArrayList<FileInfo> saveFormFileList(ArrayList<FormFile> formFileList) {
        ArrayList<FileInfo> fileList = new ArrayList<>();
        if (formFileList == null || formFileList.isEmpty()) {
            return fileList;
        }
        for (FormFile file : formFileList) {
            if (file != null && !StringUtils.isEmpty(file.getFileName()) && file.getFileSize() > 0) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFilename(file.getFileName());
                String filename = file.getFileName();
                fileInfo.setExtension(filename.lastIndexOf(".") > -1 ? filename.substring(filename.lastIndexOf(".") + 1) : "");;
                fileInfo.setFilesize(file.getFileSize());
                fileInfo.setContentType(file.getContentType());
                if (save(fileInfo)) {
                    logInfo("Saving to disk: " + file.getFileName());
                    Integer id = fileInfo.getId();
                    if (FileUtils.writeFile(file, String.valueOf(id))) {
                        fileList.add(fileInfo);
                    }
                    else {
                        delete(fileInfo);
                    }
                }
            }
        }
        return fileList;
    }
    
    @Override
    protected void beforeInsert(FileInfo domain) {
        super.beforeInsert(domain);
        domain.setUploadedBy(domain.getLastUpdatedBy());
        domain.setUploadedDate(domain.getLastUpdatedDate());
    }

    @Override
    protected FileInfoDao getDao() {
        return (FileInfoDao) super.getDao();
    }
}
