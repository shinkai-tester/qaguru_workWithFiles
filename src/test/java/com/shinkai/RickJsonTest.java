package com.shinkai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinkai.model.RickMortyCharacter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

public class RickJsonTest {

    private final ClassLoader cl = RickJsonTest.class.getClassLoader();
    @Test
    void jsonTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("Rick_Sanchez.json");
             InputStreamReader isr = new InputStreamReader(is)) {
            ObjectMapper mapper = new ObjectMapper();

            // convert a JSON string to a RickMortyCharacter object
            RickMortyCharacter rick = mapper.readValue(isr, RickMortyCharacter.class);

            Assertions.assertEquals("Rick Sanchez", rick.getName());
            Assertions.assertEquals("Earth (C-137)", rick.getOrigin().getName());
            assertThat(rick.getEpisodes(), hasItems(
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/13"
            ));
        }
    }
}
