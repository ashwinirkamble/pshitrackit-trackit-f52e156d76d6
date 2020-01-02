package com.premiersolutionshi.old.bean;

import org.apache.struts.action.ActionForm;

/**
 * Data holder for a FILE form
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class FileBean extends ActionForm {
    private static final long serialVersionUID = 4871741513182582882L;
    private boolean isEmpty(String tData) { return (tData == null || tData.trim().length() == 0 || tData.equalsIgnoreCase("null")); }
	private String nes(String tStr) { return (isEmpty(tStr) ? "" : tStr); }

	private String filePk = null;
	public String getFilePk() { return nes(this.filePk); }
	public void setFilePk(String newFilePk) { this.filePk = newFilePk; }

	private String filename = null;
	public String getFilename() { return nes(this.filename); }
	public void setFilename(String newFilename) { this.filename = newFilename; }

	private String extension = null;
	public String getExtension() { return nes(this.extension); }
	public void setExtension(String newExtension) { this.extension = newExtension; }

	private String image = null;
	public String getImage() { return nes(this.image); }
	public void setImage(String newImage) { this.image = newImage; }

	private String smlImage = null;
	public String getSmlImage() { return nes(this.smlImage); }
	public void setSmlImage(String newSmlImage) { this.smlImage = newSmlImage; }

	private String filesize = null;
	public String getFilesize() { return nes(this.filesize); }
	public void setFilesize(String newFilesize) { this.filesize = newFilesize; }

	private String contentType = null;
	public String getContentType() { return nes(this.contentType); }
	public void setContentType(String newContentType) { this.contentType = newContentType; }

	private String uploadedBy = null;
	public String getUploadedBy() { return nes(this.uploadedBy); }
	public void setUploadedBy(String newUploadedBy) { this.uploadedBy = newUploadedBy; }

	private String uploadedDate = null;
	public String getUploadedDate() { return nes(this.uploadedDate); }
	public void setUploadedDate(String newUploadedDate) { this.uploadedDate = newUploadedDate; }
} // end of class