package io.github.oliviercailloux.persons_manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

/**
 * A non-exhaustive set of tests for the {@link MyPersonsManager}
 * implementation.
 *
 */
public class PersonsManagerTests {
	@Test
	void testSize() throws Exception {
		final Person john = Person.given(0, "John");
		final Person mary = Person.given(0, "Mary");

		final MyPersonsManager manager = new MyPersonsManager();
		assertTrue(manager.size() == 0);
		manager.setPersons(ImmutableList.of(john));
		assertTrue(manager.size() == 1);
		manager.setPersons(ImmutableList.of(john, john));
		assertTrue(manager.size() == 1);
		manager.setPersons(ImmutableList.of(john, mary));
		assertTrue(manager.size() == 2);
		manager.setPersons(ImmutableList.of());
		assertTrue(manager.size() == 0);

	}

	@Test
	void testContainsString() throws Exception {
		final Person john = Person.given(0, "John");
		final Person mary = Person.given(0, "Mary");

		final MyPersonsManager manager = new MyPersonsManager();
		assertFalse(manager.contains("John"));
		manager.setPersons(ImmutableList.of(john, mary));
		assertTrue(manager.contains("John"));
	}

	@Test
	void testContainsStream() throws Exception {
		final Person john = Person.given(0, "John");
		final Person leo = Person.given(0, "Éléonore");

		try (InputStream johnStream = new ByteArrayInputStream(john.getName().getBytes(StandardCharsets.UTF_8))) {
			final MyPersonsManager manager = new MyPersonsManager();
			assertFalse(manager.contains(johnStream));
		}

		try (InputStream johnStream = new ByteArrayInputStream(john.getName().getBytes(StandardCharsets.UTF_8))) {
			final MyPersonsManager manager = new MyPersonsManager();
			manager.setPersons(ImmutableList.of(john, leo));
			assertTrue(manager.contains(johnStream));
		}

		try (InputStream leoStream = new ByteArrayInputStream(leo.getName().getBytes(StandardCharsets.UTF_8))) {
			final MyPersonsManager manager = new MyPersonsManager();
			manager.setPersons(ImmutableList.of(john));
			assertFalse(manager.contains(leoStream));
		}

		try (InputStream leoStream = new ByteArrayInputStream(leo.getName().getBytes(StandardCharsets.UTF_8))) {
			final MyPersonsManager manager = new MyPersonsManager();
			manager.setPersons(ImmutableList.of(john, leo));
			assertTrue(manager.contains(leoStream));
		}
	}

	@Test
	void testToMap() throws Exception {
		final Person john = Person.given(0, "John");
		final Person mary = Person.given(1, "Mary");

		final MyPersonsManager manager = new MyPersonsManager();
		assertTrue(manager.toMap().isEmpty());
		manager.setPersons(ImmutableList.of(john, mary));
		assertEquals(ImmutableMap.of(1, mary, 0, john), manager.toMap());
	}

	@Test
	void testMapImmutable() throws Exception {
		final Person john = Person.given(0, "John");

		final MyPersonsManager manager = new MyPersonsManager();
		assertThrows(UnsupportedOperationException.class, () -> manager.toMap().put(1, john));
	}

	@Test
	void testIterator() throws Exception {
		final Person john = Person.given(0, "John");
		final Person mary = Person.given(1, "Mary");

		final MyPersonsManager manager = new MyPersonsManager();
		assertFalse(manager.personIterator().hasNext());
		manager.setPersons(ImmutableList.of(john, mary));
		final Iterator<Person> it = manager.personIterator();
		assertTrue(it.hasNext());
		final Person p1 = it.next();
		assertTrue(it.hasNext());
		final Person p2 = it.next();
		assertFalse(it.hasNext());
		assertEquals(ImmutableSet.of(john, mary), ImmutableSet.of(p1, p2));
	}

	@Test
	void testIteratorUnicity() throws Exception {
		final Person john = Person.given(0, "John");

		final MyPersonsManager manager = new MyPersonsManager();
		assertFalse(manager.personIterator().hasNext());
		manager.setPersons(ImmutableList.of(john, john));
		final Iterator<Person> it = manager.personIterator();
		assertTrue(it.hasNext());
		assertEquals(john, it.next());
		assertFalse(it.hasNext());
	}

	@Test
	void testIteratorRemove() throws Exception {
		final Person john = Person.given(0, "John");
		final Person mary = Person.given(1, "Mary");

		final MyPersonsManager manager = new MyPersonsManager();
		assertFalse(manager.personIterator().hasNext());
		manager.setPersons(ImmutableList.of(john, mary));
		assertEquals(2, manager.size());
		final Iterator<Person> it = manager.personIterator();
		assertTrue(it.hasNext());
		it.next();
		it.remove();
		assertTrue(it.hasNext());

		assertEquals(1, manager.size());
	}

	@Test
	void testIdIterator() throws Exception {
		final Person john = Person.given(0, "John");
		final Person mary0 = Person.given(0, "Mary");
		final Person mary1 = Person.given(1, "Mary");

		final MyPersonsManager manager = new MyPersonsManager();
		assertFalse(manager.idIterator().hasNext());
		manager.setPersons(ImmutableList.of(john, mary0, mary1));
		final Iterator<Integer> it = manager.idIterator();
		assertTrue(it.hasNext());
		final Integer i1 = it.next();
		assertTrue(it.hasNext());
		final Integer i2 = it.next();
		assertTrue(it.hasNext());
		final Integer i3 = it.next();
		assertFalse(it.hasNext());

		assertEquals(ImmutableMultiset.of(0, 0, 1), ImmutableMultiset.of(i1, i2, i3));
	}

	@Test
	void testIdIteratorRemove() throws Exception {
		final Person john = Person.given(0, "John");
		final Person mary0 = Person.given(0, "Mary");
		final Person mary1 = Person.given(1, "Mary");

		final MyPersonsManager manager = new MyPersonsManager();
		assertFalse(manager.idIterator().hasNext());
		manager.setPersons(ImmutableList.of(john, mary0, mary1));
		assertEquals(3, manager.size());

		final Iterator<Integer> it = manager.idIterator();
		assertTrue(it.hasNext());
		final int toRemove = it.next();
		it.remove();
		assertTrue(it.hasNext());

		final int expectedNbId;
		if (toRemove == 0) {
			expectedNbId = 2;
		} else {
			expectedNbId = 1;
		}

		assertEquals(2, manager.size());
		assertEquals(expectedNbId, Iterators.size(manager.idIterator()));
	}

	@Test
	void testRedundancyCounter() throws Exception {
		final Person john = Person.given(0, "John");
		final Person mary = Person.given(0, "Mary");

		final MyPersonsManager manager = new MyPersonsManager();
		manager.setPersons(ImmutableList.of(john, john, mary));
		final RedundancyCounter counter = manager.getRedundancyCounter();
		assertEquals(1, counter.getRedundancyCount());
		assertEquals(2, counter.getUniqueCount());

		manager.setPersons(ImmutableList.of(john));
		assertEquals(0, counter.getRedundancyCount());
		assertEquals(1, counter.getUniqueCount());
	}

}
