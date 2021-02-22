package com.github.tedteted;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller("/filer")
public class FilerController {

    @Inject
    FilerService filerService;

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public String listAll(){
        return filerService.listAllNamesFromFile();
    }

    @Get("/add/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String addFile(String name) throws FileNotFoundException {
        return filerService.appendNameToFile(name);
    }

    @Get("/remove/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String removeFile(String name) throws IOException {
        return filerService.removeNameFromFile(name);
    }
}