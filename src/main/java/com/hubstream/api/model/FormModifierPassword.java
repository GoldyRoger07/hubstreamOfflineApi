package com.hubstream.api.model;

import lombok.Data;

@Data
public class FormModifierPassword {
    private String idCompte;

    private String ancienPassword;

    private String nouveauPassword;
}
