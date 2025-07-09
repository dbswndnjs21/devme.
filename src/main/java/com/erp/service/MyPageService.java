package com.erp.service;

import com.erp.repository.StudyJoinRequestRepository;
import com.erp.repository.StudyRepository;
import com.erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final StudyJoinRequestRepository joinRequestRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

}
