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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Characterization tests for {@link Owner} pet lookup. No Spring context.
 */
class OwnerTests {

	@Test
	void getPetByNameFindsNewPetCaseInsensitively() {
		Owner owner = new Owner();
		Pet leo = new Pet();
		leo.setName("Leo");
		owner.addPet(leo);

		assertThat(owner.getPet("leo")).isSameAs(leo);
		assertThat(owner.getPet("LEO")).isSameAs(leo);
		assertThat(owner.getPet("Leo")).isSameAs(leo);
	}

	@Test
	void getPetByNameMatchesIgnoreNewFalseIncludingNewPets() {
		Owner owner = new Owner();
		Pet fido = new Pet();
		fido.setName("Fido");
		owner.addPet(fido);

		assertThat(fido.isNew()).isTrue();
		assertThat(owner.getPet("Fido")).isSameAs(owner.getPet("Fido", false));
		assertThat(owner.getPet("Fido")).isSameAs(fido);
		assertThat(owner.getPet("Fido", false)).isSameAs(fido);
	}

	@Test
	void getPetByNameIgnoringNewReturnsNullForNewPetAndFindsPersistedPet() {
		Owner owner = new Owner();

		Pet bowser = new Pet();
		bowser.setName("Bowser");
		owner.addPet(bowser);
		bowser.setId(7);

		Pet basil = new Pet();
		basil.setName("Basil");
		owner.addPet(basil);

		assertThat(bowser.isNew()).isFalse();
		assertThat(basil.isNew()).isTrue();
		assertThat(owner.getPet("Basil", true)).isNull();
		assertThat(owner.getPet("Basil", false)).isSameAs(basil);
		assertThat(owner.getPet("Bowser", true)).isSameAs(bowser);
		assertThat(owner.getPet("bowser", true)).isSameAs(bowser);
	}

	@Test
	void getPetByIdFindsPersistedPetSkipsNewPetAndMissingId() {
		Owner owner = new Owner();

		Pet bowser = new Pet();
		bowser.setName("Bowser");
		owner.addPet(bowser);
		bowser.setId(7);

		Pet basil = new Pet();
		basil.setName("Basil");
		owner.addPet(basil);

		assertThat(owner.getPets()).contains(bowser, basil);
		assertThat(owner.getPet(7)).isSameAs(bowser);
		assertThat(basil.isNew()).isTrue();
		assertThat(owner.getPet(basil.getId())).isNull();
		assertThat(owner.getPet(99)).isNull();
	}

	@Test
	void getPetByNameDoesNotMatchPetWithNullName() {
		Owner owner = new Owner();
		Pet unnamed = new Pet();
		owner.addPet(unnamed);

		assertThat(unnamed.getName()).isNull();
		assertThat(owner.getPets()).contains(unnamed);
		assertThat(owner.getPet((String) null)).isNull();
		assertThat(owner.getPet("")).isNull();
		assertThat(owner.getPet("Leo")).isNull();
	}

	@Test
	void getPetReturnsNullWhenNameOrIdIsMissing() {
		Owner owner = new Owner();
		Pet leo = new Pet();
		leo.setName("Leo");
		owner.addPet(leo);
		leo.setId(1);

		assertThat(owner.getPet("Ghost")).isNull();
		assertThat(owner.getPet("Ghost", false)).isNull();
		assertThat(owner.getPet("Ghost", true)).isNull();
		assertThat(owner.getPet(99)).isNull();
		assertThat(owner.getPet((Integer) null)).isNull();
		assertThat(owner.getPet((String) null)).isNull();
	}

}
