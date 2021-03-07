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

package zaesora.madeshiro.api.web;

import zaesora.madeshiro.api.GenericPacket;
import zaesora.madeshiro.api.Packet;
import zaesora.madeshiro.api.web.packets.InvalidRequest;
import zaesora.madeshiro.parser.ParseException;
import zaesora.madeshiro.parser.json.JSONObject;
import zaesora.madeshiro.parser.json.JSONParser;

/**
 * Class of NbEssential
 */
public abstract class GenericWebPacket implements GenericPacket {

    protected WebPacket out, in;
    protected JSONObject object;

    protected GenericWebPacket() {
        out = new WebPacket();
    }

    protected boolean checkPacketContent() {
        try {
            object = new JSONParser().parse(in.getContent());
            if (GenericPacket.CheckWebsiteID(object)) {
                return true;
            } else
                out = (WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.Webkey).out();
        } catch (ParseException e) {
            out = (WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.BadFormat).out();
        }

        return false;
    }

    @Override
    public boolean treatRequest() {
        if (in == null)
            return false;

        if (checkPacketContent()) {
            try {
                _treatRequest();
            } catch (NullPointerException e) {
                e.printStackTrace();
                out = (WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.BadRequest).out();
            } catch (Exception e) {
                e.printStackTrace();
                out = (WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.InternalError).out();
            }
        }

        return true;
    }

    protected abstract void _treatRequest() throws Exception;

    @Override
    public Packet in() {
        return in;
    }

    @Override
    public Packet out() {
        return out;
    }

    @Override
    public void setInputPacket(Packet packet) {
        this . in = (WebPacket) packet;
    }
}
