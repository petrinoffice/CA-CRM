package Main.Common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class WorkWithFile {

    public static void SaveFile(byte[] bytes, File file){
        try {
          System.out.println(file.getAbsoluteFile());

            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
            fos.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
