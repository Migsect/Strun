package net.samongi.frunction.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.IllegalArgumentException;

public class IniFileReader {

  private static final Pattern SECTION  = Pattern.compile("\\s*\\[([^]]*)\\]\\s*");
  private static final Pattern KEY_VALUE = Pattern.compile("\\s*([^=]*)=(.*)");

  private Map<String, Map<String, String>>  keyValuePairs;

  public IniFileReader() { 
    this.keyValuePairs = new HashMap<>();
  }
  
  public IniFileReader(InputStream stream) throws IOException, IllegalArgumentException {
    this.keyValuePairs = new HashMap<>();
    load(stream);
  }

  public IniFileReader(String filepath) throws IOException, IllegalArgumentException {
    this.keyValuePairs = new HashMap<>();
    load(filepath);
  }

  
  public void load(String filepath) throws IOException, IllegalArgumentException {
    File file = new File(filepath);
    if(file.exists() && !file.isDirectory()) { 
      loadStream(new BufferedReader(new FileReader(filepath)));
    }
    else {
      throw new IllegalArgumentException("The file '" + filepath + "' does not exist.");
    }
  }

  public void load(InputStream stream) throws IllegalArgumentException, IOException {
    if (stream != null) {
      loadStream(new BufferedReader(new InputStreamReader(stream)));
    }
    else {
      throw new IllegalArgumentException();
    }
  }


  public String getString( String section, String key, String defaultvalue ) {
    Map<String, String> kv = this.keyValuePairs.get(section);
    if(kv != null) {
      String result = kv.get(key);
      if (result != null) return result;
    }
    
    return defaultvalue;
  }

  public int getInt( String section, String key, int defaultvalue ) {
    Map< String, String> kv = this.keyValuePairs.get(section);

    if (kv != null){
      try {
        String value = kv.get(key);
        if (value != null) return Integer.parseInt(kv.get(key));
      }
      catch (NumberFormatException ignore) {
      }
    }
    
    return defaultvalue;
  }

  public float getFloat( String section, String key, float defaultvalue ) {
    Map< String, String> kv = this.keyValuePairs.get(section);

    if (kv != null){
      try {
        String value = kv.get(key);
        if (value != null) return Float.parseFloat(kv.get(key));
      }
      catch (NumberFormatException ignore) {
      }
    }
    
    return defaultvalue;
  }

  public double getDouble( String section, String key, double defaultvalue ) {
    Map< String, String> kv = this.keyValuePairs.get(section);

    if (kv != null){
      try {
        String value = kv.get(key);
        if (value != null) return Double.parseDouble(kv.get(key));
      }
      catch (NumberFormatException ignore) {
      }
    }
    
    return defaultvalue;
  }


  private void loadStream(BufferedReader reader) throws IOException, IllegalArgumentException {
    if (reader != null) {
      try {
        String line;
        String section = null;
        
        while ((line = reader.readLine()) != null ) {
          Matcher matcher = SECTION.matcher( line );
          
          if (matcher.matches()) {
            section = matcher.group(1).trim();
          }
          else if (section != null) {
            matcher = KEY_VALUE.matcher( line );
            
            if(matcher.matches()) {
              String key   = matcher.group(1).trim();
              String value = matcher.group(2).trim();
              
              Map<String, String> kv = this.keyValuePairs.get(section);
              if( kv == null ) {
                this.keyValuePairs.put(section, kv = new HashMap<>());   
              }
              if (key != null) {
                kv.put(key, value);
              }
            }
          }
        }
        
      }
      finally {
        reader.close();
      }
    }
    else {
      throw new IllegalArgumentException("The BufferedReader cannot be null.");
    }
  }
}