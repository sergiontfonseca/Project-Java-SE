// 1.1.2 Creation of main class for tests
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import components.Account;
import components.Client;
import components.Credit;
import components.CurrentAccount;
import components.Debit;
import components.Flow;
import components.FlowDTO;
import components.SavingsAccount;
import components.Transfer;

public class Main {
    private static final Path FILENAME = Paths.get("src/datafiles/accounts.xml") ;

    public static void main(String[] args) {
        // 1.1.2
        List<Client> clientSet = new ArrayList<>();
        clientSet = genClientSet(5);
        displayList(clientSet);
        System.out.println("\n---------\n");

        // 1.2.3
        List<Account> accountSet = new ArrayList<>(createAccounts(clientSet));
        // 2.2
        accountSet.clear();
        Account.setAccountNumberCount(0);
        accountSet = getAccountsFromXML(clientSet);

        displayList(accountSet);
        System.out.println("\n---------\n");

        // 1.3.1
        Map<Integer, Account> accountMap = getHashtable(accountSet);

        // 1.3.4
        List<Flow> flowList = getFlowList(accountSet);
        // 2.1
        flowList.clear();
        flowList = getFlowListFromJson();

        // 1.3.5
        updateAccountBalances(flowList, accountMap);
        displayAccountsOrderedByBalance(accountMap);
        System.out.println("\n---------\n");

    }

    // method for displaying a List
    private static <X> void displayList(List<X> x) {
        x.forEach(System.out::println);
    }

    // 1.1.2 method for generating clientSet
    private static List<Client> genClientSet(int n) {
        String[] firstNames = { "Aaron", "Bob", "Claire", "John", "Mary", "Monica" };
        String[] names = { "Allen", "Campbell", "Johnson", "Miller", "Parker", "Walker" };
        Random randomFirstName = new Random();
        Random randomName = new Random();
        List<Client> generatedClients = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int firstNameIndex = randomFirstName.nextInt(n+1);
            int nameIndex = randomName.nextInt(n+1);
            generatedClients.add(new Client(firstNames[firstNameIndex], names[nameIndex]));
        }
        return generatedClients;
    }

    // 1.2.3 method for generating Accounts
    private static List<Account> createAccounts(List<Client> clientsList) {
        List<Account> generatedAccs = new ArrayList<>();

        for (Client client : clientsList) {
            generatedAccs.add(new CurrentAccount("CurrentAccount", client));
            generatedAccs.add(new SavingsAccount("SavingsAccount", client));
        }
        return generatedAccs;
    }

    // 1.3.1 method for returning a Hashtable from Accounts
    private static Map<Integer, Account> getHashtable(List<Account> accountList) {
        Map<Integer, Account> generatedHashtable = new Hashtable<>();
        for (Account account : accountList) {
            generatedHashtable.put(account.getAccountNumber(), account);
        }

        return generatedHashtable;
    }

    // 1.3.1 method for displaying the Map in ascending order according to the
    // balance
    private static void displayAccountsOrderedByBalance(Map<Integer, Account> accountMap) {
        accountMap.values().stream().sorted(Comparator.comparingDouble(Account::getBalance))
                .forEach(System.out::println);
    }

    // 1.3.4 method for returning the Flow List
    private static List<Flow> getFlowList(List<Account> accountList) {

        List<Flow> generatedFlows = new ArrayList<>();
        generatedFlows.add(new Debit("Debit", 50, 1));
        for (Account account : accountList) {
            if (account instanceof CurrentAccount) {
                generatedFlows.add(new Credit("Credit", 100.50, account.getAccountNumber()));
            } else {
                generatedFlows.add(new Credit("Credit", 1500, account.getAccountNumber()));
            }
        }
        generatedFlows.add(new Transfer("Transfer", 50, 2, 1));
        return generatedFlows;
    }

    // 1.3.5 method for updating the balances of the accounts
    private static void updateAccountBalances(List<Flow> flows, Map<Integer, Account> accountMap) {
        for (Flow flow : flows) {
            accountMap.get(flow.getTargetAccountNumber()).setBalance(flow);
            if (flow instanceof Transfer) {
                accountMap.get(((Transfer) flow).getOriginAccountNumber()).setBalance(flow);
            }
        }

        Optional<Account> invalidAccount = accountMap.values().stream().filter(account -> account.getBalance() < 0)
                .findAny();

        if (invalidAccount.isPresent()) {
            System.out.println("There's a negative balance!!!");
        }
    }

    // 2.1 JSON file of flows
    private static List<Flow> getFlowListFromJson() {

        Path path = Paths.get("src/datafiles", "Flowsfile.json").toAbsolutePath();
        List<Flow> flowsList = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }

            String jsonString = content.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            List<FlowDTO> flows = objectMapper.readValue(jsonString, new TypeReference<List<FlowDTO>>() {
            });

            for (FlowDTO flow : flows) {
                if (flow.getComment().equalsIgnoreCase("Credit")) {
                    flowsList.add(new Credit(flow.getComment(), flow.getAmount(), flow.getTargetAccountNumber()));
                } else if (flow.getComment().equalsIgnoreCase("Debit")) {
                    flowsList.add(new Debit(flow.getComment(), flow.getAmount(), flow.getTargetAccountNumber()));
                } else {
                    flowsList.add(new Transfer(flow.getComment(), flow.getAmount(), flow.getTargetAccountNumber(),
                            flow.getOriginAccountNumber()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flowsList;
    }

    // 2.2 XML file of account
    private static List<Account> getAccountsFromXML(List<Client> clientsList) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<Account> accountsList = new ArrayList<>();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(String.valueOf(FILENAME)));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("account");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String label = element.getElementsByTagName("label").item(0).getTextContent();
                    int clientn = Integer.parseInt(element.getElementsByTagName("clientn").item(0).getTextContent());

                    for (Client client : clientsList) {
                        int cn = client.getClientNumber();

                        if (clientn == cn && label.equals("SavingsAccount")) {
                            accountsList.add(new SavingsAccount("SavingsAccount", client));
                        } else if (clientn == cn && label.equals("CurrentAccount")) {
                            accountsList.add(new CurrentAccount("CurrentAccount", client));
                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return accountsList;
    }
}
