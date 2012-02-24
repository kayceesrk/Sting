//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/phone_example/Album.sj -d tests/classes/

package purdue.continuations;
import java.io.Serializable;

class Album implements Serializable
{
        String name;
        int count;
        public Album(String name, int count)
        {
                this.name = name;
                this.count = count;
        }
        public String Name()
        {
                return name;
        }
        public int Count()
        {
                return count;
        }
}