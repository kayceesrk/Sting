//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/common/User.sj -d tests/classes/

package oopsla.chat.common;

import java.io.Serializable;
import java.util.*;

public class User
{
	private Integer	id; 
	private String name;
	
	public User(Integer id, String name) //implements Serializable
	{
		this.id = id;
		this.name = name;
	}
	
	public Integer getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return getName() + " (" + getId().toString() + ")";
	}

	public final boolean equals(Object obj)
	{
		if (obj instanceof User)
		{
			return (getId().equals(((User) obj).getId())); // Don't bother checking names.
		}
		else
		{
			return false;
		}
	}

	public final int hashCode()
	{
		return getId().hashCode();
	}
}
