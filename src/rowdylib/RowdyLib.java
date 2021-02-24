
package rowdylib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import rowdy.BaseNode;
import rowdy.RowdyInstance;
import rowdy.exceptions.ConstantReassignmentException;
import rowdy.NativeJavaCode;
import rowdy.NativeJava;


/**
 *
 * @author Richard
 */
public class RowdyLib {
  
  @NativeJava
  public static NativeJavaCode instance() {
    return (RowdyInstance instance, Object... params) -> {
      try {
        Class<?> clazz = Class.forName((String) params[0]);
        ArrayList<String> constructorTypes = new ArrayList<>();
        int expectedInitLength = 1;
        if (params.length > 1) {
           constructorTypes = (ArrayList<String>) params[1];
           expectedInitLength = 2;
        }
        int additionalParamsLength = params.length - expectedInitLength;
      
        Class[] paramClasses = new Class[additionalParamsLength];
        Object[] paramValues = new Object[additionalParamsLength];

        if (additionalParamsLength > 0) {
          int startIndex = expectedInitLength;
          for (int i = startIndex; i < params.length; i++) {
            paramClasses[i - expectedInitLength] = params[i].getClass();
            paramValues[i - expectedInitLength] = params[i];
          }
        } 
        
        Constructor<?> constructor = clazz.getConstructor(paramClasses);
        return constructor.newInstance(paramValues);
        
      } catch (NoSuchMethodException ex) {
        
        throw new RuntimeException(ex);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }; 
  }
  
  @NativeJava
  public static NativeJavaCode java() {
    return (RowdyInstance instance, Object... params) -> {
      Class c = params[0].getClass();
      String methodName = (String) params[1];
      ArrayList<String> types = new ArrayList<>();
      int expectedInitLength = 2;
      if (params.length > 2) {
         types = (ArrayList<String>) params[2];
         expectedInitLength = 3;
      }
      Method javaMethod;
      int additionalParamsLength = params.length - expectedInitLength;
      
      Class[] paramClasses = new Class[additionalParamsLength];
      Object[] paramValues = new Object[additionalParamsLength];
      
      if (additionalParamsLength > 0) {
        int startIndex = expectedInitLength;
        for (int i = startIndex; i < params.length; i++) {
          paramClasses[i - expectedInitLength] = params[i].getClass();
          paramValues[i - expectedInitLength] = params[i];
        }
      } 
      
      try {
        javaMethod = c.getDeclaredMethod(methodName, paramClasses);
        return javaMethod.invoke(params[0], paramValues);
      } catch (NoSuchMethodException nme) {
        explicitCast(c, methodName, types, paramClasses);
        try {
          javaMethod = c.getDeclaredMethod(methodName, paramClasses);
          return javaMethod.invoke(params[0], paramValues);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
      
    };
  }

  private static void explicitCast(Class c, String methodName, ArrayList<String> types, Class[] paramClasses) throws RuntimeException {
    for (Method method : c.getMethods()) {
      if (method.getName().equals(methodName)) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == types.size()) {
          int i = 0;
          for (Class<?> clazz : paramTypes) {
            Class<?> compareToClass;
            try {
              compareToClass = Class.forName(types.get(i));
            } catch (ClassNotFoundException ex) {
              throw new RuntimeException(ex);
            }
            if (clazz == compareToClass) {
              paramClasses[i] = compareToClass;
            } else {
              Class<?> primativeCheck = checkForPrimatives(clazz, compareToClass);
              if (primativeCheck != null) {
                paramClasses[i] = primativeCheck;
              }
            }
            i++;
          }
        }
      }
    }
  }
  
  private static Class<?> checkForPrimatives(Class<?> clazz, Class<?> compareToClass) {
    if (clazz == int.class) {
      return (Integer.class == compareToClass)? int.class : null;
    } else if (clazz == long.class) {
      return (Long.class == compareToClass)? long.class : null;
    } else if (clazz == short.class) {
      return (Short.class == compareToClass)? short.class : null;
    } else if (clazz == byte.class) {
      return (Byte.class == compareToClass)? byte.class : null;
    } else if (clazz == float.class) {
      return (Float.class == compareToClass)? float.class : null;
    } else if (clazz == double.class) {
      return (Double.class == compareToClass)? double.class : null;
    } else if (clazz == char.class) {
      return (Character.class == compareToClass)? char.class : null;
    } else {
      return null;
    }
  }
  
  @NativeJava
  public static NativeJavaCode intToChar() {
    return (RowdyInstance instance, Object... params) -> {
      int ci = (int) params[0];
      char c = (char) ci;
      return c;
    }; 
  }
  
  @NativeJava
  public static NativeJavaCode getRowdyInstance() {
    return (RowdyInstance instance, Object... params) -> {
      return instance;
    }; 
  }
  
  @NativeJava
  public static NativeJavaCode DecimalFormat() {
    return (RowdyInstance instance, Object... params) -> {
      return new DecimalFormat(params[0].toString());
    };
  }
  
  @NativeJava
  public static NativeJavaCode format() {
    return (RowdyInstance instance, Object... params) -> {
      if (params[1] instanceof DecimalFormat) {
        DecimalFormat df = (DecimalFormat) params[1];
        return df.format(params[0]);
      }
      return null;
    };
  }
  
  @NativeJava
  public static NativeJavaCode runtime() {
    return (RowdyInstance instance, Object... params) -> {
      
      BaseNode callback = (BaseNode) params[0];
      BaseNode function = instance.buildAndAllocateCallBack(callback);
      long start = System.currentTimeMillis();
      try {
        instance.executeFunc(function, new ArrayList<>());
      } catch (ConstantReassignmentException ex) {
        throw new RuntimeException(ex);
      }
      
      long finish = System.currentTimeMillis();
      return finish - start;
    };
  }
  
  @NativeJava
  public static NativeJavaCode equalsIgnoreCase() {
    return (RowdyInstance instance, Object... params) -> {
      String p1 = params[0].toString();
      String p2 = params[1].toString();
      
      return p1.equalsIgnoreCase(p2);
    };
  }
  
  @NativeJava
  public static NativeJavaCode strcmp() {
    return (RowdyInstance instance, Object... params) -> {
      String p1 = params[0].toString();
      String p2 =  params[1].toString();
      
      return p1.compareTo(p2);
    };
  }
  
  @NativeJava
  public static NativeJavaCode contains() {
    return (RowdyInstance instance, Object... params) -> {
      String p1 = params[0].toString();
      String p2 = params[1].toString();
      
      return p1.contains(p2);
    };
  }
  
  @NativeJava
  public static NativeJavaCode slice() {
    return (RowdyInstance instance, Object... params) -> {
      String str = params[0].toString();
      int p1 = (int) params[1];
      int p2 = (int) params[2];
      
      return str.substring(p1, p2);
    };
  }
  
  @NativeJava
  public static NativeJavaCode getChar() {
    return (RowdyInstance instance, Object... parameters) -> {
      Integer index = (Integer) parameters[1];
      String str = (String) parameters[0];
      return str.charAt(index);
    };
  }
  
  @NativeJava
  public static NativeJavaCode type() {
    return (RowdyInstance instance, Object... parameters) -> {
      Object test = parameters[0];
      if (test == null) {
        return null;
      }
      return test.getClass().getCanonicalName().replaceAll("java.lang.", "");
    };
  }
  
}
