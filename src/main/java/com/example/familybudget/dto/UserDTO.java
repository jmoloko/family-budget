package com.example.familybudget.dto;

import com.example.familybudget.entity.Role;
import com.example.familybudget.entity.Status;
import com.example.familybudget.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @JsonView({JsonViews.ShortView.class})
    private Long id;
    @JsonView({JsonViews.ShortView.class})
    private String email;
    @JsonView({JsonViews.ShortView.class})
    private String name;
    @JsonView({JsonViews.FullView.class})
    private Role role;
    @JsonView({JsonViews.FullView.class})
    private Status status;

    public static UserDTO toDto(UserEntity entity){
        UserDTO userDto = new UserDTO();
        userDto.setId(entity.getId());
        userDto.setEmail(entity.getEmail());
        userDto.setName(entity.getName());
        userDto.setRole(entity.getRole());
        userDto.setStatus(entity.getStatus());
        return userDto;
    }
}
