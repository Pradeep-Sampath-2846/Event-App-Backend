package app.eventapi.eventapiservice.domain.util.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.logging.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static app.eventapi.eventapiservice.domain.util.log.LogConstants.*;

@Aspect
@Component
@Slf4j
public class ServiceAspect {
    @Around("execution("
            + "* app.eventapi.eventapiservice.domain.service..*(..))")
    public Object logDomainServiceInfo(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        return logServiceData(proceedingJoinPoint);
    }

    @Around("execution("
            + "* app.eventapi.eventapiservice.application.controller..*(..))")
    public Object logPostMethod(ProceedingJoinPoint jointPoint)
            throws Throwable {
        return logRequest(jointPoint);
    }
    private Object logServiceData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object[] signatureArgs = proceedingJoinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) (proceedingJoinPoint.getSignature());
        String[] paramNames = methodSignature.getParameterNames();

        String argString = getArgumentString(paramNames, signatureArgs);

        log.info(DATA_IN_SVC, proceedingJoinPoint.getSignature().getName(), argString);

        Object result = proceedingJoinPoint.proceed();
        String resultString = getResultString(result);

        long elapsedTime = System.currentTimeMillis() - start;

        log.info(DATA_OUT_SVC, proceedingJoinPoint.getSignature().getName(), elapsedTime,
                resultString);
        return result;
    }
    private String getArgumentString(String[] paramNames, Object[] signatureArgs) {
        StringBuilder argString;
        argString = new StringBuilder();
        for (int i = 0; i < signatureArgs.length; i++) {
            argString.append(paramNames[i]).append(" - ")
                    .append((signatureArgs[i] == null) ? "null" : signatureArgs[i].toString()).append(" ");
        }
        return argString.toString();
    }
    private String getResultString(@Nullable Object result) {
        return (result == null) ? null : result.toString();
    }
    private Object logRequest(ProceedingJoinPoint jointPoint)
            throws Throwable {
        long start = System.currentTimeMillis();
        Object[] signatureArgs = jointPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) (jointPoint.getSignature());
        String[] paramNames = methodSignature.getParameterNames();

        String argString = getArgumentString(paramNames, signatureArgs);
        log.info(REQUEST_RECEIVED_CONTROLLER,
                jointPoint.getSignature().getName(),argString);
        Object result = jointPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        log.info(REQUEST_COMPLETE_CONTROLLER, jointPoint.getSignature().getName(),
                elapsedTime, getResultString(result));

        return result;
    }
}
