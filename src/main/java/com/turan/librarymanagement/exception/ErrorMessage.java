package com.turan.librarymanagement.exception;

public class ErrorMessage {

    public final static String BORROWER_NOT_FOUND_EXCEPTION = "Borrower not found ";
    public final static String BOOK_NOT_FOUND_EXCEPTION = "Book not found with : %s  ";
    public final static String EMAIL_ALREADY_EXIST_MESSAGE = " Email : %s already exists";
    public final static String ROLE_NOT_FOUND_EXCEPTION = "Role not found with : %s  ";
    public static final String NOT_AVAILABLE_BOOK = "The Book is not available with title : %s ";
    public static final String NOT_EXIST_YOUR_LIST = "The book does not exist in your list";
    public static final String SAME_BOOK_MESSAGE = "You cannot borrow the same book";
    public static final String NOT_RETURNED_BOOK_MESSAGE = "Borrower has three books. This Borrower has not returned the books yet.";
    public static final String NOT_AVAILABLE_BOOK_MESSAGE = "The book is not available in our library ";
    public static final String THE_BOOK_ALREADY_CREATED_MESSAGE = "This book has already been created by Admin";
    public static final String THE_BOOK_LOAN_STATUS_MESSAGE = "This book has loaned by Borrowers";
}
