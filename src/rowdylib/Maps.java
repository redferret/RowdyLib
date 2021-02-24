
package rowdylib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rowdy.BaseNode;
import rowdy.RowdyInstance;
import rowdy.Value;
import rowdy.exceptions.ConstantReassignmentException;
import rowdy.NativeJavaCode;
import rowdy.NativeJava;


/**
 *
 * @author Richard
 */
public class Maps {
  
  @NativeJava
  public static NativeJavaCode forLoop() {
    return (RowdyInstance instance, Object... params) -> {
      Integer start = (int) params[0];
      Integer end = (int) params[1];
      Integer inc = (int) params[2];
      BaseNode callback = (BaseNode) params[3];
      
      BaseNode callBackFunc = instance.buildAndAllocateCallBack(callback);
      List<Value> funcParams = new ArrayList<>();
      Value iValue = new Value(null, false);
      funcParams.add(iValue);
      for (int i = start; (start < end) ? i < end : i >= end; i += inc) {
        try {
          iValue.setValue(i);
          Value returnVal = instance.executeFunc(callBackFunc, funcParams);
          if (returnVal.getValue() != null && returnVal.getValue() instanceof Boolean) {
            if ((Boolean) returnVal.getValue()) {
              break;
            }
          }
        } catch (ConstantReassignmentException ex) {
          Logger.getLogger(Maps.class.getName()).log(Level.SEVERE, "Can't reassign a constant", ex);
        }
      }
      
      return null;
    };
  }
  
  @NativeJava
  public static NativeJavaCode next() {
    return (RowdyInstance instance, Object... params) -> {
      Iterator iter = (Iterator) params[0];
      return iter.hasNext()? iter.next() : null;
    };
  }
  
  @NativeJava
  public static NativeJavaCode Iterator() {
    return (RowdyInstance instance, Object... params) -> {
      Object collection = params[0];
      if (collection instanceof List) {
        return ((List)collection).iterator();
      } else if (collection instanceof HashMap) {
        return ((HashMap)collection).values().iterator();
      } else {
        return null;
      }
    };
  }
  
  @NativeJava
  public static NativeJavaCode forEach() {
    return (RowdyInstance instance, Object... params) -> {
      List<Object> values = (List<Object>) params[0];
      BaseNode callback = (BaseNode) params[1];
      
      BaseNode callBackFunc = instance.buildAndAllocateCallBack(callback);
      
      List<Value> callBackFuncParams = new LinkedList<>();
      Value callBackFuncValue = new Value(null, false);
      callBackFuncParams.add(callBackFuncValue);
      
      values.forEach((value) -> {
        try {
          callBackFuncValue.setValue(value);
          instance.executeFunc(callBackFunc, callBackFuncParams);
        } catch (ConstantReassignmentException ex) {
          Logger.getLogger(Maps.class.getName()).log(Level.SEVERE, null, ex);
        }
      });

      return null;
    };
  }
  
  @NativeJava
  public static NativeJavaCode List() {
    return (RowdyInstance instance, Object... params) -> {
      return new LinkedList<>();
    };
  }
  
  @NativeJava
  public static NativeJavaCode Map() {
    return (RowdyInstance instance, Object... params) -> {
      return new HashMap<>();
    };
  }
  
  @NativeJava
  public static NativeJavaCode get() {
    return (RowdyInstance instance, Object... params) -> {
      if (params[0] instanceof HashMap) {
        HashMap<String, Object> map = (HashMap<String, Object>) params[0];
        String key = params[1].toString();
        Object value = map.get(key);
        return value;
      } else if (params[0] instanceof List) {
        List<Object> list = (List<Object>) params[0];
        int index = Integer.parseInt(params[1].toString());
        return list.get(index);
      } else {
        return null;
      }
    };
  }
  
  @NativeJava
  public static NativeJavaCode add() {
    return (RowdyInstance instance, Object... params) -> {
      List<Object> list = (List<Object>) params[0];
      Object value = params[1];
      list.add(value);
      return value;
    };
  }
  
  @NativeJava
  public static NativeJavaCode set() {
    return (RowdyInstance instance, Object... params) -> {
      List<Object> list = (List<Object>) params[0];
      int index = (Integer.parseInt(params[1].toString()));
      Object value = params[2];
      list.remove(index);
      list.add(index, value);
      return value;
    };
  }
  
  @NativeJava
  public static NativeJavaCode remove() {
    return (RowdyInstance instance, Object... params) -> {
      List<Object> list = (List<Object>) params[0];
      Integer index = (Integer.parseInt(params[1].toString()));
      return list.remove(index);
    };
  }
  
  @NativeJava
  public static NativeJavaCode size() {
    return (RowdyInstance instance, Object... params) -> {
      if (params[0] instanceof HashMap) {
        return ((HashMap) params[0]).size();
      } else if (params[0] instanceof List) {
        List<Object> list = (List<Object>) params[0];
        return ((List<Object>) params[0]).size();
      } else {
        return 0;
      }
    };
  }
  
  @NativeJava
  public static NativeJavaCode put() {
    return (RowdyInstance instance, Object... params) -> {
      if (params[0] instanceof HashMap) {
        HashMap<String, Object> map = (HashMap<String, Object>) params[0];
        String key = params[1].toString();
        Object value = params[2];
        map.put(key, value);
        return value;
      } else {
        return null;
      }
    };
  }
}
