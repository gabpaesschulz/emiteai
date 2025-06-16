package com.emiteai.api.exception;

public class DuplicateCpfException extends RuntimeException {
    public DuplicateCpfException() {
        super("CPF já cadastrado");
    }
}
