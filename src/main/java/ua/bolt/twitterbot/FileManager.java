package ua.bolt.twitterbot;

import twitter4j.Logger;

import java.io.*;

/**
 * Created by ackiybolt on 15.04.15.
 */
public class FileManager {

    private static Logger LOG = Logger.getLogger(FileManager.class);

    public static void writeContent(String content, String path) {

        try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            writer.println(content);

        } catch (FileNotFoundException ex) {
            LOG.warn("Can't find file. Skipped. ", ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            LOG.warn("Encoding problems. Skipped. ", ex.getMessage());
        }
    }


    public static String readContent(String path) {

        StringBuilder sb = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

        } catch (FileNotFoundException ex) {
            LOG.warn("Can't find file. ", ex.toString());
        } catch (IOException ex) {
            LOG.warn("Can't read file. ", ex.toString());
        }

        return sb.toString();
    }
}
