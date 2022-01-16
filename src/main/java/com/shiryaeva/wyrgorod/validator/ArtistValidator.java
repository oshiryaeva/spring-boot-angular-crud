package com.shiryaeva.wyrgorod.validator;

import com.shiryaeva.wyrgorod.exception.MissingMandatoryFieldException;
import com.shiryaeva.wyrgorod.model.Artist;
import org.springframework.stereotype.Component;

@Component
public class ArtistValidator {
    public void validate(Artist artist) {
        if (artist == null || artist.getName() == null) {
            throw new MissingMandatoryFieldException("Fill all mandatory fields please");
        }
    }
}
