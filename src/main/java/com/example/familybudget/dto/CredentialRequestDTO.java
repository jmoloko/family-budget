package com.example.familybudget.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class CredentialRequestDTO {
    @JsonView({JsonViews.ShortView.class})
    private String email;
    @JsonView({JsonViews.FullView.class})
    private String name;
    @JsonView({JsonViews.ShortView.class})
    private String password;
}
