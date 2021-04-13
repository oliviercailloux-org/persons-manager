package io.github.oliviercailloux.persons_manager;

import static com.google.common.base.Preconditions.checkNotNull;

class MyRedundancyCounter implements RedundancyCounter {
	static public RedundancyCounter linkedTo(MyPersonsManager manager) {
		return new MyRedundancyCounter(manager);
	}

	private MyPersonsManager manager;

	private MyRedundancyCounter(MyPersonsManager manager) {
		this.manager = checkNotNull(manager);
	}

	@Override
	public int getRedundancyCount() {
		return manager.getLastListSize() - manager.size();
	}

	@Override
	public int getUniqueCount() {
		return manager.size();
	}

}