package com.dnd.Exercise.domain.notification.entity;

public enum NotificationTopic {
    CHEER("응원하기"),
    ALERT("깨우기"),
    MATCHING("매칭"),
    START("배틀 시작"),
    COMPLETE("배틀 종료"),
    EJECT("팀원 퇴출"),
    EXIT("팀 나가기"),
    CHANGE_LEADER("방장 변경"),
    UPDATE_INFO("팀 정보 변경"),
    BATTLE_ACCEPT("배틀 성사"),
    TEAM_ACCEPT("팀 합류");

    private String topic;

    private NotificationTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return topic;
    }
}
