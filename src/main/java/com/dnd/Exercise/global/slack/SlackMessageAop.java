package com.dnd.Exercise.global.slack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Profile({"prod", "dev"})
@RequiredArgsConstructor
public class SlackMessageAop {

    private final RequestStorage requestStorage;
    private final SlackMessageGenerator slackMessageGenerator;
    private final SlackMessageSender slackMessageSender;

    @Before("@annotation(com.dnd.Exercise.global.slack.SlackAlarm)")
    public void appendExceptionToResponseBody(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (!validateIsException(args)) {
            return;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SlackAlarm annotation = signature.getMethod().getAnnotation(SlackAlarm.class);
        SlackAlarmErrorLevel level = annotation.level();

        String message = slackMessageGenerator
                .generate(requestStorage.get(), (Exception) args[0], level);

        slackMessageSender.send(message);
    }

    private boolean validateIsException(Object[] args) {
        if (!(args[0] instanceof Exception)) {
            log.warn("[SlackAlarm] argument is not Exception");
            return false;
        }
        return true;
    }
}
