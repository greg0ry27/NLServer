package com.nl.service;

import akka.japi.Option;
import com.nl.data.PlayerCharacter;
import com.nl.data.PlayerCharacterType;

import java.util.List;

/**
 * Created by ge on 22.09.17.
 */
public interface IPlayerService {

    class LoginToken{
        public String playerId;
        public String sessionId;
        public String publicKey;
    }
    /**
     * Login player with credentials
     * @param login
     * @param password
     * @return PlayerId if player login sucess, or null if unsuccess
     * @apiNote  blocking send-request-reply
     */
    Option<LoginToken> doLogin(String login, String password);

    /**
     * Return Character list, belongs this playerId
     * @param playerId
     * @return
     */
    List<PlayerCharacter> findCharactersByPlayer(String playerId);

    /**
     * Create character for player
     * @param playerId
     * @param characterName
     * @param characterType
     * @return for blocking send-request-reply!
     */
    boolean createCharacter(String playerId, String characterName, PlayerCharacterType characterType);

    PlayerCharacter findCharacter(String characterId);
}
