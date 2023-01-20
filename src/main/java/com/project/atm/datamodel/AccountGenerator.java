package com.project.atm.datamodel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class AccountGenerator {

    /**
     *
     * @param pin pin of an account
     * @return sha-256 hash of the pin
     */
    public static String hash(String pin){
        byte [] pinHash = null;

        // Encrypt to md5 part just make them both to strings when comparing
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            pinHash = md.digest(pin.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        StringBuilder pinString = new StringBuilder();
        for (int i = 0; i < pinHash.length; i++) {
            if ((0xff & pinHash[i]) < 0x10) {
                pinString.append("0"
                        + Integer.toHexString((0xFF & pinHash[i])));
            } else {
                pinString.append(Integer.toHexString(0xFF & pinHash[i]));
            }
        }

        return pinString.toString();
    }

    /**
     * to be placed into registration controller class
     * @return unique uid of a client
     */
    public static String clientUidGenerator() {
        String uid = "";
        Random rng = new Random();

        boolean nonUnique;
        do {

            int n = 100000 + rng.nextInt(900000);
            uid = String.valueOf(n);

            nonUnique = checkUnique(uid, "client");
        } while (nonUnique);

        return uid;
    }

    /**
     * to be place into registration controller class
     * @return unique uid of an account
     */
    public static String accountUidGenerator() {
        String uid = "";
        Random rng = new Random();

        boolean nonUnique;
        do {

            int n = 10000000 + rng.nextInt(90000000);
            uid = String.valueOf(n);

            nonUnique = checkUnique(uid, "account");
            // will keep generating uid until it is unique (nonUnique = false)
        } while (nonUnique);

        return uid;
    }

    /**
     * to be placed into registration controller class
     * @return unique card number for an account
     */
    public static String cardGenerator(){

        String cardNo = "";
        int len = 16;


        Random rng = new Random();

        boolean nonUnique;
        do {

            for (int i = 0; i < len; i++) {
                int j = rng.nextInt(10);
                // prevent the first digit being 0
                if (i == 0 && j == 0){
                    j +=1;
                }
                cardNo += String.valueOf(j);
            }

            nonUnique = checkUnique(cardNo, "account");
            // will keep generating uid until it is unique (nonUnique = false)
        } while (nonUnique);

        return cardNo;
    }

    /** to be placed in controller class
     * Checks the db if the uid is unique, table depends on uid len
     * client if len = 6, account if len = 8, card if len = 16
     * @param string id of an account or client
     * @return
     */
    public static boolean checkUnique(String string, String uidType){
        switch (uidType){
            case "account":
                return Datasource.getInstance().checkAccounts(string);
            case "client":
                return Datasource.getInstance().checkClients(string);
            case "card":
                return Datasource.getInstance().checkCards(string);
            default:
                System.out.println("Error");
                return true;
        }

    }
}
