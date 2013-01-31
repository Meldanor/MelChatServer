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
