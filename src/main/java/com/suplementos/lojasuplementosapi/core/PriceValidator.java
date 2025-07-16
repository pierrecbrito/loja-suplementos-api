package com.suplementos.lojasuplementosapi.core;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.math.BigDecimal;

@Documented
@Constraint(validatedBy = PriceValidator.PriceConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PriceValidator {
    
    String message() default "O preço deve ser maior que zero";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    class PriceConstraintValidator implements ConstraintValidator<PriceValidator, BigDecimal> {
        @Override
        public void initialize(PriceValidator constraintAnnotation) {
            // Inicialização, se necessário
        }
        
        @Override
        public boolean isValid(BigDecimal price, ConstraintValidatorContext context) {
            return price != null && price.compareTo(BigDecimal.ZERO) > 0;
        }
    }
}
