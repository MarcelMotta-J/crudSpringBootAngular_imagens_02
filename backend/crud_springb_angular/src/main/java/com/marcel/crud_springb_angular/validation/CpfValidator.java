package com.marcel.crud_springb_angular.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCPF, String>{

    @Override
    public boolean isValid(
            String cpf,
            ConstraintValidatorContext context) {

        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int firstDigit = calculateDigit(
                cpf.substring(0, 9),
                10
        );

        if (firstDigit != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        int secondDigit = calculateDigit(
                cpf.substring(0, 10),
                11
        );

        return secondDigit ==
                Character.getNumericValue(cpf.charAt(10));
    }

    private int calculateDigit(String numbers, int weight) {

        int sum = 0;

        for (int i = 0; i < numbers.length(); i++) {

            int number =
                    Character.getNumericValue(numbers.charAt(i));

            sum += number * weight--;

        }

        int remainder = sum % 11;

        if (remainder < 2) {
            return 0;
        }

        return 11 - remainder;
    }
}
