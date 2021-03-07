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

package zaesora.madeshiro.api;

import nb.essential.main.NbEssential;
import zaesora.madeshiro.api.web.RequestType;
import zaesora.madeshiro.api.web.WebPacket;
import zaesora.madeshiro.api.web.packets.InvalidRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.UUID;

import static nb.essential.main.NbEssential.getLogger;
import static nb.essential.main.NbEssential.isPluginDebug;

/**
 * Class of NbEssential
 */
public class JSONAPI extends Thread {
    /**
     *
     */
    private static JSONAPI instance;
    public static final String website = "http://nbmc.wamp";

    private boolean running;

    private String website_id;
    private String last_website_id;
    private long last_website_id_timestamp;

    private ServerSocket server;

    private JSONAPI(int port) throws IOException {
        this . running = false;
        this . server = new ServerSocket(port);
    }

    public static int Nb_JAPI_GetPort() {
        return instance.server.getLocalPort();
    }

    @Override
    public void run() {
        running = true;

        if (!Nb_JAPI_UpdateWebsiteUID()) {
            if (isPluginDebug())
                getLogger().severe("[JSONAPI] Stop running JSONAPI Server {reason=no-website-uid}");
            running = false;
        }
        while (running) {
            try {
                Socket socket = server.accept();
                try {
                    WebPacket packet = read(socket);
                    GenericPacket gen = Nb_JAPI_CreateGenPacket(packet);
                    if (gen.treatRequest()) {
                        String data = ((WebPacket) gen.out()).getHttpOutContent();
                        socket.getOutputStream().write(data.getBytes(), 0, data.length());
                    }
                } catch (InvalidPayloadException e) {
                    e.printStackTrace();
                    String data = ((WebPacket) InvalidRequest.generate(InvalidRequest.ErrorEnum.BadRequest).out())
                            .getHttpOutContent();
                    socket.getOutputStream().write(data.getBytes(), 0, data.length());
                }

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static GenericPacket Nb_JAPI_CreateGenPacket(WebPacket packet) {
        GenericPacket gPacket = RequestType.getRequestByURI(packet.getRequestURI()).generate();
        gPacket.setInputPacket(packet);

        return gPacket;
    }

    public static boolean Nb_JAPI_UpdateWebsiteUID() {
        try {
            instance.website_id = UUID.randomUUID().toString();
            // System.out.println("Website id: " + instance.website_id);

            HttpURLConnection connection = (HttpURLConnection) new URL(website + "/features/serverkey").openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("Content-Length", Integer.toString(instance.website_id.length()));
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            connection.getOutputStream().write(instance.website_id.getBytes(), 0, instance.website_id.length());

            //noinspection StatementWithEmptyBody
            while (connection.getInputStream().read() != -1) {
            }
            connection.disconnect();
        } catch (IOException e) {
            getLogger().severe("[JSONAPI] Unable to get the website-uid {reason=website-down-exception}");
            return false;
        }

        return true;
    }

    public static boolean Nb_JAPI_VerifyWebsiteID(String id) {
        return instance.website_id.equals(id);
    }

    private WebPacket read(Socket socket) throws IOException, InvalidPayloadException {

        String header = "", content = "";
        char[] eoh = {' ', ' ', ' ', ' '};
        do {
            for (int i = 0; i < 3; i++)
                eoh[i] = eoh[i+1];

            eoh[3] = (char) socket.getInputStream().read();
            header += eoh[3];
        } while (! new String(eoh).equals("\r\n\r\n"));

        String[] headers = header.split("\r\n");
        if (!headers[0].contains("HTTP") || !headers[0].contains("POST")) {
            throw new InvalidPayloadException("Invalid HTTP request sended ! (waiting HTTP and POST method");
        }

        int content_length = 0;
        for (String h : headers) {
            if (h.toLowerCase().contains("content-length")) {
                content_length = Integer.parseInt(h.toLowerCase().replace("content-length: ", ""));
                break;
            }
        }
        for (int i = 0; i < content_length; i++)
            content += (char) socket.getInputStream().read();

        return new WebPacket(headers, content);
    }

    public static boolean Nb_JAPI_Create(int port) {
        try {
            instance = new JSONAPI(port);
        } catch (IOException e) {
            if (NbEssential.isPluginDebug())
                getLogger().severe("[JSONAPI] Unable to create JSONAPI Server !");
            return false;
        }

        if (NbEssential.isPluginDebug())
            getLogger().info("[JSONAPI] Successfuly create JSONAPI Server !");
        return true;
    }

    public static boolean Nb_JAPI_Start() {
        if (instance == null)
            return false;

        instance.start();
        return true;
    }

    public static void Nb_JAPI_Stop() {
        if (isPluginDebug() && instance.running)
            getLogger().info("[JSONAPI] Stop running JSONAPI Server...");
        instance.running = false;
    }
}
