package com.dnd.Exercise.domain.matchEntry.dto.response;

import com.dnd.Exercise.domain.userMatch.dto.response.FindAllMembersRes;
import java.util.List;
import lombok.Data;

@Data
public class FindAllTeamEntryRes {

    private List<FindAllMembersRes> teamEntriesInfos;
    private Long totalCount;
}
