package com.erp.service;

import com.erp.domain.dto.StudyCreateRequestDto;
import com.erp.domain.entity.Study;
import com.erp.domain.entity.User;
import com.erp.domain.repository.StudyRepository;
import com.erp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyCreateService {

    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    public void studyCreate(StudyCreateRequestDto dto, User user) {
        Study study = Study.builder()
                .title(dto.getName())
                .description(dto.getDescription())
                .maxMembers(dto.getMaxMembers())
                .createdBy(user)
                .build();

        studyRepository.save(study);

    }

}
