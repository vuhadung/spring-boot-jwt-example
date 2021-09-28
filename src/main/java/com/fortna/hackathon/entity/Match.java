package com.fortna.hackathon.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MATCHES")
public class Match {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MATCH_GENERATOR")
    @SequenceGenerator(name = "MATCH_GENERATOR", sequenceName = "MATCH_SEQ", allocationSize = 1)
    private Long id;

    // Round
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUND_ID")
    private Round round;

    // Players
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_0_ID")
    private User player0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_1_ID")
    private User player1;

    // Courses
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_ID")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BACKUP_COURSE_ID")
    private Course backupCourse;

    // Match's winner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WINNER_ID")
    private User finalWinner;

    @Column(name = "RESULT_PUBLISHED")
    private Boolean resultPublished;

    // Ket qua luot di va luot ve
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AWAY_MATCH_WINNER_ID")
    private User awayMatchWinner;

    @Column(name = "PATH_TO_AWAY_MATCH_RESULT")
    private String pathToAwayMatchResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOME_MATCH_WINNER_ID")
    private User homeMatchWinner;

    @Column(name = "PATH_TO_HOME_MATCH_RESULT")
    private String pathToHomeMatchResult;

    // this determines players for the next match
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_MATCH_0_ID")
    private Match parentMatch0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_MATCH_1_ID")
    private Match parentMatch1;

    // Others
    @Column(name = "ERROR_MESSAGE", length = 1000)
    private String errorMessage;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPlayer0() {
        return player0;
    }

    public void setPlayer0(User player0) {
        this.player0 = player0;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean isResultPublished() {
        return resultPublished;
    }

    public void setResultPublished(boolean resultPublished) {
        this.resultPublished = resultPublished;
    }

    public Course getBackupCourse() {
        return backupCourse;
    }

    public void setBackupCourse(Course backupCourse) {
        this.backupCourse = backupCourse;
    }

    public User getFinalWinner() {
        return finalWinner;
    }

    public void setFinalWinner(User finalWinner) {
        this.finalWinner = finalWinner;
    }

    public User getAwayMatchWinner() {
        return awayMatchWinner;
    }

    public void setAwayMatchWinner(User awayMatchWinner) {
        this.awayMatchWinner = awayMatchWinner;
    }

    public String getPathToAwayMatchResult() {
        return pathToAwayMatchResult;
    }

    public void setPathToAwayMatchResult(String pathToAwayMatchResult) {
        this.pathToAwayMatchResult = pathToAwayMatchResult;
    }

    public User getHomeMatchWinner() {
        return homeMatchWinner;
    }

    public void setHomeMatchWinner(User homeMatchWinner) {
        this.homeMatchWinner = homeMatchWinner;
    }

    public String getPathToHomeMatchResult() {
        return pathToHomeMatchResult;
    }

    public void setPathToHomeMatchResult(String pathToHomeMatchResult) {
        this.pathToHomeMatchResult = pathToHomeMatchResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

}
