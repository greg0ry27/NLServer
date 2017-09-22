package com.nl.network;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Option;
import akka.japi.pf.FI;
import com.nl.data.PlayerCharacter;
import com.nl.data.PlayerCharacterType;
import com.nl.network.generated.NLCharacterSelect;
import com.nl.network.generated.NLCreateCharacter;
import com.nl.network.generated.NLLoadLevel;
import com.nl.network.generated.NLLogin;
import com.nl.network.generated.NetworkPocket;
import com.nl.network.generated.PocketType;
import com.nl.network.util.NetworkUtils;
import com.nl.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Dispatcher extends AbstractActor {

    final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), self());

    private static final Class<NetworkPocket> DISPATCH_CLASS = NetworkPocket.class;

    private static final FI.TypedPredicate<NetworkPocket> LOGIN = create(PocketType.LOGIN);
    private static final FI.TypedPredicate<NetworkPocket> CREATE_CHARACTER = create(PocketType.CREATE_CHARACTER);
    private static final FI.TypedPredicate<NetworkPocket> CHARACTER_SELECT = create(PocketType.CHARACTER_SELECT);
    private static final FI.TypedPredicate<NetworkPocket> LEVEL_READY = create(PocketType.LEVEL_READY);

    @Autowired private IPlayerService playerService;

    private static FI.TypedPredicate<NetworkPocket> create(PocketType type) {
        return networkPocket -> networkPocket.getType() == type;
    }

    public static Props props() {
        return Props.create(() -> new Dispatcher());
    }

    private String playerId;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DISPATCH_CLASS, LOGIN, p -> {
                    NLLogin login = p.getLogin();

                    Option<String> optionPlayerId = playerService.doLogin(
                            login.getLogin(), login.getPassword());

                    NetworkPocket response;
                    if (optionPlayerId.isDefined()) {

                        this.playerId = optionPlayerId.get();
                        List<PlayerCharacter> characters = playerService.findCharactersByPlayer(playerId);
                        response = NetworkUtils.characterList(playerId, characters);

                    } else
                        //TODO close connection
                        response = NetworkUtils.loginFiled();

                    sender().tell(response, self());
                })
                .match(DISPATCH_CLASS, CREATE_CHARACTER, p->{
                    NLCreateCharacter createCharacter = p.getCreateCharacter();
                    String type = createCharacter.getType().name();
                    PlayerCharacterType characterType = PlayerCharacterType.valueOf(type);
                    playerService.createCharacter(playerId, createCharacter.getName(), characterType);

                    List<PlayerCharacter> characters = playerService.findCharactersByPlayer(playerId);
                    sender().tell(NetworkUtils.characterList(playerId, characters), self());

                })
                .match(DISPATCH_CLASS, CHARACTER_SELECT, p -> {
                    NLCharacterSelect characterSelect = p.getCharacterSelect();
                    String characterId = String.valueOf(characterSelect.getId());
                    PlayerCharacter character = playerService.findCharacter(characterId);

                    NLLoadLevel pocket = NLLoadLevel.newBuilder()
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
