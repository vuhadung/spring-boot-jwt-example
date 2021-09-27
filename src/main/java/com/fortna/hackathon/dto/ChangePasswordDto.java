package com.fortna.hackathon.dto;

import javax.validation.constraints.NotEmpty;

public class ChangePasswordDto {

    @NotEmpty(message = "Old password can not be empty!")
    private String oldPassword;

    @NotEmpty(message = "New password can not be empty!")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
