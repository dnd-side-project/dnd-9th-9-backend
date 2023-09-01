package com.dnd.Exercise.domain.notification.service;

import com.dnd.Exercise.domain.user.entity.User;

public interface NotificationService {

    void cheerMember(User user, Long id);
}
