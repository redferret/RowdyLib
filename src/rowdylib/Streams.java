
package rowdylib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import rowdy.NativeJavaCode;
import rowdy.NativeJava;
import rowdy.RowdyInstance;

/**
 *
 * @author Richard
 */
public class Streams {
  
  @NativeJava
  public static NativeJavaCode getBufferedReader() {
    return (RowdyInstance instance, Object... params) -> {
      if (!(params[0] instanceof FileInputStream)) {
        throw new IllegalArgumentException("Expected FileInputStream");
      }
      FileInputStream stream = (FileInputStream) params[0];
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      return reader;
    };
  }
  
  @NativeJava
  public static NativeJavaCode openFileInputStream() {
    return (RowdyInstance instance, Object ... params) -> {
      Object p1 = params[0];
      if (!(p1 instanceof String)) {
        throw new IllegalArgumentException("Expected String for file name");
      }
      String fileName = (String) params[0];
      FileInputStream input = null;
      File file = new File(fileName);
    
      if (!file.canRead()) {
        file.setReadable(true);
      }
      try {
        input = new FileInputStream(file);
      } catch (FileNotFoundException ex) {
        throw new RuntimeException(ex);
      }
      
      return input;
    };
  }
  
  @NativeJava
  public static NativeJavaCode closeInputStream() {
    return (RowdyInstance instance, Object... params) -> {
      if (!(params[0] instanceof InputStream)) {
        throw new IllegalArgumentException("Expected InputStream");
      }
      InputStream inputStream = (InputStream) params[0];
      try {
        inputStream.close();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      instance.setInputStream(System.in);
      
      return true;
    };
  }
  
  @NativeJava
  public static NativeJavaCode closeBufferedReader() {
    return (RowdyInstance instance, Object... params) -> {
      if (!(params[0] instanceof BufferedReader)) {
        throw new IllegalArgumentException("Expected BufferedReader");
      }
      BufferedReader reader = (BufferedReader) params[0];
      try {
        reader.close();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      instance.setInputStream(System.in);
      
      return true;
    };
  }
  
  @NativeJava
  public static NativeJavaCode readLine() {
    return (RowdyInstance instance, Object... params) -> {
      if (!(params[0] instanceof BufferedReader)) {
        throw new IllegalArgumentException("Expected BufferedReader");
      }
      BufferedReader reader = (BufferedReader) params[0];
      try {
        return reader.readLine();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    };
  }
  
}
