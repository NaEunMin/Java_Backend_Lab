package java.date;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeTest {

    @Test
    public void 불변성과_날짜계산을_테스트한다() {
        LocalDate original = LocalDate.of(2024, 2, 28);
        LocalDate next = original.plusDays(1);

        assertEquals(LocalDate.of(2024, 2, 28), original);
        assertEquals(LocalDate.of(2024, 2, 29), next);
    }

    @Test
    public void 잘못된_날짜_입력시_예외를_테스트한다() {
        assertThrows(DateTimeException.class, () -> {
            LocalDate.of(2025, 2, 29);
        });
    }

    @Test
    public void 서울시각을_Instant로_바꾼다() {
        LocalDateTime local = LocalDateTime.of(2026, 8, 29, 12, 0);
        ZoneId seoul = ZoneId.of("Asia/Seoul");
        ZonedDateTime zoned = local.atZone(seoul);
        Instant instant = zoned.toInstant();

        assertEquals(Instant.parse("2026-08-29T03:00:00Z"), instant);
    }

    @Test
    public void 서울시각으로만든Instant를_UTC로_바꾼다() {
        LocalDateTime local = LocalDateTime.of(2026, 8, 29, 12, 0);
        ZoneId seoul = ZoneId.of("Asia/Seoul");
        ZonedDateTime zoned = local.atZone(seoul);
        Instant instant = zoned.toInstant();

        ZonedDateTime actual = instant.atZone(ZoneOffset.UTC);
        ZonedDateTime expected = ZonedDateTime.of(
                2026, 8, 29,
                3, 0, 0, 0,
                ZoneOffset.UTC
        );

        assertEquals(expected, actual);
    }

    @Test
    public void 고정시각_Clock_을_테스트한다() {
        Clock clock = Clock.fixed(Instant.parse("2026-08-29T03:00:00Z"), ZoneId.of("Asia/Seoul"));

        LocalDateTime actualLocalDateTime = LocalDateTime.now(clock);
        Instant actualInstant = Instant.now(clock);

        LocalDateTime expectedLocalDateTime = LocalDateTime.of(2026, 8, 29, 12, 0);

        Instant expectedInstant = Instant.parse("2026-08-29T03:00:00Z");

        assertEquals(expectedLocalDateTime, actualLocalDateTime);
        assertEquals(expectedInstant, actualInstant);
    }
}
