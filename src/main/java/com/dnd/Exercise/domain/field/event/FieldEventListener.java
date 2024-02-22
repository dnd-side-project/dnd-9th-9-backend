package com.dnd.Exercise.domain.field.event;

import com.dnd.Exercise.domain.MemberEntry.repository.MemberEntryRepository;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.BattleEntry.repository.BattleEntryRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FieldEventListener {

    private final UserFieldRepository userFieldRepository;
    private final MemberEntryRepository memberEntryRepository;

    @EventListener
    public void handleFieldCreateEvent(CreateEvent createEvent) {
        User creator = createEvent.getCreator();
        Field field = createEvent.getField();

        UserField userField = new UserField(creator, field);
        userFieldRepository.save(userField);

        memberEntryRepository.deleteAllByEntrantUserIdAndType(creator.getId(), field.getFieldType());
    }


}
