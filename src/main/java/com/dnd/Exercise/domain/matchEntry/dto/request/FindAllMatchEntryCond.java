package com.dnd.Exercise.domain.matchEntry.dto.request;

import com.dnd.Exercise.domain.match.entity.MatchType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindAllMatchEntryCond {

    private MatchType matchType;

    private EntryDirection entryDirection;
}
