package com.nl.network;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.TcpMessage;

import java.net.InetSocketAddress;

public class Server extends AbstractActor{

    final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), self());

    private InetSocketAddress address;

    public static Props props(InetSocketAddress address){
        return Props.create(Server.class, () -> new Server(address));
    }

    private Server(InetSocketAddress address){
        this.address = address;
    }

    @Override
    public void preStart() throws Exception {
        ActorRef tcp = Tcp.get(getContext().getSystem()).manager();
        Tcp.Command bind = TcpMessage.bind(getSelf(), address, 100);
        tcp.tell(bind, self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Bound.class, msg -> {
                    log.debug("Server is ready to accept connections on {}", address);
                })
                .match(Tcp.CommandFailed.class, msg -> {
                    getContext().stop(self());
                    log.debug("Start server failed on {}{", address);
                }).match(Tcp.Connected.class, conn -> {
                    ActorRef connection = getSender();
                    ActorRef handler = getContext().actorOf(Connection.props(conn.remoteAddress(), connection));
                    getSender().tell(TcpMessage.register(handler), self());
                }).build();
    }
}
