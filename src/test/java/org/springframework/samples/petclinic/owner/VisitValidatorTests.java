/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

/**
 * Test class for {@link VisitValidator}
 */
@DisabledInNativeImage
class VisitValidatorTests {

	private VisitValidator visitValidator;

	private Visit visit;

	private Errors errors;

	@BeforeEach
	void setUp() {
		visitValidator = new VisitValidator();
		visit = new Visit();
		errors = new MapBindingResult(new HashMap<>(), "visit");
	}

	@Test
	void supportsVisitClass() {
		assertTrue(visitValidator.supports(Visit.class));
	}

	@Test
	void doesNotSupportNonVisitClass() {
		assertFalse(visitValidator.supports(String.class));
	}

	@Test
	void validateWithFutureDate() {
		visit.setDate(LocalDate.now().plusDays(1));
		visit.setDescription("Checkup");

		visitValidator.validate(visit, errors);

		assertFalse(errors.hasFieldErrors("date"));
	}

	@Test
	void validateWithTodayDate() {
		visit.setDate(LocalDate.now());
		visit.setDescription("Checkup");

		visitValidator.validate(visit, errors);

		assertTrue(errors.hasFieldErrors("date"));
	}

	@Test
	void validateWithPastDate() {
		visit.setDate(LocalDate.now().minusDays(1));
		visit.setDescription("Checkup");

		visitValidator.validate(visit, errors);

		assertTrue(errors.hasFieldErrors("date"));
	}

}
