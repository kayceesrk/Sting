//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/phone_example/Song.sj -d tests/classes/

package purdue.continuations;
import java.io.Serializable;

class Song implements Serializable
{
        String name;
        public Song(String name)
        {
                this.name = name;
        }
        public String Name()
        {
                return name;
        }
}