package org.yangyi.project.annotation;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class TestAnnotationAspect {

    private static final Logger log = LoggerFactory.getLogger(TestAnnotationAspect.class);

    @Pointcut("@annotation(org.yangyi.project.annotation.TestAnnotation)")
    public void annotationPointCut() {

    }

    @Before("annotationPointCut()")
    public void before(JoinPoint joinPoint) {
        System.err.println("Before");
    }

    @AfterReturning(value = "annotationPointCut()", returning = "keys")
    public void doAfterReturningAdvice(JoinPoint joinPoint, Object keys) {
        System.err.println("AfterReturning");
    }

    @AfterThrowing(value = "annotationPointCut()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        log.info("发生了空指针异常!!!!!");
    }

    @After(value = "annotationPointCut()")
    public void doAfterAdvice(JoinPoint joinPoint) {
        System.err.println("After");
    }

    @Around("annotationPointCut()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        Object[] args = proceedingJoinPoint.getArgs();   //  获取的方法参数
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        TestAnnotation testAnnotation = method.getAnnotation(TestAnnotation.class);
        String data = "{'name':'yangyi'}";
        args[0] = JSON.parseObject(data, testAnnotation.clazz());
        Object obj = proceedingJoinPoint.proceed(args);
        log.info("返回数据:" + obj);
        return obj;
    }

}
