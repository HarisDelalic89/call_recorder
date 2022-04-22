package com.qtelecom.call_recorder.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    private static final String DEFAULT_FIRSTNAME = "firstName";
    private static final String DEFAULT_LASTNAME = "lastName";
    private static final String DEFAULT_EMAIL = "firstName@lastName.com";
    private static final Integer DEFAULT_AGE = 10;

    @Nested
    @DisplayName("Tests for the JPA validations")
    class ValidationTest {
        private Validator validator;

        @BeforeEach
        public void setUp() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        @Test
        public void testFirstNameIsNotBlank() {
            ConstraintViolation<Customer> validationError =
                    getViolationForFields(DEFAULT_EMAIL, "", DEFAULT_LASTNAME, DEFAULT_AGE);
            validate(validationError, "must not be blank", "firstName");
        }

        @Test
        public void testLastNameIsNotBlank() {
            ConstraintViolation<Customer> validationError =
                    getViolationForFields(DEFAULT_EMAIL, DEFAULT_FIRSTNAME, "", DEFAULT_AGE);
            validate(validationError, "must not be blank", "lastName");
        }

        @Test
        public void testEmailIsNotNull() {
            ConstraintViolation<Customer> validationError =
                    getViolationForFields("", DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, DEFAULT_AGE);
            validate(validationError, "must not be blank", "email");
        }

        @Test
        public void testEmailIsInValidFormat() {
            ConstraintViolation<Customer> validationError =
                    getViolationForFields("invalid_invalid.com", DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, DEFAULT_AGE);
            validate(validationError, "must be a well-formed email address", "email");
        }

        @Test
        public void testAgeIsNotNull() {
            ConstraintViolation<Customer> validationError =
                    getViolationForFields(DEFAULT_EMAIL, DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, null);
            validate(validationError, "must not be null", "age");
        }

        @Test
        public void testAgeIsPositiveNumber() {
            ConstraintViolation<Customer> validationError =
                    getViolationForFields(DEFAULT_EMAIL, DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, -5);
            validate(validationError, "must be greater than 0", "age");
        }

        private void validate(ConstraintViolation<Customer> validationError, String expectedMessage, String expectedProperty) {
            assertAll("Test that Property has exact error",
                    () -> assertEquals(
                                expectedProperty,
                                validationError.getPropertyPath().toString()),
                    () -> assertEquals(
                                expectedMessage,
                                validationError.getMessage())
                    );
        }

        private ConstraintViolation<Customer> getViolationForFields(String email, String firstName, String lastName, Integer age) {
            Customer customer = getCustomer(email, firstName, lastName, age);
            Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
            return violations.iterator().next();
        }
    }

    @Test
    public void shouldCreateSearchNameByConcatenatingFirstNameAndLastName() {
        Customer customer = getCustomer("", DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, null);

        customer.calculateSearchName();

        assertEquals(DEFAULT_FIRSTNAME+DEFAULT_LASTNAME, customer.getSearchName());
    }

    private static Customer getCustomer(String email, String firstName, String lastName, Integer age) {
        return Customer.builder().email(email).firstName(firstName).lastName(lastName).age(age).build();
    }
}