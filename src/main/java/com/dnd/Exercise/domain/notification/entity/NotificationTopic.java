package com.dnd.Exercise.domain.notification.entity;

public enum NotificationTopic {
    CHEER("응원하기"),
    ALERT("깨우기"),
    MATCHING("매칭"),
    START("배틀 시작"),
    COMPLETE("배틀 종료");

    private String topic;

    private NotificationTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return topic;
    }
}
