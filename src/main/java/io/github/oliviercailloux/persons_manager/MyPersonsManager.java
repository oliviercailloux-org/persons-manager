package io.github.oliviercailloux.persons_manager;

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

	MyPersonsManager() {
		// …
	}

}
