package com.xxx.rpclite.error;

/**
 * Created by klose on 2017/2/3.
 */
public class ErrorMaker {

  public static void main(String[] args) throws Exception {

    Class<Exception> aClass = Exception.class;
    Throwable exception = aClass.getConstructor(String.class).newInstance("one exception");
////    Object exception = aClass.getConstructor(String.class).newInstance();
//
    try {
      System.out.println("sddffg");
      throw exception;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("yuguygguk");
    } catch (Throwable throwable) {
      System.out.println("sd");
      throwable.printStackTrace();
    }
//    Class<A> aClass = A.class;
//    A a = aClass.getConstructor(String.class).newInstance("xxx");
//    System.out.println(a.a);

  }

  static class A{
    String a;


    public A(String a) {
      this.a = a;
    }

    public String getA() {
      return a;
    }

    public void setA(String a) {
      this.a = a;
    }
  }


}
