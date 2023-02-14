package com.salesianos.triana.playfutday.exception;

import javax.persistence.EntityNotFoundException;

public class GlobalEntityNotFounException extends EntityNotFoundException {

    public GlobalEntityNotFounException(String content) {
        super(String.format(content));
    }
}
