//$ bin/sessionjc tests/src/pldi/example/NoAliasLinkedList.sj -d tests/classes/

package pldi.example;

import java.io.*;

public class NoAliasLinkedList implements Serializable
{
	private int value;
	private noalias NoAliasLinkedList next;

	public NoAliasLinkedList(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public noalias NoAliasLinkedList getNext()
	{
		return next;
	}

	public void setNext(noalias NoAliasLinkedList next)
	{
		this.next = next;
	}

	public noalias NoAliasLinkedList get(int i)
	{
		if (i <= 0)
		{
			throw new IndexOutOfBoundsException(new Integer(i).toString());
		}
		else if (i == 1)
		{
			return getNext();
		}
		else
		{
			noalias NoAliasLinkedList foo = getNext();
			noalias NoAliasLinkedList bar = foo.get(i - 1);

			setNext(foo);

			return bar;
		}
	}

	public int size()
	{
		noalias NoAliasLinkedList foo = getNext();

		if (foo == null)
		{
			return 1;
		}
		else
		{
			int size = foo.size();

			setNext(foo);

			return size + 1;
		}
	}

	public String toString()
	{
		noalias NoAliasLinkedList foo = getNext();

		String m = new Integer(value).toString();

		if (foo == null)
		{
			return m;
		}

		m += ", " + foo.toString();

		setNext(foo);

		return m;
	}
}
