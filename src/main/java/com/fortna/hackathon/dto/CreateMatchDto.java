package com.fortna.hackathon.dto;

public class CreateMatchDto {
    
    private Long roundId;

    private Long firstPlayerId;

    private Long secondPlayerId;

    private Long mainCourseId;

    private Long backupCourseId;

    public Long getFirstPlayerId() {
        return firstPlayerId;
    }

    public void setFirstPlayerId(Long firstPlayerId) {
        this.firstPlayerId = firstPlayerId;
    }

    public Long getSecondPlayerId() {
        return secondPlayerId;
    }

    public void setSecondPlayerId(Long secondPlayerId) {
        this.secondPlayerId = secondPlayerId;
    }

    public Long getMainCourseId() {
        return mainCourseId;
    }

    public void setMainCourseId(Long mainCourseId) {
        this.mainCourseId = mainCourseId;
    }

    public Long getBackupCourseId() {
        return backupCourseId;
    }

    public void setBackupCourseId(Long backupCourseId) {
        this.backupCourseId = backupCourseId;
    }

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

}
