package com.nl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.nl.network.Server;

import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ActorSystem actorSystem = ActorSystem.create();
        InetSocketAddress address = new InetSocketAddress(49999);
        ActorRef server = actorSystem.actorOf(Server.props(address));
        System.out.println( "Hello World!" );
    }
}
