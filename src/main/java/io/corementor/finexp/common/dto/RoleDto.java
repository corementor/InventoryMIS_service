package io.corementor.finexp.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import psychemesh.framework.common.dto.AbstractBaseDto;

@Getter @Setter
@ToString
public class RoleDto extends AbstractBaseDto {
    private String roleName;
}
