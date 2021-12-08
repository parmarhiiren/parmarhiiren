package com.hiiren.springbootgraphql.service.datafetcher;

import com.hiiren.springbootgraphql.model.Book;
import com.hiiren.springbootgraphql.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookDataFetcher implements DataFetcher<Book> {
    @Autowired
    BookRepository bookRepository;

    @Override
    public Book get(DataFetchingEnvironment dataFetchingEnvironment) {
        // id : is the name given in the books.graphql file
        String id = dataFetchingEnvironment.getArgument("id");
        return bookRepository.findById(id).get();
    }
}
