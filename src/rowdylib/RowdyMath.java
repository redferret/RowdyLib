
package rowdylib;

import java.util.regex.Pattern;
import rowdy.RowdyInstance;
import rowdy.NativeJavaCode;
import rowdy.NativeJava;

/**
 *
 * @author Richard
 */
public class RowdyMath {
  
  @NativeJava
  public static NativeJavaCode cos() {
    return (RowdyInstance instance, Object... parameters) -> {
      Double a = Double.parseDouble(parameters[0].toString());
      return Math.cos(a);
    };
  }
  
  @NativeJava
  public static NativeJavaCode sin() {
    return (RowdyInstance instance, Object... parameters) -> {
      Double a = Double.parseDouble(parameters[0].toString());
      return Math.sin(a);
    };
  }
  
  @NativeJava
  public static NativeJavaCode tan() {
    return (RowdyInstance instance, Object... parameters) -> {
      Double a = Double.parseDouble(parameters[0].toString());
      return Math.tan(a);
    };
  }
  
  @NativeJava
  public static NativeJavaCode rand() {
    return (RowdyInstance instance, Object... parameters) -> {
      return Math.random();
    };
  }
  
  @NativeJava
  public static NativeJavaCode isNumber() {
    return (RowdyInstance instance, Object... parameters) -> {
      String value = parameters[0].toString();
      return Pattern.matches("-?\\d+(\\.\\d+)?", value);
    };
  }
  
}
