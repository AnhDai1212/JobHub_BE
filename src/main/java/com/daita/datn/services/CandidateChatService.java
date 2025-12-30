package com.daita.datn.services;

import com.daita.datn.models.dto.CandidateChatRequest;
import com.daita.datn.models.dto.CandidateChatResponse;

public interface CandidateChatService {
    CandidateChatResponse matchCandidate(Integer jobId, CandidateChatRequest request);
}
