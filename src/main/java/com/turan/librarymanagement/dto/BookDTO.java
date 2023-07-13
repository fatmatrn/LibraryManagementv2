package com.turan.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @NotBlank(message = "Please provide title")
    private String title;
    @NotBlank(message = "Please provide author")
    private String author;
    @NotBlank(message = "Please provide isbn")
    private String isbn;
    @NotNull(message = "Please provide available copies")
    private int availableCopies;
}
