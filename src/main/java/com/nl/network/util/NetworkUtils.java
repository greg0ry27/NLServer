package com.nl.network.util;

import com.nl.data.ErrorCodes;
import com.nl.data.PlayerCharacter;
import generated.*;
import generated.Character;

import java.util.List;

public class NetworkUtils {

    public static NetworkPacket loginAccepted(String sessionId, String publicKey) {
        LoginAccepted loginAccepted = LoginAccepted.newBuilder()
                .setSessionId(sessionId)
                .setPublicKey(publicKey)
                .build();

        return NetworkPacket.newBuilder()
                .setType(PacketType.LOGIN_ACCEPTED)
                .setLoginAccepted(loginAccepted)
                .build();
    }

    public static NetworkPacket characterList(String playerId, List<PlayerCharacter> characters) {
        Long id = Long.parseLong(playerId);

        PlayerInfo.Builder playerInfoBuilder = PlayerInfo.newBuilder().setId(id);

        for (PlayerCharacter pch : characters) {
            playerInfoBuilder.addCharaters(Character.newBuilder()
                    .setId(pch.getId())
                    .setLevel(pch.getLevel())
                    .setName(pch.getName())
                    .setType(CharacterType.valueOf(pch.getType().name()))
            );
        }

        return NetworkPacket.newBuilder()
                .setType(PacketType.PLAYER_INFO)
                .setPlayerInfo(playerInfoBuilder.build())
                .build();
    }

    public static NetworkPacket loginFiled() {
        return NetworkUtils.error(ErrorCodes.LOGIN_FAILED, "Login or password incorrect");
    }

    public static NetworkPacket error(int errorCode, String errorDesc) {
        return NetworkPacket.newBuilder()
                .setType(PacketType.LOGIN_FAILED).setLoginFailed(
                        LoginFailed.newBuilder()
                                .setErrorCode(String.valueOf(errorCode))
                                .setErrordesc(errorDesc)
                                .build())
                .build();
    }
}
