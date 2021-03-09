package io.github.oliviercailloux.persons_manager;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * This class should be instanciated using one of its static factory method (to
 * be created). One is named “empty”, admits no parameters, and returns a
 * manager that “manages” an empty set of persons; the other one is named
 * “given”, admits an iterable instance of persons as a parameter, and uses it
 * to initialize the set of persons that the returned instance will manage (this
 * also initializes correspondingly the redundancy counter).
 *
 */
class MyPersonsManager implements PersonsManager {

	private Set<Person> persons;

	MyPersonsManager() {
		persons = new LinkedHashSet<>();
	}

	@Override
	public void setPersons(List<Person> persons) {
		this.persons = new LinkedHashSet<>();
		this.persons.addAll(persons);
	}

	@Override
	public int size() {
		return persons.size();
	}

	@Override
	public boolean contains(String name) {
		return persons.stream().anyMatch(p -> p.getName().equals(name));
	}

	@Override
	public boolean contains(InputStream personNameAsStream) throws IOException {
		return contains(new String(personNameAsStream.readAllBytes(), StandardCharsets.UTF_8));
	}

	@Override
	public Map<Integer, Person> toMap() {
		return persons.stream().collect(ImmutableMap.toImmutableMap(Person::getId, p -> p));
	}

	@Override
	public Iterator<Person> personIterator() {
		TODO();
		return null;
	}

	@Override
	public Iterator<Integer> idIterator() {
		TODO();
		return null;
	}

	@Override
	public RedundancyCounter getRedundancyCounter() {
		TODO();
		return null;
	}

	@Override
	public String toString() {
		TODO();
		return super.toString();
	}
}
