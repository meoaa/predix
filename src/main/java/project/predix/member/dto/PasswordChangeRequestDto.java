package project.predix.member.dto;

import lombok.Data;

@Data
public class PasswordChangeRequestDto {
    private String oldPassword;
    private String newPassword;
    private String newPassword2;
}
