package com.nl.network;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import com.nl.network.generated.NetworkPocket;

import java.net.InetSocketAddress;

public class Connection extends AbstractActor {

    final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), self());

    private static class ACK implements Tcp.Event{
        private int dataSize;

        public ACK(int dataSize) {
            this.dataSize = dataSize;
        }
    }

    private InetSocketAddress remoteAddress;
    private ActorRef connection;
    private ActorRef dispatcher;
    private long bytesReaded;
    private long writeBuffer;

    public static Props props(InetSocketAddress remoteAddress, ActorRef connection) {
        return Props.create(Connection.class, () -> new Connection(remoteAddress, connection));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.debug("Establish connection with {}", remoteAddress);
    }

    private Connection(InetSocketAddress remoteAddress, ActorRef connection) {
        this.remoteAddress = remoteAddress;
        this.dispatcher = getContext().actorOf(Dispatcher.props());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Received.class, msg -> {
                    ByteString data = msg.data();
                    bytesReaded += data.length();
                    NetworkPocket pocket = NetworkPocket.parseFrom(data.asByteBuffer());
                    dispatcher.tell(pocket, self());
                })
                .match(NetworkPocket.class, msg -> {
                    ByteString data = ByteString.fromArray(msg.toByteArray());
                    int dataSize = data.size();
                    Tcp.Command command = TcpMessage.write(data, new ACK(dataSize));
                    writeBuffer+= dataSize;
                    connection.tell(command, self());
                    log.debug("Write buffer state[+]: {}", writeBuffer);
                })
                .match(ACK.class, msg -> {
                    writeBuffer-=msg.dataSize;
                    log.debug("Write buffer state[-]: {}", writeBuffer);
                })
                .match(Tcp.ConnectionClosed.class, msg -> {
                    getContext().stop(self());
                    dispatcher.tell(PoisonPill.getInstance(), self());
                })
                .build();
    }
}
