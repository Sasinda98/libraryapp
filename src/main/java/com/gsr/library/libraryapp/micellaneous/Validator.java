package com.gsr.library.libraryapp.micellaneous;

import com.gsr.library.libraryapp.domain.Book;

public class Validator {
    private static Validator validator;

    private Validator(){

    }

    public static Validator getInstance(){
        if(validator == null){
            validator = new Validator();
            return validator;
        }
        return validator;
    }

    //Some basic validation.
    public Boolean isBookValid(Book book){
        Boolean isPropertiesValid = !book.getCategory().isEmpty()
                && !book.getTitle().isEmpty()
                && (book.getIsbn() > 0)
                && (book.getQuantity() >= 0)
                && (book.getBookID() >= 0) ;
        return isPropertiesValid;
    }
}
