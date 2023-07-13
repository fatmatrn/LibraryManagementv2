package com.turan.librarymanagement.domain.enums;

public enum RoleType {

    ROLE_MEMBER("Borrower"),
    ROLE_ADMIN("Administrator");

    private String name;
    private RoleType(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
