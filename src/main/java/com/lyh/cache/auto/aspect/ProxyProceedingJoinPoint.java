package com.lyh.cache.auto.aspect;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;

class ProxyProceedingJoinPoint implements ProceedingJoinPoint {
  private JoinPoint point;

  public ProxyProceedingJoinPoint(JoinPoint point) {
    this.point = point;
  }

  public Object proceed() throws Throwable {

    return ((ProceedingJoinPoint) point).proceed();
  }

  public Object proceed(Object[] args) throws Throwable {
    return ((ProceedingJoinPoint) point).proceed(args);
  }

  public MethodSignature getMethodSignature() {
    return (MethodSignature) getSignature();
  }

  public Method getMethod() {
    return getMethodSignature().getMethod();
  }

  @Override
  public Object[] getArgs() {
    return this.point.getArgs();
  }

  @Override
  public String getKind() {
    return this.point.getKind();
  }

  @Override
  public Signature getSignature() {
    return this.point.getSignature();
  }

  @Override
  public SourceLocation getSourceLocation() {
    return this.point.getSourceLocation();
  }

  @Override
  public StaticPart getStaticPart() {
    return point.getStaticPart();
  }

  @Override
  public Object getTarget() {
    return point.getTarget();
  }

  @Override
  public Object getThis() {
    return point.getThis();
  }

  @Override
  public String toLongString() {
    return point.toLongString();
  }

  @Override
  public String toShortString() {
    return point.toShortString();
  }

  @Override
  public void set$AroundClosure(AroundClosure arc) {
    ((ProceedingJoinPoint) point).set$AroundClosure(arc);
  }

  /**
   * 获取短名称ClassName.methodName
   * 
   * @return
   */
  public String getShortName() {
    return getTarget().getClass().getSimpleName() + "." + this.getMethod().getName() + "()";
  }
}
