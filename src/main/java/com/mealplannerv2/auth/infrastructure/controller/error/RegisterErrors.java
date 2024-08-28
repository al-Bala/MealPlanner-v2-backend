package com.mealplannerv2.auth.infrastructure.controller.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterErrors {
    boolean isUsernameValid;
    boolean isEmailValid;

    public RegisterErrors() {
        this.isUsernameValid = true;
        this.isEmailValid = true;
    }

    public void setUsernameInvalid() {
        this.isUsernameValid = false;
    }

    public void setEmailInvalid() {
        this.isEmailValid = false;
    }
}
