package io.github.oliviercailloux.persons_manager;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class should be instanciated using one of its static factory method (to
 * be created). One is named “empty”, admits no parameters, and returns a
 * manager that “manages” an empty set of persons; the other one is named
 * “given”, admits an iterable instance of persons as a parameter, and uses it
 * to initialize the set of persons that the returned instance will manage (this
 * also initializes correspondingly the redundancy counter).
 */
class MyPersonsManager implements PersonsManager {

	public static PersonsManager empty() {
		return new MyPersonsManager();
	}

	public static PersonsManager given(Iterable<Person> persons) {
		final MyPersonsManager manager = new MyPersonsManager();
		manager.setPersons(ImmutableList.copyOf(persons));
		return manager;
	}

	private Set<Person> persons;
	private int lastListSize;

	MyPersonsManager() {
		persons = new LinkedHashSet<>();
		lastListSize = 0;
	}

	@Override
	public void setPersons(List<Person> persons) {
		this.persons = new LinkedHashSet<>();
		this.persons.addAll(persons);
		lastListSize = persons.size();
	}

	@Override
	public void setTaskForce(Person... persons) {
		final ImmutableList<Person> personsList = ImmutableList.copyOf(persons);
		checkArgument(personsList.size() == 1 || personsList.size() == 2);
		setPersons(personsList);
	}

	@Override
	public int size() {
		return persons.size();
	}

	int getLastListSize() {
		return lastListSize;
	}

	@Override
	public boolean contains(String name) {
//		return persons.stream().anyMatch(p -> p.getName().equals(name));
		for (Person person : persons) {
			if (person.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean contains(InputStream personNameAsStream) throws IOException {
		return contains(new String(personNameAsStream.readAllBytes(), StandardCharsets.UTF_8));
	}

	@Override
	public ImmutableMap<Integer, Person> toMap() {
//		return persons.stream().collect(ImmutableMap.toImmutableMap(Person::getId, p -> p));
		final ImmutableMap.Builder<Integer, Person> builder = ImmutableMap.builder();
		for (Person person : persons) {
			builder.put(person.getId(), person);
		}
		return builder.build();
	}

	@Override
	public Iterator<Person> personIterator() {
		return persons.iterator();
	}

	@Override
	public Iterator<Integer> idIterator() {
//		return Iterators.transform(persons.iterator(), Person::getId);
		final ImmutableList.Builder<Integer> builder = ImmutableList.builder();
		for (Person person : persons) {
			builder.add(person.getId());
		}
		return builder.build().iterator();
	}

	@Override
	public RedundancyCounter getRedundancyCounter() {
		return MyRedundancyCounter.linkedTo(this);
	}

	@Override
	public String toString() {
		return "PersonsManager with " + size() + " entries";
	}
}
