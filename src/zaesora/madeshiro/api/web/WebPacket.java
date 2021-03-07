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

import zaesora.madeshiro.api.Direction;
import zaesora.madeshiro.api.InvalidPayloadException;
import zaesora.madeshiro.api.Packet;

import java.util.HashMap;

/**
 * Class of NbEssential
 */
public class WebPacket extends Packet {

    private Direction direction;

    protected String http_version;
    protected String http_method;
    protected String http_status;
    protected String http_uri;

    protected HashMap<String, String> header;
    private String content;

    public WebPacket() {
        this . direction = Direction.OUT;
        this . header = new HashMap<>();
        {
            http_version = "HTTP/1.0";
            http_status = "200 OK";

            header.put("Content-Type", "application/json");
            header.put("Content-Length", "0");
        }
        this . content = "";
    }

    public WebPacket(String[] headers, String payload) throws InvalidPayloadException {
        this();

        this . direction = Direction.IN;
        this . treatInput(headers, payload);
    }

    public Direction getDirection() {
        return direction;
    }

    public String getRequestURI() {
        return http_uri;
    }

    public String getRequestMethod() {
        return http_method;
    }

    private void treatInput(String[] headers, String payload) throws InvalidPayloadException {
        {
            String[] http_context = headers[0].split(" ");
            http_method = http_context[0];
            http_uri = http_context[1];
            http_version = http_context[2];
        }

        for (int i = 1; i < headers.length; i++) {
            String[] var1 = headers[i].split(":");
            header.put(var1[0].toLowerCase(), var1[1].substring(1));
        }

        if (!header.get("content-type").equalsIgnoreCase("application/json"))
            throw new InvalidPayloadException("Invalid content type : expecting 'application/json'");

        this.content = payload;
    }

    @Override
    public String getContent() {
        return content;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public String getHttpOutContent() {
        String _header = getOutHttpContext() + "\r\n";
        header.put("Content-Length", String.valueOf(content.length()));
        for (String header_key : this.header.keySet())
            _header += header_key + ": " + this.header.get(header_key) + "\r\n";
        _header += "\r\n";

        return _header + getContent();
    }

    public void setOutHttpStatus(String status) {
        if (direction == Direction.OUT)
            http_status = status;
    }

    public void setOutContent(String content) {
        if (direction == Direction.OUT)
            this.content = content;
    }

    public String getOutHttpContext() {
        return http_version + " " + http_status;
    }
}
