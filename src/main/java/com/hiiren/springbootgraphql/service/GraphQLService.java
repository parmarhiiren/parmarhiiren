package com.hiiren.springbootgraphql.service;

import com.hiiren.springbootgraphql.model.Book;
import com.hiiren.springbootgraphql.repository.BookRepository;
import com.hiiren.springbootgraphql.service.datafetcher.AllBooksDataFetcher;
import com.hiiren.springbootgraphql.service.datafetcher.BookDataFetcher;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class GraphQLService {

    GraphQL graphQL;

    @Value("classpath:books.graphql")
    Resource resource;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private AllBooksDataFetcher allBooksDataFetcher;

    @Autowired
    private BookDataFetcher bookDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException {
        loadDataIntoH2();
        // Get the Schema
        File schemaFile = resource.getFile();

        // Parse the schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private void loadDataIntoH2() {
        Stream.of(
                new Book("123", "Big Data", "Kindle Edition",
                        new String[] {
                                "Vivesh"
                        }, "Nov 2017"),
                new Book("124", "Java", "Orielly",
                        new String[] {
                                "Kathy", "Berts"
                        }, "Jan 2015"),
                new Book("125", "Learn Microprofile", "Orielly",
                        new String[] {
                                "Manish", "Kumar"
                        }, "Dec 2016")
        ).forEach(book -> {
            bookRepository.save(book);
        });
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("allBooks", allBooksDataFetcher)
                        .dataFetcher("findBook", bookDataFetcher))
                .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }

}
