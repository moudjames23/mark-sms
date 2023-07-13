package com.moudjames23.marksms.model.requests;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Length(min = 5, message = "Le nom doit avoir au moins 5 caractères")
    private String name;

    @Email(message = "L'email est invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Length(min = 9, max = 9, message = "Le numéro de téléphone doit avoir 9 chiffres")
    private String phone;

}
