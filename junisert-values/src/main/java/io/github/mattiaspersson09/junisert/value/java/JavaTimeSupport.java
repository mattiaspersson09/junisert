/*
 * Copyright (c) 2025-2025 Mattias Persson
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

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.SupportBuilder;

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
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.Locale;

final class JavaTimeSupport {
    private JavaTimeSupport() {
    }

    static AggregatedValueGenerator getTimeSupport() {
        return SupportBuilder.createSupport()
                .support(TemporalAccessor.class)
                .withImplementation(Instant.class, Instant::now)
                .withImplementation(LocalDate.class, LocalDate::now)
                .withImplementation(LocalDateTime.class, LocalDateTime::now)
                .withImplementation(LocalTime.class, LocalTime::now)
                .withImplementation(MonthDay.class, MonthDay::now)
                .withImplementation(OffsetDateTime.class, OffsetDateTime::now)
                .withImplementation(OffsetTime.class, OffsetTime::now)
                .withImplementation(Year.class, Year::now)
                .withImplementation(YearMonth.class, YearMonth::now)
                .withImplementation(ZonedDateTime.class, ZonedDateTime::now)
                .withImplementation(ZoneOffset.class, () -> ZoneOffset.ofHours(0))
                .withImplementation(HijrahDate.class, HijrahDate::now)
                .withImplementation(JapaneseDate.class, JapaneseDate::now)
                .withImplementation(MinguoDate.class, MinguoDate::now)
                .withImplementation(ThaiBuddhistDate.class, ThaiBuddhistDate::now)
                .support(TemporalAmount.class)
                .withImplementation(Duration.class, () -> Duration.ofSeconds(60))
                .withImplementation(Period.class, () -> Period.ofDays(1))
                .supportSingle(Clock.class, Clock::systemDefaultZone)
                .supportSingle(DateTimeFormatter.class, () -> DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .supportSingle(ZoneId.class, () -> ZoneId.of("Europe/Stockholm"))
                .supportSingle(TemporalField.class, ChronoField.class, () -> ChronoField.SECOND_OF_MINUTE)
                .supportSingle(TemporalUnit.class, ChronoUnit.class, () -> ChronoUnit.SECONDS)
                .support(Chronology.class)
                .withImplementation(JapaneseChronology.class, () -> JapaneseChronology.INSTANCE)
                .withImplementation(HijrahChronology.class, () -> HijrahChronology.INSTANCE)
                .withImplementation(IsoChronology.class, () -> IsoChronology.INSTANCE)
                .withImplementation(MinguoChronology.class, () -> MinguoChronology.INSTANCE)
                .withImplementation(ThaiBuddhistChronology.class, () -> ThaiBuddhistChronology.INSTANCE)
                .supportSingle(DecimalStyle.class, () -> DecimalStyle.of(Locale.ENGLISH))
                .supportSingle(ValueRange.class, () -> ValueRange.of(0, 1))
                .build();
    }
}
