package com.fortna.hackathon.service;

import java.util.List;

import com.fortna.hackathon.dto.CreateMatchDto;
import com.fortna.hackathon.dto.MatchMgmtDto;

public interface MatchService {

    void executeMatch(long id);
    
    void verifyMatch(long id);
    
    boolean createMatch(CreateMatchDto matchDto);
    
    List<MatchMgmtDto> getAllMatchesForAdmin();
    
    void publishMatchesResult(List<Long> matchIds);
    
    boolean canDownloadMatchResult(String username, Long matchId);
    
    String getPathToMatchResult(long matchId, boolean isAwayMatch);

}
