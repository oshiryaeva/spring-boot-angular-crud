package com.shiryaeva.wyrgorod.validator;

import com.shiryaeva.wyrgorod.exception.MissingMandatoryFieldException;
import com.shiryaeva.wyrgorod.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidator {
    public void validate(Customer customer) {
        if (customer == null ||
                customer.getFirstName() == null ||
                customer.getLastName() == null ||
                customer.getEmail() == null) {
            throw new MissingMandatoryFieldException("Fill all mandatory fields please");
        }
    }
}
