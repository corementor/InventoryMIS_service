package io.corementor.finexp.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import psychemesh.framework.common.dto.AbstractBaseDto;

import java.util.Set;

@Getter @Setter
@ToString
public class UserDto  extends AbstractBaseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private Set<RoleDto> role;

}
