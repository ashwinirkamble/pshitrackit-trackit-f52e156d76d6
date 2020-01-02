package com.premiersolutionshi.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.old.model.BaseModel;

public class FileUtils extends BaseModel {
    private static Logger logger = Logger.getLogger(FileUtils.class.getSimpleName());
    public static final String UPLOAD_DIR = "C:\\Java\\pshi\\upload\\";

    public static boolean writeFile(FormFile file, String filename) {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            String dirPath = dir.getPath();
            if (!dir.mkdirs()) {
                logError("writeFile", "Failed to create directory: " + dirPath, logger);
                return false;
            }
            else {
                logInfo("writeBinaryFile", "Created directory: " + dirPath, logger);
            }
        }
        String filePath = UPLOAD_DIR + filename;
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath))) {
            InputStream in = file.getInputStream();
            byte[] byteArr = new byte[512 * 1024]; // 512 KB buffer
            int byteRead = 0;
            double totalRead = 0;
            while ((byteRead = in.read(byteArr)) != -1) {
                out.write(byteArr, 0, byteRead);
                totalRead += byteRead;
            }
            logInfo("writeFile", filePath + " successfully written to disk. Bytes read: " + totalRead + " bytes.", logger);
        }
        catch (IOException e) {
            logError("writeFile", "Error writing file to " + filePath, logger, e);
            return false;
        }
        return true;
    }

    public static boolean deleteFile(Integer filePk) {
        if (filePk == null) {
            logError("deleteFile", "Failed. filePk IS NULL", logger);
            return false;
        }
        String filePath = UPLOAD_DIR + filePk;
        try {
            File file = new File(filePath);
            if (file.exists() && file.delete()) {
                logInfo("deleteBinaryFile", "Successfully deleted '" + filePath + "'", logger);
                return true;
            }
            else {
                logError("deleteBinaryFile", "Could not find file or Failed to delete " + filePath, logger);
            }
            return true;
        } catch (Exception e) {
            logError("deleteBinaryFile", "Failed to delete filePk=" + filePk, logger, e);
        }
        return false;
    }
}
