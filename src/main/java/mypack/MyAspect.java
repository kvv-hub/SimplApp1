package mypack;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class MyAspect {

    private Map<Class<?>, Integer> counter;
    private String strAspect;

    public String getStrAspect() {
        return strAspect;
    }

    public void setStrAspect(String strAspect) {
        this.strAspect = strAspect;
    }


    public MyAspect(){

    }

//    @Pointcut("execution(* *.logEvent(..))")
    @Pointcut("execution(* *.testMethod())")
    private void allLogEventMethods(){}

    @Before("allLogEventMethods()")
    public void logBefore(JoinPoint joinPoint){
        System.out.println("BEFORE: "+ joinPoint.getTarget().getClass().getSimpleName()
                + " "+ joinPoint.getSignature().getName());
        this.strAspect = "Ура!!!";
    }

    @AfterReturning(pointcut =  "allLogEventMethods()", returning = "retVal")
    public void logAfter(Object retVal){
        System.out.println("Return value : "+retVal);
    }
}
