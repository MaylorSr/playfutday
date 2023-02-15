package com.salesianos.triana.playfutday.exception;


import org.springframework.security.access.AccessDeniedException;

public class NotPermission extends AccessDeniedException {
    public NotPermission(){
        super(String.format("Not have permission!"));
    }

}
