package net.samongi.frunction.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestLib
{
	public static String readFile(String file_path) throws IOException {return TestLib.readFile(file_path, Charset.defaultCharset());}
	public static String readFile(String file_path, Charset encoding) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(file_path));
	  return new String(encoded, encoding);
	}
}
