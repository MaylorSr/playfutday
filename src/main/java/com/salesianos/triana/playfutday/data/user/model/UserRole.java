package com.salesianos.triana.playfutday.data.user.model;



public enum UserRole {

    ADMIN, USER;

    public static boolean contains(String text) {
        try {
            UserRole.valueOf(text);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }


}
