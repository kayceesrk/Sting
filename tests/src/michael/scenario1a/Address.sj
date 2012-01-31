//$ bin/sessionjc -cp tests/classes/ tests/src/michael/scenario1a/Address.sj -d tests/classes/

package michael.scenario1a;

import java.io.Serializable;

class Address implements Serializable {
	private String address;

	public Address(String address) {
		this.address = address;
	}

	public String toString() {
		return address;
	}
}
