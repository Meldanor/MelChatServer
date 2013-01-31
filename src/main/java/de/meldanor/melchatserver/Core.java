/*
 * Copyright (C) 2013 Kilian Gaertner
 * 
 * This file is part of MelChatServer.
 * 
 * MelChatServer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MelChatServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MelChatServer.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.meldanor.melchatserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import de.meldanor.network.NetworkHandler;

public class Core {

    public static void main(String[] args) {

        System.out.println("Starting Server...");
        String[] connInfo = parseArguments(args);
        NetworkHandler nHandler = null;
        try {
            nHandler = new NetworkHandler(connInfo[0]);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Server started at port " + connInfo[0] + "!");
        nHandler.serverLoop();

    }

    public static String[] parseArguments(String[] args) {
        Options options = new Options();
        options.addOption("p", true, "Port");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        String port = null;

        try {
            cmd = parser.parse(options, args);
            port = cmd.getOptionValue('p');
        } catch (ParseException e) {
            System.out.println("Usage: -p PORT");
            return null;
        }
        return new String[]{port};
    }
}
