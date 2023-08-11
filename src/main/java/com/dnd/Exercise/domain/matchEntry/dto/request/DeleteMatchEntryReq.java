package com.dnd.Exercise.domain.matchEntry.dto.request;

import com.dnd.Exercise.domain.match.entity.MatchType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteMatchEntryReq {
    private MatchType matchType;

    private EntryDirection entryDirection;
}
