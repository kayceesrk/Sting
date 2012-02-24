//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/phone_example/SongInfo.sj -d tests/classes/

package purdue.continuations;
import java.io.Serializable;

class SongInfo implements Serializable
{
        String name;
        int rating;
        public SongInfo(String name, int r)
        {
                this.name = name;
                rating = r;
        }
        public String Name()
        {
                return name;
        }
        public int Rating()
        {
                return rating;
        }
}
