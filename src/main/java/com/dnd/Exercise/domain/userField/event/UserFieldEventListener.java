package com.dnd.Exercise.domain.userField.event;

import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.EJECT;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.EXIT;

import com.dnd.Exercise.domain.BattleEntry.repository.BattleEntryRepository;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class UserFieldEventListener {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final BattleEntryRepository battleEntryRepository;

    @EventListener
    public void handleEjectMemberEvent(EjectMemberEvent ejectMemberEvent){
        List<Long> memberIds = ejectMemberEvent.getMemberIds();
        Field field = ejectMemberEvent.getField();

        List<User> targetUsers = userRepository.findByIdIn(memberIds);
        targetUsers.forEach(u -> notificationService.sendUserNotification(EJECT, field, u));
    }

    @EventListener
    public void handleExitFieldEvent(ExitFieldEvent exitFieldEvent){
        User user = exitFieldEvent.getUser();
        Field field = exitFieldEvent.getField();

        battleEntryRepository.deleteAllByHostField(field);
        battleEntryRepository.deleteAllByEntrantField(field);
        notificationService.sendFieldNotification(EXIT, field, user.getName());
    }
}
