package com.dnd.Exercise.domain.BattleEntry.event;

import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.BATTLE_ACCEPT;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.BattleEntry.repository.BattleEntryRepository;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class BattleEntryEventListener {

    private final BattleEntryRepository battleEntryRepository;
    private final NotificationService notificationService;

    @EventListener
    public void handleAcceptBattleEntryEvent(AcceptBattleEvent acceptBattleEvent) {
        Field entrantField = acceptBattleEvent.getEntrantField();
        Field hostField = acceptBattleEvent.getHostField();

        battleEntryRepository.deleteAllByEntrantFieldOrHostField(entrantField, hostField);

        notificationService.sendFieldNotification(BATTLE_ACCEPT, hostField, entrantField.getName());
        notificationService.sendFieldNotification(BATTLE_ACCEPT, entrantField, hostField.getName());
    }


}

