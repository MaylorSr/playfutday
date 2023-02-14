package com.salesianos.triana.playfutday.exception;

import javax.persistence.EntityNotFoundException;

public class GlobalEntityListNotFounException extends EntityNotFoundException {
    public GlobalEntityListNotFounException(String content) {
        super(String.format(content));
    }
}
