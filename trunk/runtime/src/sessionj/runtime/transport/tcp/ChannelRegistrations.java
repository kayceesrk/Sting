package sessionj.runtime.transport.tcp;

import sessionj.runtime.net.SJServerSocket;

import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SelectableChannel;
import java.util.*;

public class ChannelRegistrations {
    private final Map<SocketChannel, InputState> inputs;
    private final Map<ServerSocketChannel, SJServerSocket> accepts;
    private final Set<SelectableChannel> channels;

    public ChannelRegistrations() {
        inputs = new HashMap<SocketChannel, InputState>();
        accepts = new HashMap<ServerSocketChannel, SJServerSocket>();
        channels = new HashSet<SelectableChannel>();
    }

    public void accept(ServerSocketChannel ssc, SJServerSocket ss) {
        accepts.put(ssc, ss);
        channels.add(ssc);
    }

    public void input(SocketChannel sc, InputState state) {
        inputs.put(sc, state);
        channels.add(sc);
    }

    public InputState getInput(SocketChannel sc) {
        return inputs.get(sc);
    }

    public SJServerSocket getAccept(ServerSocketChannel ssc) {
        return accepts.get(ssc);
    }

    public Set<SelectableChannel> registeredChannels() {
        return Collections.unmodifiableSet(channels);
    }

    public void removeInput(SocketChannel sc) {
        inputs.remove(sc);
        channels.remove(sc);
    }
}
