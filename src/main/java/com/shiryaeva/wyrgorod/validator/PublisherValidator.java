package com.shiryaeva.wyrgorod.validator;

import com.shiryaeva.wyrgorod.exception.MissingMandatoryFieldException;
import com.shiryaeva.wyrgorod.model.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherValidator {
    public void validate(Publisher publisher) {
        if (publisher == null || publisher.getName() == null) {
            throw new MissingMandatoryFieldException("Fill all mandatory fields please");
        }
    }
}
