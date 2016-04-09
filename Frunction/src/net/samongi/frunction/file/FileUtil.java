package net.samongi.frunction.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil
{
  public static String readFile(File file)
  {
    String file_path = file.getAbsolutePath();
    try
    {
      return FileUtil.readFile(file_path);
    }
    catch(IOException e)
    {
      return null;
    }
  }

  public static String readFile(String file_path) throws IOException
  {
    return FileUtil.readFile(file_path, Charset.defaultCharset());
  }

  public static String readFile(String file_path, Charset encoding) throws IOException
  {
    byte[] encoded = Files.readAllBytes(Paths.get(file_path));
    return new String(encoded, encoding);
  }
}
