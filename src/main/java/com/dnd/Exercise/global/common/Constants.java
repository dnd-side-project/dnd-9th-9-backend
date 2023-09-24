package com.dnd.Exercise.global.common;

public final class Constants {
    public static final String ASYNC_NOTIFICATION = "fcm notification";
    public static final String REDIS_CHEER_PREFIX = "CHEER_MEMBER";
    public static final String REDIS_NOTIFICATION_VERIFIED = "LIMIT";
    public static final String REDIS_WAKEUP_PREFIX = "WAKEUP_FIELD";

    public static final String ID_REGEXP = "^(?=.*[a-zA-z])(?=.*[0-9]).{6,15}$";
    public static final String PW_REGEXP = "^(?=.*[a-zA-z])(?=.*[0-9]).{8,16}$";
}
