package com.premiersolutionshi.common.domain;

import java.time.LocalDateTime;

import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.util.CommonMethods;

public class FileInfo extends ModifiedDomain {
    private static final long serialVersionUID = 219885410225362329L;

    private String filename;
    private String extension;
    private String contentType;
    private Integer filesize;
    private boolean deleted;
    private String uploadedBy;
    private LocalDateTime uploadedDate;

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public String getUploadedDateSql() {
        return DateUtils.formatToSqliteDatetime(uploadedDate);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getSmallIcon() {
        return CommonMethods.getSmallIcon(getExtension());
    }

    public String getLargeIcon() {
        return CommonMethods.getLargeIcon(getExtension());
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return The size of the file, in bytes.
     */
    public Integer getFilesize() {
        return filesize;
    }

    /**
     * @return The size of the file, in bytes.
     */
    public String getFilesizeStr() {
        return StringUtils.formatBytes(filesize);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadedDate() {
        return uploadedDate;
    }

    public String getUploadedDateStr() {
        return uploadedDate == null ? null : DateUtils.formatToBasicFormat(uploadedDate);
    }

    public void setUploadedDate(LocalDateTime uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}
