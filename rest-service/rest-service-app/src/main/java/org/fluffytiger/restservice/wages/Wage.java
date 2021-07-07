package org.fluffytiger.restservice.wages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class Wage {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 32, message = "Name length must be between 2 and 32")
    private final String name;
    @NotBlank(message = "Surname is mandatory")
    @Size(min = 2, max = 32, message = "Surname length must be between 2 and 32")
    private final String surname;
    @PositiveOrZero(message = "Wage must be non-negative")
    private final double wage;
    @NotNull(message = "Event time is mandatory")
    private final LocalDateTime eventTime;

    public Wage(String name, String surname, double wage, LocalDateTime eventTime) {
        this.name = name;
        this.surname = surname;
        this.wage = wage;
        this.eventTime = eventTime;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public double getWage() {
        return wage;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }
}
