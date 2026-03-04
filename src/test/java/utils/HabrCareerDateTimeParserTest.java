package utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class HabrCareerDateTimeParserTest {

    private final DateTimeParser parser = new HabrCareerDateTimeParser();

    @Test
    void correctDate() {
        String dateString = "2023-12-25T15:30:45";
        LocalDateTime result = parser.parse(dateString);
        assertThat(2023).isEqualTo(result.getYear());
        assertThat(12).isEqualTo(result.getMonth().getValue());
        assertThat(25).isEqualTo(result.getDayOfMonth());
        assertThat(15).isEqualTo(result.getHour());
        assertThat(30).isEqualTo(result.getMinute());
        assertThat(45).isEqualTo(result.getSecond());
    }

    @Test
    void beginningOfTheYear() {
        String dateString = "2023-01-01T00:00:00";
        LocalDateTime result = parser.parse(dateString);
        assertThat(2023).isEqualTo(result.getYear());
        assertThat(01).isEqualTo(result.getMonth().getValue());
        assertThat(01).isEqualTo(result.getDayOfMonth());
        assertThat(00).isEqualTo(result.getHour());
        assertThat(00).isEqualTo(result.getMinute());
        assertThat(00).isEqualTo(result.getSecond());
    }

    @Test
    void endOfTheYear() {
        String dateString = "2023-12-31T23:59:59";
        LocalDateTime result = parser.parse(dateString);
        assertThat(2023).isEqualTo(result.getYear());
        assertThat(12).isEqualTo(result.getMonth().getValue());
        assertThat(31).isEqualTo(result.getDayOfMonth());
        assertThat(23).isEqualTo(result.getHour());
        assertThat(59).isEqualTo(result.getMinute());
        assertThat(59).isEqualTo(result.getSecond());
    }

    @Test
    void incorrectDate() {
        String dateString = "2023-13-33T25:59:59";
        assertThatThrownBy(() -> parser.parse(dateString))
                .isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void parsingNull() {
        String dateString = null;
        assertThatThrownBy(() -> parser.parse(dateString))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void parsingLeapYear() {
        String dateString = "2024-02-29T12:00:00";
        LocalDateTime result = parser.parse(dateString);
        assertThat(2024).isEqualTo(result.getYear());
        assertThat(02).isEqualTo(result.getMonth().getValue());
        assertThat(29).isEqualTo(result.getDayOfMonth());
        assertThat(12).isEqualTo(result.getHour());
        assertThat(00).isEqualTo(result.getMinute());
        assertThat(00).isEqualTo(result.getSecond());
    }
}