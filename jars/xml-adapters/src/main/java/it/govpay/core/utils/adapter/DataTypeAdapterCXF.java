/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package it.govpay.core.utils.adapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

import jakarta.xml.bind.DatatypeConverter;

public final class DataTypeAdapterCXF {
	private static final String PATTERN_OFFSET = ".*([+-]\\d{2}:\\d{2}|Z)$";
	private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER;
    static {
    	LOCAL_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral('T')
                .appendValue(ChronoField.HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                .toFormatter();
    }

    private DataTypeAdapterCXF() {
    }

    public static Date parseDate(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        return DatatypeConverter.parseDate(s).getTime();
    }
    public static String printDate(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return DatatypeConverter.printDate(c);
    }

    public static LocalDate parseLocalDate(String value) {
    	if (value == null || value.isEmpty()) {
            return null;
        }
        
        // Verifica se la stringa termina con un offset nel formato +HH:mm o -HH:mm
        if (value.matches(PATTERN_OFFSET)) {
        	// Rimuove l'offset se presente
            value = value.replaceFirst("([+-]\\d{2}:\\d{2}|Z)$", "");
        }

        // La stringa non contiene offset: uso ISO_LOCAL_DATE
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
    }

	public static String printLocalDate(LocalDate value) {
    	return value != null ? DateTimeFormatter.ISO_LOCAL_DATE.format(value) : null;
    }

    public static Date parseTime(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        return DatatypeConverter.parseTime(s).getTime();
    }
    public static String printTime(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return DatatypeConverter.printTime(c);
    }

    public static LocalTime parseLocalTime(String value) {
    	if (value == null || value.isEmpty()) {
            return null;
        }
        
        // Verifica se la stringa termina con un offset nel formato +HH:mm o -HH:mm
        if (value.matches(PATTERN_OFFSET)) {
            // La stringa contiene offset: uso ISO_OFFSET_TIME
        	OffsetTime odt = OffsetTime.parse(value, DateTimeFormatter.ISO_OFFSET_TIME);
            return odt.toLocalTime();
        } else {
            // La stringa non contiene offset: uso ISO_LOCAL_TIME
            return LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME);
        }
    }

	public static String printLocalTime(LocalTime value) {
    	return value != null ? DateTimeFormatter.ISO_LOCAL_TIME.format(value) : null;
    }


    public static Date parseDateTime(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        return DatatypeConverter.parseDateTime(s).getTime();
    }
    public static String printDateTime(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return DatatypeConverter.printDateTime(c);
    }

    public static LocalDateTime parseLocalDateTime(String value) {
    	if (value == null || value.isEmpty()) {
            return null;
        }
        
        // Verifica se la stringa termina con un offset nel formato +HH:mm o -HH:mm
        if (value.matches(PATTERN_OFFSET)) {
            // La stringa contiene offset: uso ISO_OFFSET_DATE_TIME
            OffsetDateTime odt = OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return odt.toLocalDateTime();
        } else {
            // La stringa non contiene offset: uso ISO_LOCAL_DATE_TIME
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

	public static String printLocalDateTime(LocalDateTime value) {
    	return value != null ? LOCAL_DATE_TIME_FORMATTER.format(value) : null;
    }
}
