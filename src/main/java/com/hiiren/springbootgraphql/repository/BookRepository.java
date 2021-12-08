package com.hiiren.springbootgraphql.repository;

import com.hiiren.springbootgraphql.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
