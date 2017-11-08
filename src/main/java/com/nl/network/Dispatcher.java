package com.nl.network;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Option;
import akka.japi.pf.FI;
import com.nl.data.PlayerCharacter;
import com.nl.data.PlayerCharacterType;
import com.nl.network.util.NetworkUtils;
import com.nl.service.IPlayerService;
import com.nl.service.IPlayerService.LoginToken;
import com.nl.service.impl.PlayerService;
import generated.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Dispatcher extends AbstractActor {

    final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), self());

    private static final Class<NetworkPacket> DISPATCH_CLASS = NetworkPacket.class;

    private static final FI.TypedPredicate<NetworkPacket> LOGIN = create(PacketType.LOGIN);
    private static final FI.TypedPredicate<NetworkPacket> CREATE_CHARACTER = create(PacketType.CREATE_CHARACTER);
    private static final FI.TypedPredicate<NetworkPacket> CHARACTER_SELECT = create(PacketType.CHARACTER_SELECT);
    private static final FI.TypedPredicate<NetworkPacket> LEVEL_READY = create(PacketType.LEVEL_READY);

    @Autowired private IPlayerService playerService;

    private static FI.TypedPredicate<NetworkPacket> create(PacketType type) {
        return NetworkPacket -> NetworkPacket.getType() == type;
    }

    public static Props props() {
        return Props.create(Dispatcher.class, Dispatcher::new);
    }

    public Dispatcher() {
        this.playerService = TypedActor.get(getContext()).typedActorOf(new TypedProps<>(IPlayerService.class, PlayerService.class));
    }

    private String playerId;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DISPATCH_CLASS, LOGIN, p -> {
                    Login login = p.getLogin();
                    Option<LoginToken> optionPlayerId = playerService.doLogin(
                            login.getLogin(), login.getPassword());

                    if (optionPlayerId.isDefined()) {
                        LoginToken token = optionPlayerId.get();
                        this.playerId = token.playerId;
                        List<PlayerCharacter> characters = playerService.findCharactersByPlayer(playerId);
                        sender().tell(NetworkUtils.loginAccepted(token.sessionId, token.publicKey), self());
                        sender().tell(NetworkUtils.characterList(playerId, characters), self());
                    } else
                        sender().tell(NetworkUtils.loginFiled(), self());
                })
                .match(DISPATCH_CLASS, CREATE_CHARACTER, p->{
                    CreateCharacter createCharacter = p.getCreateCharacter();
                    String type = createCharacter.getType().name();
                    PlayerCharacterType characterType = PlayerCharacterType.valueOf(type);
                    playerService.createCharacter(playerId, createCharacter.getName(), characterType);

                    List<PlayerCharacter> characters = playerService.findCharactersByPlayer(playerId);
                    sender().tell(NetworkUtils.characterList(playerId, characters), self());

                })
                .match(DISPATCH_CLASS, CHARACTER_SELECT, p -> {
                    CharacterSelect characterSelect = p.getCharacterSelect();
                    String characterId = String.valueOf(characterSelect.getId());
                    PlayerCharacter character = playerService.findCharacter(characterId);

                    LoadLevel pocket = LoadLevel.newBuilder()
                            .setLevel(character.getLocation())
                            .build();

                    sender().tell(pocket, self());

                }).match(DISPATCH_CLASS, LEVEL_READY, p -> {
                    log.debug("Level is Ready!!!");
                    //todo start spawn everything!
                })
                .build();
    }
}
