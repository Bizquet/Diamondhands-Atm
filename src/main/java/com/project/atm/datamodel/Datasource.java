package com.project.atm.datamodel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    //Enums
    private enum AccountsConstants{
        TABLE("atm_db.accounts"),
        UID("accounts.uid"),
        ACC_TYPE("accounts.acc_type"),
        BALANCE("accounts.balance"),
        HOLDER("accounts.acc_holder"),
        CARD_NO("accounts.card_no"),
        PIN("accounts.pin");

        final String constant;

        AccountsConstants(String constant) {
            this.constant = constant;
        }

        @Override
        public String toString() {
            return constant;
        }
    }

    private enum ClientsConstants{
        TABLE("atm_db.clients"),
        UID("clients.uid");

        final String constant;

        ClientsConstants(String constant){
            this.constant = constant;
        }

        @Override
        public String toString() {
            return constant;
        }
    }

    private enum ClientInfoConstants{
        TABLE("atm_db.client_infos"),
        CLIENT_ID("client_infos.client_id"),
        FNAME("client_infos.fname"),
        LNAME("client_infos.lname"),
        BIRTHDAY("client_infos.birthday"),
        OCCUPATION("client_infos.occupation"),
        PURPOSE("client_infos.purpose"),
        ADDRESS("client_infos.address"),
        EMAIL("client_infos.email");

        final String constant;

        ClientInfoConstants(String constant){
            this.constant = constant;
        }

        @Override
        public String toString() {
            return constant;
        }
    }

    private enum TransactionConstants{
        TABLE("atm_db.transactions"),
        TYPE("transactions.type"),
        ACC_ID("transactions.acc_id"),
        AMOUNT("transactions.amount"),
        DATE("transactions.date"),
        TIME("transactions.time");

        final String constant;

        TransactionConstants(String constant){
            this.constant = constant;
        }

        @Override
        public String toString() {
            return constant;
        }

    }

    private static Datasource instance = new Datasource();

    private static final String CONNECTION_STRING = "jdbc:mysql://127.0.01:3306/atm_db";
    private static final String USERNAME = "root";
    private static final String  PASSWORD = "Violet_1337";

    private static final String ADD_NEW_CLIENT_ID = "INSERT INTO " + ClientsConstants.TABLE + " (uid) VALUES (?)";
    private static final String ADD_NEW_ACCOUNT = "INSERT INTO " + AccountsConstants.TABLE +
            " (" + AccountsConstants.UID + ", " + AccountsConstants.ACC_TYPE + ", " +
            AccountsConstants.HOLDER + ", " + AccountsConstants.CARD_NO + ", " + AccountsConstants.PIN +
            ") " + "VALUES (?, ?, ?, ?, ?)";
    private static final String ADD_NEW_CLIENT_INFO = "INSERT INTO " + ClientInfoConstants.TABLE +
            " (" + ClientInfoConstants.CLIENT_ID + ", " + ClientInfoConstants.FNAME +
            ", " + ClientInfoConstants.LNAME + ", " + ClientInfoConstants.BIRTHDAY +
            ", " + ClientInfoConstants.OCCUPATION + ", " + ClientInfoConstants.PURPOSE +
            ", " + ClientInfoConstants.ADDRESS + ", " + ClientInfoConstants.EMAIL +
            ") " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DEPOSIT_AMOUNT = "UPDATE " + AccountsConstants.TABLE + " SET "
            + AccountsConstants.BALANCE + " = ? WHERE " + AccountsConstants.UID + " = ?";
    private static final String ADD_NEW_TRANSACTION = "INSERT INTO " + TransactionConstants.TABLE +
            " (" + TransactionConstants.TYPE + ", " + TransactionConstants.ACC_ID + ", " +
            TransactionConstants.AMOUNT + ", " + TransactionConstants.DATE + ", " + TransactionConstants.TIME
            + ") " + "VALUES (?, ?, ?, ?, ?)";

    private Connection con;
    private PreparedStatement insertNewClient;
    private PreparedStatement insertNewAccount;
    private PreparedStatement insertNewClientInfo;
    private PreparedStatement depositAmount;
    private PreparedStatement insertTransaction;

    private Datasource(){

    }

    // makes the class a singleton class
    public static Datasource getInstance(){
        return instance;
    }

    /**
     * initiates the connections to the db
     * @return true if a connection is established, else false
     */
    public boolean open(){
        try {
            con = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
            insertNewClient = con.prepareStatement(ADD_NEW_CLIENT_ID);
            insertNewAccount = con.prepareStatement(ADD_NEW_ACCOUNT);
            insertNewClientInfo = con.prepareStatement(ADD_NEW_CLIENT_INFO);
            depositAmount = con.prepareStatement(DEPOSIT_AMOUNT);
            insertTransaction = con.prepareStatement(ADD_NEW_TRANSACTION);
            return true;
        }catch (SQLException e){
            System.out.println("Couldn't connect to database" + e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    /**
     * closes all the connections to the database
     */
    public void close(){
        try{
            if(con != null){
                con.close();
                System.out.println("connection established and closed");
            }

            if(insertNewClient != null){
                insertNewClient.close();
            }

            if(insertNewAccount != null){
                insertNewAccount.close();
            }

            if (insertNewClientInfo != null){
                insertNewClientInfo.close();
            }

            if(depositAmount != null){
                depositAmount.close();
            }

            if(insertTransaction != null){
                insertTransaction.close();
            }

        } catch (SQLException e){
            System.out.println("Couldn't close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * stay in data source, to be used in tandem with generators in controller class
     * @param uid to be checked of there a re similar entries
     * @return true if there are similar entries, else false (what we want)
     */
    public boolean checkAccounts(String uid){
        String query = "SELECT * FROM " + AccountsConstants.TABLE + " WHERE " + AccountsConstants.UID
                + " = ?";

        boolean nonUnique = true;

        try(PreparedStatement querySimilarUid = con.prepareStatement(query)){
            querySimilarUid.setString(1, uid);

            try(ResultSet resultSet = querySimilarUid.executeQuery()){
                // will return true if there are entries
                nonUnique = resultSet.next();
            }
        }catch (SQLException e){
            System.out.println("Error getting similar clients " + e.getMessage());
            e.printStackTrace();
        }

        return nonUnique;
    }

    /**
     * stay in data source, to be used in tandem with generators in controller class
     * @param uid to be checked of there are similar entries
     * @return true if there are similar entries, else false (what we want)
     */
    public boolean checkClients(String uid){
        String query = "SELECT * FROM " + ClientsConstants.TABLE + " WHERE " + ClientsConstants.UID
                + " = ?";

        boolean nonUnique = true;

        try(PreparedStatement querySimilarUid = con.prepareStatement(query)){
            querySimilarUid.setString(1, uid);

            try(ResultSet resultSet = querySimilarUid.executeQuery()){
                // will return true if there are entries
                nonUnique = resultSet.next();
            }
        }catch (SQLException e){
            System.out.println("Error getting similar clients " + e.getMessage());
            e.printStackTrace();
        }

        return nonUnique;
    }

    /**
     * stay in data source, to be used in tandem with generators in controller class
     * @param cardNo to be checked of there are similar entries
     * @return true if there are similar entries, else false (what we want)
     */
    public boolean checkCards(String cardNo){
        String query = "SELECT * FROM " + AccountsConstants.TABLE + " WHERE " + AccountsConstants.CARD_NO
                + " = ?";

        boolean nonUnique = true;

        try(PreparedStatement querySimilarCard = con.prepareStatement(query)){
            querySimilarCard.setString(1, cardNo);

            try(ResultSet resultSet = querySimilarCard.executeQuery()){
                nonUnique = resultSet.next();
            }
        }catch (SQLException e){
            System.out.println("Error getting similar cards " + e.getMessage());
            e.printStackTrace();
        }

        return nonUnique;
    }

    /** LOGIN
     * Will check if there matching credentials from the accounts table
     * @param pin string inputted by the user
     * @param cardNo string inputted by the user
     * @return true if the pin is similar to something in db
     */
    public boolean checkCredentials(String pin, String cardNo){
        String query = "SELECT * FROM " + AccountsConstants.TABLE + " WHERE " + AccountsConstants.CARD_NO
                + " = ? AND " + AccountsConstants.PIN + " = ?";

        boolean hasSimilar = false;

        try(PreparedStatement querySimilarAccount = con.prepareStatement(query)){
            querySimilarAccount.setString(1,cardNo);
            querySimilarAccount.setString(2, pin);

            try(ResultSet resultSet = querySimilarAccount.executeQuery()){
                hasSimilar = resultSet.next();
            }

        }catch (SQLException e){
            System.out.println("Error getting rows with similar credentials " + e.getMessage());
            e.printStackTrace();
        }

        return hasSimilar;
    }

    /** LOGIN
     * Must have logged in first before calling this method
     * @param cardNo string to be used in querying the row
     * @return Account object where data from the specific row is stored
     */
    public Account queryAccount(String cardNo){
        String query = "SELECT * FROM " + AccountsConstants.TABLE + " WHERE " + AccountsConstants.CARD_NO
                + " = ?";
        Account account = new Account();

        try(PreparedStatement queryAccount = con.prepareStatement(query)){
            queryAccount.setString(1, cardNo);

            try(ResultSet resultSet = queryAccount.executeQuery()){
                if(resultSet.next()){
                    account.setuId(resultSet.getInt(1));
                    account.setAccountType(resultSet.getString(2));
                    account.setBalance(resultSet.getDouble(3));
                }else {
                    throw new SQLException("Error queryng account");
                }
                System.out.println("Successfully retrieved account");
            }
        }catch (SQLException e){
            System.out.println("Error getting row with similar card no. " + e.getMessage());
            e.printStackTrace();
        }

        return account;
    }

    /**
     * Method to get the list of transactions of a particular account
     * @param uid identifier of the account
     * @return list of transactions of the account
     */
    public List<TransactionFX> queryTransactions(int uid){
        String query = "SELECT * FROM " + TransactionConstants.TABLE + " WHERE " + TransactionConstants.ACC_ID +
                " = ?";

        System.out.println(query);
        List<TransactionFX> transactionList = new ArrayList<>();

        try (PreparedStatement statement = con.prepareStatement(query)){
            statement.setInt(1, uid);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    TransactionFX transaction = new TransactionFX();

                    transaction.setTransaction_id(resultSet.getInt(1));
                    transaction.setTransactType(resultSet.getString(2));
                    transaction.setAccountId(Integer.parseInt(resultSet.getString(3)));
                    transaction.setAmount(resultSet.getDouble(4));
                    transaction.setDate(resultSet.getDate(5).toLocalDate().toString());
                    transaction.setTime(resultSet.getTime(6).toLocalTime().toString());
                    transactionList.add(transaction);
                }
            }

            return transactionList;

        } catch (SQLException e){
            System.out.println("Error getting transactions " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get account data of the uid matching the inputted one
     * @param uid uid of the destination account
     * @return Account object where data from the specific row is stored
     */
    public Account queryDestinationAccount(int uid) {
        String query = "SELECT * FROM " + AccountsConstants.TABLE + " WHERE " + AccountsConstants.UID
                + " = ?";
        Account account = new Account();

        try(PreparedStatement queryAccount = con.prepareStatement(query)){
            queryAccount.setInt(1, uid);

            try(ResultSet resultSet = queryAccount.executeQuery()){
                if(resultSet.next()){
                    account.setuId(resultSet.getInt(1));
                    account.setAccountType(resultSet.getString(2));
                    account.setBalance(resultSet.getDouble(3));
                }else {
                    throw new SQLException("Cannot retrieve destination Account");

                }

            }
        }catch (SQLException e){
            System.out.println("Error getting row with similar card no. " + e.getMessage());
            e.printStackTrace();
        }

        return account;
    }

    /** WITHDRAW, DEPOSIT, TRANSFER
     * tries to update the value of current account balance
     * united method to update balance after a withdraw, deposit, and transfer
     * @param amount value to insert in column
     * @param uid to be used in WHERE clause
     * @return true if successful deposit, else false
     */
    public boolean updateBalanceAmount(double amount, int uid) throws SQLException{

        try{
            con.setAutoCommit(false);

            depositAmount.setDouble(1, amount);
            depositAmount.setInt(2, uid);

            int affectedRows = depositAmount.executeUpdate();
            if (affectedRows == 1){
                System.out.println("Successfully changed amount, New amount: " + amount);
                con.commit();
            } else{
                throw new SQLException("Updating balance failed");
            }
            return true;

        } catch (Exception e){
            System.out.println("Balance change failed: " + e.getMessage());
            e.printStackTrace();
            try{
                // aborts the transaction and rollbacks any change we made
                System.out.println("Performing rollback");
                con.rollback();
            } catch (SQLException e2){
                System.out.println("Ohhh mahh gahd" + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            System.out.println("Setting auto commit to true");
            try{
                // turns on the autocommit again after transaction ends
                con.setAutoCommit(true);
            }catch (SQLException e3){
                System.out.println("Couldn't reset autocommit " + e3.getMessage());
            }
        }
        // return false if code made it through here
        return false;
    }

    /** REGISTRATION
     * Tries to add client uid top the database. Part of addAllClient Transaction
     * @throws SQLException when no rows are affected
     */
    private void addNewClient(int uid) throws SQLException{
        // generate client uid
        insertNewClient.setInt(1, uid);

        int affectedRows = insertNewClient.executeUpdate();

        if(affectedRows != 1) {
            throw new SQLException("Couldn't add new client");
        }

    }

    /** REGISTRATION
     * Tries to create new account with the use of generators. Part of addAllClient Transaction
     * @param client_uid uid of the client
     * @param acc_type type of the account [savings/checking]
     * @param pin pin of the user
     * @throws SQLException when no rows are affected
     */
    private void addNewAccount(int client_uid, String acc_type, String pin, int acc_uid, String cardNo)
            throws SQLException{
        // generate account uid, and card number -> hash card no and pin here


        insertNewAccount.setInt(1, acc_uid);
        insertNewAccount.setString(2, acc_type);
        insertNewAccount.setInt(3, client_uid);
        insertNewAccount.setString(4, AccountGenerator.hash(cardNo));
        insertNewAccount.setString(5, AccountGenerator.hash(pin));

        int affectedRows = insertNewAccount.executeUpdate();

        if(affectedRows != 1) {
            throw new SQLException("Couldn't add new account");
        }

    }

    /**
     * Creates a transaction and inserts all data into respective table. Method to be used when registering
     * Check all data first**
     * @param acc_type type of account
     * @param pin pin of the user
     * @param info info object that contains registration form inputs
     * @param client_uid uid of the client
     * @param acc_uid uid of the specific account
     * @param cardNo card number of the specific account
     */
    public boolean addAllClientData(String acc_type, String pin, ClientInfo info, int client_uid, int acc_uid,
                                 String cardNo){
        boolean isSuccessful = false;
        try{
            con.setAutoCommit(false);
            //add New Client First and add to client table
            addNewClient(client_uid);
            //create new account (generate uid and card number -> hash the string parameter and generated card uid
            addNewAccount(client_uid, acc_type, pin, acc_uid, cardNo);
            //Add now the client info to client info table
            insertNewClientInfo.setInt(1,client_uid);
            insertNewClientInfo.setString(2, info.getFirstName());
            insertNewClientInfo.setString(3,info.getLastName());
            insertNewClientInfo.setDate(4, java.sql.Date.valueOf(info.getBirthDay()));
            insertNewClientInfo.setString(5, info.getOccupation());
            insertNewClientInfo.setString(6, info.getPurpose());
            insertNewClientInfo.setString(7, info.getAddress());
            insertNewClientInfo.setString(8, info.getEmail());

            int affectedRows = insertNewClientInfo.executeUpdate();
            if (affectedRows == 1){
                System.out.println("Inserted all data successfully");
//                System.out.println("Your Card Number is: " + cardNo);
                con.commit();
                isSuccessful = true;
            } else{
                throw new SQLException("Client info insertion failed");
            }

        } catch (Exception e){
            System.out.println("Insert client Exception: " + e.getMessage());
            e.printStackTrace();
            try{
                // aborts the transaction and rollbacks any change we made
                System.out.println("Performing rollback");
                con.rollback();
            } catch (SQLException e2){
                System.out.println("Ohhh mahh gahd" + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            System.out.println("Setting auto commit to true");
            try{
                // turns on the autocommit again after transaction ends
                con.setAutoCommit(true);
            }catch (SQLException e3){
                System.out.println("Couldn't reset autocommit " + e3.getMessage());
            }
        }

        return isSuccessful;
    }

    /**
     * Records transaction and adds it to transactions table
     * @param transaction transaction object generated after a transaction
     */
    public void recordTransaction(Transaction transaction){
        try{
            insertTransaction.setString(1, transaction.getTransactType());
            insertTransaction.setInt(2, transaction.getAccountId());
            insertTransaction.setDouble(3, transaction.getAmount());
            insertTransaction.setDate(4, java.sql.Date.valueOf(transaction.getDate()));
            insertTransaction.setTime(5, java.sql.Time.valueOf(transaction.getTime()));

            int affectedRows = insertTransaction.executeUpdate();

            if(affectedRows ==1){
                System.out.println("Successfully added transaction");
            }

        } catch (SQLException e){
            System.out.println("Error recording transaction " + e.getMessage());
            e.printStackTrace();
        }
    }
}
