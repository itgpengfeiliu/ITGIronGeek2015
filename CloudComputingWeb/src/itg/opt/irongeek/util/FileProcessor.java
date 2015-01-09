

package itg.opt.irongeek.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileProcessor {
    
    
    public boolean writeToFile(String path, InputStream uploadedInputStream, String fileName) {

        try {
            //String videoFilename = fileName;
            //logger.debug("FileProcessor writeToFile " + videoFilename);
            System.out.println("fileName = " + fileName);
            OutputStream out = new FileOutputStream(new File(path + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

            return true;
        } catch (IOException e) {
            //logger.error("IOException: " + e.getMessage());
        }

        return false;
    }
}
