package com.dnd.Exercise.domain.userField.service;

import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import java.util.List;

public interface UserFieldService {

    List<FindAllMembersRes> findAllMembers(Long fieldId);
}
