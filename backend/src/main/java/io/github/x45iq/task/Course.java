package io.github.x45iq.task;

import java.util.UUID;

public record Course(UUID id,String title) {
    public Course(String title){
        this(UUID.randomUUID(),title);
    }
}
