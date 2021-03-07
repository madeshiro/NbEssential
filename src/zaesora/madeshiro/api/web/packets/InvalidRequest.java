/*
 * This file is a part of the NbEssential plugin, licensed under the GPL v3 License (GPL)
 * Copyright © 2015-2018 MađeShirő ƵÆsora <https://github.com/madeshiro>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zaesora.madeshiro.api.web.packets;

import zaesora.madeshiro.api.GenericPacket;
import zaesora.madeshiro.api.Packet;
import zaesora.madeshiro.api.web.WebPacket;
import zaesora.madeshiro.parser.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Class of NbEssential
 */
public class InvalidRequest implements GenericPacket {

    private WebPacket packet;
    private JSONObject object;

    public InvalidRequest() {
        packet = new WebPacket();
        object = new JSONObject();

        packet.setOutHttpStatus("200 OK");
    }

    public static InvalidRequest generate(ErrorEnum errType) {
        InvalidRequest request = new InvalidRequest();
        request.setError(errType);

        switch (errType.id) {
            case -1:
                request.packet.setOutHttpStatus("500 Internal Error");
                break;
            case 5:
                request.packet.setOutHttpStatus("404 Not Found");
                break;
            default:
                // nothing
        }

        return request;
    }

    @Override
    public void setInputPacket(Packet packet) {
        // Do Nothing
    }

    @Override
    public Packet in() {
        return null;
    }

    @Override
    public Packet out() {
        Writer writer = new StringWriter();
        try {
            object.write(writer);
            packet.setOutContent(writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public void setErrorMessage(String msg) {
        object.put("ErrorRequest", msg);
    }

    public void setErrorType(int id) {
        object.put("ErrorType", id);
    }

    public void setError(ErrorEnum err) {
        this . setErrorMessage(err.msg);
        this . setErrorType(err.id);
    }

    @Override
    public boolean treatRequest() {
        this.setError(ErrorEnum.BadURI);

        return true;
    }

    public enum ErrorEnum {
        BadFormat(0x0, "Invalid Request Format (application/json expected)"),
        Webkey(0x1, "Invalid WebKey"),
        PlayerKey(0x2, "Invalid PlayerKey"),
        InvalidPlayer(0x3, "Invalid Player name or IP"),
        BadRequest(0x4, "Bad Request (missing or invalid elements)"),
        BadURI(0x5, "Bad URI"),
        InternalError(-1, "Internal Error");

        private int id;
        private String msg;

        ErrorEnum(int id, String msg) {
            this . id = id;
            this . msg = msg;
        }
    }
}
