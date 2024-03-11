package com.dnd.Exercise.domain.MemberEntry.event;

import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.TEAM_ACCEPT;

import com.dnd.Exercise.domain.MemberEntry.business.MemberEntryBusiness;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberEntryEventListener {

    private final MemberEntryBusiness memberEntryBusiness;
    private final NotificationService notificationService;
    private final UserFieldRepository userFieldRepository;

    @EventListener
    public void handleAcceptMemberEntryEvent(AcceptMemberEvent acceptMemberEvent) {
        User entrantUser = acceptMemberEvent.getEntrantUser();
        Field hostField = acceptMemberEvent.getHostField();

        userFieldRepository.save(UserField.from(entrantUser, hostField));
        memberEntryBusiness.deleteOrphanEntry(entrantUser, hostField);

        notificationService.sendUserNotification(TEAM_ACCEPT, hostField, entrantUser);
        notificationService.sendFieldNotification(TEAM_ACCEPT, hostField, entrantUser.getName());
    }
}
