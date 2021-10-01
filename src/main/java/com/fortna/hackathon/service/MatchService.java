package com.fortna.hackathon.service;

import java.util.List;

import com.fortna.hackathon.dto.CreateMatchDto;
import com.fortna.hackathon.dto.PaginationResponseDto;

public interface MatchService {

    void executeMatch(long id);
    
    void verifyMatch(long id);
    
    boolean createMatch(CreateMatchDto matchDto);
    
    PaginationResponseDto getAllMatchesForAdmin(int pageIndex, int pageSize);
    
    void publishMatchesResult(List<Long> matchIds);
    
    boolean canDownloadMatchResult(String username, Long matchId);
    
    String getPathToMatchResult(long matchId, boolean isAwayMatch);

}
