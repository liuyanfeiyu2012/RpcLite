package com.xxx.rpclite.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by klose on 2017/1/26.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcLite {

  /**
   * 远端实现类类名，
   * 设置在类名上表示所有此类中方法公用此serviceName，
   * 设置在方法上可以覆盖设置在类名之上的值。
   *
   * @return
   */
  String serviceName() default "";

  /**
   * 远端被调用的方法名，不设置默认实用接口文件中的方法名称。
   *
   * @return
   */
  String methodName() default "";

  /**
   * 超时时间，不设置则使用默认超时时间30s，
   * 设置在类名上表示所有此类在方法公用此timeout值，
   * 设置在方法上可以覆盖设置在类名上的该值
   * @return
   */
  int timeout() default 0;

  /**
   * 重试次数，如果服务的接口天然是幂等的，那么建议不要设置该值，
   * 如果接口不是幂等的，那么务必将该值设置为0，
   * 设置在方法上，设置在类上的该属性自动忽略。
   *
   * < 0: 表示交给框架处理，
   * = 0: 表示不重试，
   * > 0: 按照设定的次数重试。
   * @return
   */
  int retry() default -1;
}
