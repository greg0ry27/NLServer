package com.nl.network.util;

import com.nl.data.ErrorCodes;
import com.nl.data.PlayerCharacter;
import com.nl.network.generated.CharacterType;
import com.nl.network.generated.NLCharacter;
import com.nl.network.generated.NLFailed;
import com.nl.network.generated.NLPlayerInfo;
import com.nl.network.generated.NetworkPocket;
import com.nl.network.generated.PocketType;

import java.util.List;

public class NetworkUtils {

    public static NetworkPocket characterList(String playerId, List<PlayerCharacter> characters){
        Long id = Long.parseLong(playerId);

        NLPlayerInfo.Builder playerInfoBuilder = NLPlayerInfo.newBuilder().setId(id);

        for(int i = 0; i<characters.size(); i++){
            PlayerCharacter pch = characters.get(i);
            playerInfoBuilder.setCharaters(i, NLCharacter.newBuilder()
                    .setId(pch.getId())
                    .setLevel(pch.getLevel())
                    .setName(pch.getName())
                    .setType(CharacterType.valueOf(pch.getType().name()))
            );
        }

        return NetworkPocket.newBuilder()
                .setType(PocketType.PLAYER_INFO)
                .setPlayerInfo(playerInfoBuilder.build())
                .build();
    }

    public static NetworkPocket loginFiled(){
        return NetworkUtils.error(ErrorCodes.LOGIN_FAILED, "Login or password incorrect");
    }

    public static NetworkPocket error(int errorCode, String errorDesc){
        return NetworkPocket.newBuilder()
                .setType(PocketType.FAILED).setFailed(
                        NLFailed.newBuilder()
                                .setErrorCode(String.valueOf(errorCode))
                                .setErrordesc(errorDesc)
                                .build())
                .build();
    }
}
