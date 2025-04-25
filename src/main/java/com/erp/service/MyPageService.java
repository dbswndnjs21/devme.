package com.erp.service;

import com.erp.domain.entity.Study;
import com.erp.domain.repository.StudyJoinRequestRepository;
import com.erp.domain.repository.StudyRepository;
import com.erp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final StudyJoinRequestRepository joinRequestRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

}
