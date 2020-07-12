package com.dol.ui.exceptions;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException() {
        super();
    }

    public FileNotFoundException(String err) {
        super(err);
    }
}
