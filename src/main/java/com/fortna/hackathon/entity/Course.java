package com.fortna.hackathon.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "COURSES")
public class Course {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COURSE_GENERATOR")
    @SequenceGenerator(name = "COURSE_GENERATOR", sequenceName = "COURSE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "COURSE_NAME")
    private String name;

    @Column(name = "SUBMISSION_DEADLINE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date deadline;

    @Column(name = "IS_BACKUP")
    @JsonIgnore
    private Boolean backup;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updatedDate;

    @Column(name = "PATH_TO_FILE")
    @JsonIgnore
    private String pathToFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Boolean getBackup() {
        return backup;
    }

    public void setBackup(boolean isBackup) {
        this.backup = isBackup;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

}
