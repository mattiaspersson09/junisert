/*
 * Copyright (c) 2025-2026 Mattias Persson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.mattiaspersson09.junisert.value.java;


import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.AbstractChronology;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoChronology;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JavaTimeSupportIntegrationTest extends JavaInternalIntegrationTest {
    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            AbstractChronology.class,
            ChronoLocalDate.class,
            ChronoLocalDateTime.class,
            Chronology.class,
            ChronoPeriod.class,
            Clock.class,
            Temporal.class,
            TemporalAmount.class,
            TemporalAccessor.class,
            TemporalAmount.class,
            TemporalField.class,
            TemporalUnit.class,
            // Implementations
            DateTimeFormatter.class,
            Duration.class,
            HijrahChronology.class,
            HijrahDate.class,
            Instant.class,
            IsoChronology.class,
            JapaneseChronology.class,
            JapaneseDate.class,
            LocalDate.class,
            LocalDateTime.class,
            DecimalStyle.class,
            LocalTime.class,
            MonthDay.class,
            MinguoChronology.class,
            MinguoDate.class,
            OffsetDateTime.class,
            OffsetTime.class,
            Period.class,
            ThaiBuddhistChronology.class,
            ThaiBuddhistDate.class,
            ValueRange.class,
            Year.class,
            YearMonth.class,
            ZonedDateTime.class,
            ZoneId.class,
            ZoneOffset.class,
    })
    void javaTime(Class<?> type) {
        assertIsSupported(type);
    }
}
