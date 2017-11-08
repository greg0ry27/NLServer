package com.nl.service.impl;

import akka.actor.Props;
import akka.japi.Option;
import com.nl.data.PlayerCharacter;
import com.nl.data.PlayerCharacterType;
import com.nl.service.IPlayerService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PlayerService implements IPlayerService{

    @Override
    public Option<LoginToken> doLogin(String login, String password) {
        System.out.println("Login: " + login + " Password: " + password);

        LoginToken token = new LoginToken();
        token.playerId = "1";
        token.sessionId = "123456";
        token.publicKey = "qwerty";

        return Option.some(token);
    }

    @Override
    public List<PlayerCharacter> findCharactersByPlayer(String playerId) {
        return Arrays.asList(new PlayerCharacter(1l, "Tester", 1, PlayerCharacterType.WARRIOR));
    }

    @Override
    public boolean createCharacter(String playerId, String name, PlayerCharacterType characterType) {
        return true;
    }

    @Override
    public PlayerCharacter findCharacter(String characterId) {
        return null;
    }
}
