package com.nl.service.impl;

import akka.dispatch.Futures;
import akka.japi.Option;
import com.nl.data.PlayerCharacter;
import com.nl.data.PlayerCharacterType;
import com.nl.service.IPlayerService;
import scala.concurrent.Future;

import java.util.Arrays;
import java.util.List;

public class PlayerService implements IPlayerService{

    @Override
    public Option<String> doLogin(String login, String password) {
        System.out.println("Login: " + login + " Password: " + password);
        return Option.some("1");
    }

    @Override
    public List<PlayerCharacter> findCharactersByPlayer(String playerId) {
        return Arrays.asList(new PlayerCharacter(1l, "Tester", 1, PlayerCharacterType.WARRIOR));
    }

    @Override
    public boolean createCharacter(String playerId, String name, PlayerCharacterType characterType) {
        return true;
    }

}
