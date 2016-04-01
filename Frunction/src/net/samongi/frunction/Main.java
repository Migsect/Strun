package net.samongi.frunction;

import java.io.File;

import net.samongi.frunction.file.FileUtil;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.parse.Commenting;
import net.samongi.frunction.parse.ParseUtil;

public class Main
{

	public static void main(String[] args)
	{
	  if(args.length < 1) return;
	  String file_loc = args[0];
	  System.out.println("Attempting to parse: " + file_loc);
	  
	  File file = new File(file_loc);
		String text_body = FileUtil.readFile(file);
		
    System.out.println("___Raw Text:");
		System.out.print(text_body);
		System.out.println();
    System.out.println();
		
		text_body = Commenting.removeComments(text_body);
		
		System.out.println("___Removed Comments Text:");
    System.out.print(text_body);
    System.out.println();
    System.out.println();
    
    System.out.println("___Starting Parsing:");
    text_body = ParseUtil.removeNextLines(text_body);
    text_body = ParseUtil.squeeze(text_body);
    DynamicFrunction main_frunction = new DynamicFrunction(null, text_body);
    main_frunction.evaluate();
	}

}
