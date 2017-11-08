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
import generated.NetworkPacket;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
        this.connection = connection;
        this.dispatcher = getContext().actorOf(Dispatcher.props());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Received.class, msg -> {
                    ByteString data = msg.data();
                    bytesReaded += data.length();
                    System.out.println("Read data: " + data.length());
                    ByteBuffer byteBuffer = data.asByteBuffer();

                    while (byteBuffer.hasRemaining()){
                        if (byteBuffer.remaining() < 4)
                            return;

                        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                        int size = byteBuffer.getInt();
                        if (byteBuffer.remaining() < size)
                            return;

                        byte[] binaryData = new byte[size];
                        byteBuffer.get(binaryData);

                        NetworkPacket pocket = NetworkPacket.parseFrom(binaryData);
                        dispatcher.tell(pocket, self());
                    }
                })
                .match(NetworkPacket.class, msg -> {
                    ByteString data = ByteString.fromArray(msg.toByteArray());
                    int dataSize = data.size();
                    ByteBuffer buffer = ByteBuffer.allocateDirect(data.size() + 4);
                    buffer.order(ByteOrder.LITTLE_ENDIAN);
                    buffer.putInt(dataSize);
                    buffer.put(data.toArray());
                    buffer.position(0);
                    Tcp.Command command = TcpMessage.write(ByteString.fromByteBuffer(buffer), new ACK(dataSize));
                    writeBuffer+= dataSize;
                    connection.tell(command, self());
                    System.out.println("Write buffer state[+]: " + writeBuffer);
                })
                .match(ACK.class, msg -> {
                    writeBuffer-=msg.dataSize;
                    System.out.println("Write buffer state[-]: " + writeBuffer);
                })
                .match(Tcp.ConnectionClosed.class, msg -> {
                    getContext().stop(self());
                    dispatcher.tell(PoisonPill.getInstance(), self());
                })
                .build();
    }
}
