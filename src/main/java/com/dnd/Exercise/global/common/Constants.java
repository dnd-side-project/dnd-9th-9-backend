package com.dnd.Exercise.global.common;

public final class Constants {
    public static final String ASYNC_NOTIFICATION = "fcm notification";
    public static final String REDIS_CHEER_PREFIX = "CHEER_MEMBER";
    public static final String REDIS_NOTIFICATION_VERIFIED = "LIMIT";
    public static final String REDIS_WAKEUP_PREFIX = "WAKEUP_FIELD";

    public static final String ID_REGEXP = "^(?=.*[a-zA-z])(?=.*[0-9]).{6,15}$";
    public static final String PW_REGEXP = "^(?=.*[a-zA-z])(?=.*[0-9]).{8,16}$";
    public static final String PHONE_NUM_REGEXP = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";

    public static final String RANDOM_UID_BASE_STR = "matchup";
    public static final int RANDOM_UID_CODE_LENGTH = 4;
}
